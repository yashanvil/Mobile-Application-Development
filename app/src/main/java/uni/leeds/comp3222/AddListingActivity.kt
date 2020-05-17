package uni.leeds.comp3222

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_listing.*

class AddListingActivity : AppCompatActivity() {

    lateinit var listing: Listing
    lateinit var user: FirebaseUser
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_listing)

        user = FirebaseAuth.getInstance().currentUser!!

        if( user == null) {
            makeToast("You need to be logged in to add a listing")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        firestore = FirebaseFirestore.getInstance()

        saveListingButton.setOnClickListener() {
            if(validateFormFilled()) {
                buildListingObjectAndSendToFirestore()
            }

        }
    }

    private fun buildListingObjectAndSendToFirestore() {
        listing = Listing()

        listing.sellerId = user.uid.toString()
        listing.sellerEmail = user.email.toString()
        listing.itemName = itemName.text.toString()
        listing.cost = price.text.toString().toFloat()
        listing.category = category.selectedItem.toString()
        listing.shortDesc = shortDesc.text.toString()
        listing.longDesc = longDesc.text.toString()
        listing.postcode = postcode.text.toString()

        firestore.collection("listings")
            .add(listing)
            .addOnSuccessListener {
                makeToast("You've successfully put your item up for sale.")
                val intent = Intent(this, ViewListingActivity::class.java)
                intent.putExtra("ListingToLoad", listing)
                startActivity(intent)
            }
    }

    private fun validateFormFilled(): Boolean {
        val isFormFilled: Boolean
        when {
            checkEditTextEmpty(itemName) -> {
                makeToast("You need to add a title.")
                isFormFilled = false
            }
            checkEditTextEmpty(price) -> {
                makeToast("You need to add a price.")
                isFormFilled = false
            }
            checkEditTextEmpty(shortDesc) -> {
                makeToast("You need to add a short description.")
                isFormFilled = false
            }
            checkEditTextEmpty(longDesc) -> {
                makeToast("You need to add a long description.")
                isFormFilled = false
            }
            checkEditTextEmpty(postcode) -> {
                makeToast("You need to add a postal code.")
                isFormFilled = false
            }
            else -> {
                isFormFilled = true
            }
        }

        return isFormFilled
    }

    private fun makeToast(toastText: String) {
        Toast.makeText(applicationContext, toastText, Toast.LENGTH_LONG).show()
    }

    private fun checkEditTextEmpty(editText: EditText) : Boolean {
        return editText.text.toString().trim().length <= 0
    }
}
