package uni.leeds.comp3222

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
    }

    fun openViewListing(view: View){
        val intent = Intent(this, ViewListingActivity::class.java)

        // Normally you'd want this to dynamically grab the id of the listing the user pressed, but this is just quick and dirty for testing.
        intent.putExtra("ListingId", "jgvsh4wwCOArD6CS2Jio")
        startActivity(intent)
    }
}
