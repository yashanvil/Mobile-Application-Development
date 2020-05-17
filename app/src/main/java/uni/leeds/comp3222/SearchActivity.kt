package uni.leeds.comp3222

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.search.saas.Client
import com.algolia.search.saas.Query
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_search.*
import org.json.JSONArray


class SearchActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val listings: ArrayList<Listing> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: SearchAdapter
    private lateinit var firestore: FirebaseFirestore

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //initialize firestore object for retrieving item listings
        firestore = FirebaseFirestore.getInstance()
        linearLayoutManager = LinearLayoutManager(this)
        searchResultsList.layoutManager = linearLayoutManager

        //create searchAdapter and initialise it
        adapter = SearchAdapter(listings)
        searchResultsList.adapter = adapter

        //when user clicks clear button, restart activity
        clearResults.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        //configure SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchField.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isIconifiedByDefault = false
        }

        //if its a search, fetch results of query otherwise load all listings
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                performSearch(query)
                if(query.length > 15) {
                    searchTerm.text = "Search: ${query.take(13)}..."
                } else {
                    searchTerm.text =  "Search: $query"
                }
                searchField.visibility = View.INVISIBLE
                searchTerm.visibility = View.VISIBLE
                clearResults.visibility = View.VISIBLE
            }
        } else {
            loadListings()
        }

        // Menu related set up
        supportActionBar?.setDisplayShowHomeEnabled(false)
        configureMenu()
        checkLogin()
    }



    private fun performSearch(queryString: String) {
        //init algoria client object with api & api key
        val client = Client(
            getString(R.string.algolia_app_id),
            getString(R.string.algolia_search_api_key)
        )

        val index = client.getIndex("listings")

        val query: Query = Query(queryString)
            .setAttributesToRetrieve("*")
            .setHitsPerPage(50)

        //Send query to algoria to get search results,
        index.searchAsync(query) { content, _ ->
            val hits: JSONArray = content!!.getJSONArray("hits")
            for (i in 0 until hits.length()) {
                val hit = hits.getJSONObject(i)
                // look up listings by ID and pass to search adapter
                firestore.collection("listings")
                    .document(hit.getString("objectID")).get()
                    .addOnSuccessListener { document ->
                        val listing = document.toObject(Listing::class.java)!!
                        listings.add(listing)
                        adapter.notifyItemChanged(listings.size - 1)
                    }
            }
        }
    }

    //if no search query entered in searchView load all listings
    private fun loadListings() {
        firestore.collection("listings").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val listing = document.toObject(Listing::class.java)
                    listings.add(listing)
                    adapter.notifyItemChanged(listings.size - 1)
                }
            }
    }

    // Menu Related Functions
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun configureMenu() {
        // Set up Menu variables
        drawerLayout = findViewById(R.id.menu_drawer)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)

        // Configure the toolbar
        setSupportActionBar(toolbar)
        navigationView.setNavigationItemSelectedListener(this)

        // Configure the Drawer Menu
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun checkLogin() {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        changeMenuIfLoggedIn(user)
        setUsernameInHeader(user)
    }

    private fun changeMenuIfLoggedIn(user: FirebaseUser?) {
        val loginItem: MenuItem = navigationView.menu.findItem(R.id.nav_login)
        val logoutItem: MenuItem = navigationView.menu.findItem(R.id.nav_logout)

        if (user != null) {
            // Hide the Login option
            loginItem.isVisible = false
            logoutItem.isVisible = true
        } else {
            // Hide the Logout option
            loginItem.isVisible = true
            logoutItem.isVisible = false
        }
    }

    private fun setUsernameInHeader(user: FirebaseUser?) {
        val header: View = navigationView.getHeaderView(0)
        val userTextView: TextView = header.findViewById(R.id.usernameText)

        if (user != null) {
            userTextView.text = "${user.displayName.toString()}!"
        } else {
            userTextView.text = "visitor!"
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            intent = when (item.itemId) {
                R.id.nav_logout -> Intent(this, LoginActivity::class.java)
                R.id.nav_addListing -> Intent(this, AddListingActivity::class.java)
                else -> {
                    return false
                }
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        startActivity(intent)
        return true
    }

    override fun onResume() {
        checkLogin()
        super.onResume()
    }

    override fun onRestart() {
        checkLogin()
        super.onRestart()
    }

}
