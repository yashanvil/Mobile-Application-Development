package uni.leeds.comp3222

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.buysell.*
import java.io.IOException
import java.util.*

class BuySellActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val PICK_IMAGE_REQUEST = 44
    var filePath: Uri? = null
    internal var storage: FirebaseStorage? = null
    internal var storageRef: StorageReference? = null

    val TAG: String = "Seller Information"
    private lateinit var firestore: FirebaseFirestore

//Spinner initializations
    private var spinner: Spinner? =null
    private var arrayAdapter:ArrayAdapter<String> ? = null

    private var categoryList = arrayOf(
        "Accessories",
        "Electronics",
        "Furniture",
        "Kitchenware",
        "Stationary")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buysell)

        //storage = FirebaseStorage.getInstance()
        //storageRef = FirebaseStorage.getInstance().reference

        //Getting input from the spinner and converting it into String
        spinner = findViewById(R.id.item_spinner)
        arrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, categoryList)
        spinner?.adapter = arrayAdapter
        spinner?.onItemSelectedListener = this

        //Click to Launch the gallery
        imageButton.setOnClickListener { launchGallery()}
        save_button.setOnClickListener { savebuysell()}
    }
//Item Selection Categories
    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(
            applicationContext, "Please select a category",
            Toast.LENGTH_SHORT)
        .show()
        }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Toast.makeText(
            applicationContext,
            " " + spinner?.selectedItem.toString(),
            Toast.LENGTH_LONG
        ).show()

    }

    //trial for spinner
    fun spinnerValue(): String{
        val item_category = spinner?.selectedItem.toString()
        return item_category
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data;
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageButton!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun saveSellerToDB(listing: Listing) {
        firestore.collection("listings")
            .add(listing)
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

    //Error saying savebuysell(view) not found. Maybe I'm not passing the url correctly
    fun savebuysell() {
        var seller_id: EditText = findViewById(R.id.seller_id)
        val sellerId = seller_id.text.toString().trim()

        var seller_email: EditText = findViewById(R.id.seller_email)
        val sellerEmail = seller_email.text.toString().trim()

        var postal: EditText = findViewById(R.id.postal)
        val postCode = postal.text.toString().trim()

        var itemname: EditText = findViewById(R.id.itemname)
        val itemName = itemname.text.toString().trim()

        var long_dec: EditText = findViewById(R.id.long_dec)
        val longDesc = long_dec.text.toString().trim()

        //Trying the code for spinner: Changes needed
        val category = spinnerValue()
        Log.d(TAG, "Received Spinner :${category}")
        //

        var price: EditText = findViewById(R.id.price)
        val cost = price.text.toString().toFloat()

        var short_desc: EditText = findViewById(R.id.short_desc)
        val shortDesc = short_desc.text.toString().trim()

        val imageURL= uploadImage()
        Log.d(TAG, "Received URL :${imageURL}")

        val listing =
            Listing(
                sellerId,
                sellerEmail,
                postCode,
                itemName,
                category,
                shortDesc,
                imageURL,
                cost,
                longDesc
            )

        if (listing != null) {
            firestore = FirebaseFirestore.getInstance()
            saveSellerToDB(listing)
            Toast.makeText(applicationContext, "User saved successfully", Toast.LENGTH_SHORT)
                .show()

        } else {
            Toast.makeText(applicationContext, "Failed to save user", Toast.LENGTH_SHORT).show()
        }
    }

    //To upload the image
    private fun uploadImage(): String {
        var temp_url = ""
        if (filePath != null) {
            //val imageRef = storageRef!!.child("images/*" + UUID.randomUUID().toString())

            val filename = UUID.randomUUID().toString()
            Log.d(TAG, "Uploading file  :${filename}")
            val imageRef = FirebaseStorage.getInstance().getReference("/images/$filename")


            val task = imageRef.putFile(filePath!!)
                .addOnSuccessListener {

                    Toast.makeText(applicationContext, "Image Uploaded", Toast.LENGTH_SHORT).show()
                    //to obtain the url from firebase

                    imageRef.downloadUrl.addOnCompleteListener {
                        Log.d("BuySellActivity", "File Location: $it")
                        //passing to the function savebuysell
                        temp_url = it.toString()
                        //savebuysell(it.toString())

                    }
                }
                .addOnFailureListener {

                    Toast.makeText(applicationContext, "Image Uploading Failed", Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnProgressListener { taskSnapShot ->
                    val progress =
                        100.0 * taskSnapShot.bytesTransferred / taskSnapShot.totalByteCount

                }

        }
        return temp_url
        Log.d(TAG, "URL: ${temp_url}")
    }
}