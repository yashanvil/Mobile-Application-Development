package uni.leeds.comp3222

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up Menu variables
        drawerLayout = findViewById(R.id.menu_drawer)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)

        // Configure the toolbar
        setSupportActionBar(toolbar)

        // Configure the Drawer Menu
        var toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val loginButton = findViewById<Button>(R.id.openLogin)
        loginButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val searchButton = findViewById<Button>(R.id.openSearch)
        searchButton.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        val addListing = findViewById<Button>(R.id.addListing)
        addListing.setOnClickListener{
            val intent = Intent(this, BuySellActivity::class.java)
            startActivity(intent)
        }

        val takePhoto = findViewById<Button>(R.id.takePhoto)
        takePhoto.setOnClickListener{
            val intent = Intent(this, TakePhotoActivity::class.java)
            startActivity(intent)
        }
    }

}
