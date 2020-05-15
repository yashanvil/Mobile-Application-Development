package uni.leeds.comp3222

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.buysell.*


class BuySellActivity: AppCompatActivity() {

    val TAG: String = "Seller Information"
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buysell)
    }

    fun saveToDB(buyerSeller: BuyerSeller) {
        firestore.collection("sellers")
            .add(buyerSeller)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG,
                    "Document Snapshot: ${documentReference.id}"
                )
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error while adding!", exception)
                Toast.makeText(applicationContext, exception.toString(), Toast.LENGTH_LONG)
                    .show()
            }
    }

    fun savebuysell(view: View) {

        var firstname: EditText = findViewById(R.id.firstname)
        val firstName = firstname.text.toString().trim()

        var lastname: EditText = findViewById(R.id.lastname)
        val lastName = lastname.text.toString().trim()

        var address1: EditText = findViewById(R.id.address1)
        val address = address1.text.toString().trim()

        var address2: EditText = findViewById(R.id.address2)
        val addressline2 = address2.text.toString().trim()

        var postal: EditText = findViewById(R.id.postal)
        val postalcode = postal.text.toString().trim()

        var email: EditText = findViewById(R.id.email)
        val emailAdd = email.text.toString().trim()


        //  val ref = FirebaseDatabase.getInstance().getReference("BuyerSellerData")
        //  val buysellId = ref.push().key
        val buyerSeller =
            BuyerSeller(firstName, lastName, address, addressline2, postalcode, emailAdd)

        if (buyerSeller != null) {
            firestore = FirebaseFirestore.getInstance()
            saveToDB(buyerSeller)
            Toast.makeText(applicationContext, "User saved successfully", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(applicationContext, "Failed to save user", Toast.LENGTH_SHORT).show()
        }
    }
}
