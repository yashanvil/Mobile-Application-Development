package uni.leeds.comp3222

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureMenu()
        checkLogin()

        val loginButton = findViewById<Button>(R.id.openLogin)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val searchButton = findViewById<Button>(R.id.openSearch)
        searchButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        val addListing = findViewById<Button>(R.id.addListing)
        addListing.setOnClickListener {
            val intent = Intent(this, BuySellActivity::class.java)
            startActivity(intent)
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
        var toggle = ActionBarDrawerToggle(
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

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

        val takePhoto = findViewById<Button>(R.id.takePhoto)
        takePhoto.setOnClickListener{
            val intent = Intent(this, TakePhotoActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        checkLogin()
        super.onResume()
    }

    override fun onRestart() {
        checkLogin()
        super.onRestart()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        if (FirebaseAuth.getInstance().currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            intent = when (item.itemId) {
                R.id.nav_logout -> Intent(this, LoginActivity::class.java)
                R.id.nav_addListing -> Intent(this, BuySellActivity::class.java)
                else -> {
                    return false
                }
            }
        }
        startActivity(intent)
        return true
    }
}
