package uni.leeds.comp3222

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_search.*
import uni.leeds.recycler.SearchAdapter


class SearchActivity : AppCompatActivity() {

    private val listings: ArrayList<Listing> = ArrayList()

    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var adapter: SearchAdapter

    private lateinit var firestore : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        firestore = FirebaseFirestore.getInstance()

        linearLayoutManager = LinearLayoutManager(this)
        searchResultsList.layoutManager = linearLayoutManager

        adapter = SearchAdapter(listings)

        searchResultsList.adapter = adapter

        clearResults.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchField.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false)
        }

        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                performSearch(query)
                clearResults.visibility = View.VISIBLE
            }
        } else {
            loadListings()
        }
        
    }

    private fun performSearch(query: String) {

        firestore.collection("listings")
            .whereEqualTo("itemName", query)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val listing = document.toObject(Listing::class.java)
                    listings.add(listing)
                    adapter.notifyItemChanged(listings.size - 1)
                }
            }
            .addOnFailureListener { exception ->
                //TODO handle failure to load data
            }

    }

    private fun loadListings() {
        firestore.collection("listings").get()
            .addOnSuccessListener { documents  ->
                for (document in documents) {
                    val listing = document.toObject(Listing::class.java)
                    listings.add(listing)
                    adapter.notifyItemChanged(listings.size - 1)
                }


            }
            .addOnFailureListener { exception ->
                //TODO handle failure to load data
            }


    }

}
