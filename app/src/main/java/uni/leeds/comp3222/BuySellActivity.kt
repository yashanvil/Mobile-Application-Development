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

    fun saveToDB(listing: Listing) {
        firestore.collection("sellers")
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

    fun savebuysell(view: View) {

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

        var item_category:EditText = findViewById(R.id.item_category)
        val category = item_category.text.toString().trim()

        var price: EditText = findViewById(R.id.price)
        val cost = price.text.toString().toFloat()

        var short_desc: EditText = findViewById(R.id.short_desc)
        val shortDesc = short_desc.text.toString().trim()

        //temperorary patch just to compile and run the code
        var item_caption: EditText = findViewById(R.id.item_caption)
        val itemPhoto = item_caption.text.toString().trim()
        //Will remove the above code once the image problem will be solved
        val listingobj = Listing(sellerId,
            sellerEmail,
            postCode,
            itemName,
            category,
            shortDesc,
            itemPhoto,
            cost,
            longDesc)

        if (listingobj != null) {
            firestore = FirebaseFirestore.getInstance()
            saveToDB(listingobj)
            Toast.makeText(applicationContext, "User saved successfully", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(applicationContext, "Failed to save user", Toast.LENGTH_SHORT).show()
        }
    }
}


//------------------------------------------------------Image Code-------------------------------------------------

/*
package com.example.impala

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.buysell.*
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(){

    private val PICK_IMAGE_REQUEST = 44
    var filePath: Uri? = null
    internal var storage: FirebaseStorage? = null
    internal var storageRef: StorageReference? = null

    val TAG: String = "Seller Information"
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buysell)

        //storage = FirebaseStorage.getInstance()
        //storageRef = FirebaseStorage.getInstance().reference

        //Click to Launch the gallery
        choose_image.setOnClickListener { launchGallery() }
    }
    private fun saveSellerToDB(lisiting: Listing) {
        firestore.collection("sellers")
            .add(lisitng)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG,
                    "Document Snapshot: ${documentReference.id}"
                )
                uploadImage()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error while adding!", exception)
                Toast.makeText(applicationContext, exception.toString(), Toast.LENGTH_LONG)
                    .show()
            }
    }

//Error saying savebuysell(view) not found. Maybe I'm not passing the url correctly
    fun savebuysell(filepath: String){
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

        var item_category:EditText = findViewById(R.id.item_category)
        val category = item_category.text.toString().trim()

        var price: EditText = findViewById(R.id.price)
        val cost = price.text.toString().toFloat()

        var short_desc: EditText = findViewById(R.id.short_desc)
        val shortDesc = short_desc.text.toString().trim()

            val lisitng =
            Listing(
                sellerId,
                sellerEmail,
                postCode,
                itemName,
                category,
                shortDesc,
                filepath,
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
}

private fun launchGallery() {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_GET_CONTENT
    startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
}

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null)
    {
        filePath =data.data;
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filePath)
            imageView!!.setImageBitmap(bitmap)
        }catch (e:IOException){
            e.printStackTrace()
        }
    }
}
 //To upload the image
private fun uploadImage() {
    if (filePath != null)
    {
        val imageRef = storageRef!!.child("images/*"+ UUID.randomUUID().toString())
      imageRef.putFile(filePath!!)
        .addOnSuccessListener {

          Toast.makeText(applicationContext, "Image Uploaded",Toast.LENGTH_SHORT).show()
            //to obtain the url from firebase
          imageRef.downloadUrl.addOnSuccessListener {
            Log.d("BuySellActivity", "File Location: $it")
              //passing to the function savebuysell
            savebuysell(it.toString())
          }
        }
        .addOnFailureListener{

          Toast.makeText(applicationContext, "Image Uploading Failed",Toast.LENGTH_SHORT).show()
        }
        .addOnProgressListener {taskSnapShot->
          val progress = 100.0 * taskSnapShot.bytesTransferred/taskSnapShot.totalByteCount

        }
    }
  }


 */