package uni.leeds.comp3222

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ViewListingActivity : AppCompatActivity() {

    val TAG : String = "VIEW LISTING ACTIVITY"
    private lateinit var firestore : FirebaseFirestore
    private lateinit var listingRef : DocumentReference
    private lateinit var listingSnapshot: DocumentSnapshot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_listing)

        // Get the passed-in listingId from the extras
        val listingId: String? = getIntent().getExtras()?.getString("ListingId")

        if (listingId != null)
        {
            // Initialise the Firestore database reference.
            firestore = FirebaseFirestore.getInstance()

            // Get a reference to the listing using the passed-in listingId.
            listingRef = listingId?.let { firestore.collection("listings").document(it) }!!

            // Collect the listing, store it in a documentSnapshot, populate the UI with the listing info.
            val listRef = firestore.collection("listings").document(listingId)
            listRef.get().addOnSuccessListener{ documentSnapshot ->
                listingSnapshot = documentSnapshot
                onListingLoaded()
            }
        } else {
            throw IllegalArgumentException("A valid listing Id must be passed before a listing can be viewed.")
        }
    }

    private fun onListingLoaded(){
        // Initialise all the UI elements.
        val itemName: TextView = findViewById(R.id.itemName)
        val shortDesc: TextView = findViewById(R.id.shortDesc)
        val longDesc: TextView = findViewById(R.id.longDesc)
        val itemCost: TextView = findViewById(R.id.itemCost)

        itemName.text = listingSnapshot.get("itemName").toString()
        shortDesc.text = listingSnapshot.get("shortDesc").toString()
        longDesc.text = listingSnapshot.get("longDesc").toString()
        itemCost.text = listingSnapshot.get("cost").toString()

        // Set and load the image from the url using glide.
        val itemPhoto: ImageView = findViewById(R.id.photo)
        Glide.with(itemPhoto.getContext())
            .load(listingSnapshot.get("itemPhoto").toString())
            .into(itemPhoto)

        // Do not reveal the UI until the listing has loaded
        revealListing()
    }


    fun openEmailApp(view: View){

        // Acquiring the seller email and item name from the listing snapshot.
        val recipient: Array<String> = arrayOf(listingSnapshot.get("sellerEmail").toString())
        val itemName = listingSnapshot.get("itemName").toString()

        // Preparing the intent to open an email application of the user's choosing
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_EMAIL, recipient)
            putExtra(Intent.EXTRA_SUBJECT, "Impala Listing Inquiry: ${itemName}")
            setType("message/rfc822")
        }

        if (intent.resolveActivity(packageManager) != null){
            startActivity(Intent.createChooser(intent, "Choose an email client:"))
        } else {
            Toast.makeText(this, "Please install an email application", Toast.LENGTH_LONG).show()
        }
    }

    // Little helper function to ensure the UI doesn't show until the listing info was retrieved.
    private fun revealListing(){
        val progressbar: ProgressBar = findViewById(R.id.loadingBar)
        val layout: LinearLayout = findViewById(R.id.mainLinearLayout)

        progressbar.setVisibility(View.INVISIBLE)
        layout.setVisibility(View.VISIBLE)
    }

}
