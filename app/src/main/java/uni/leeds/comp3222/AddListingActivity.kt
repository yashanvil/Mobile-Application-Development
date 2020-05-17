package uni.leeds.comp3222

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_add_listing.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class AddListingActivity : AppCompatActivity() {

    private val requestPhotoCode = 1

    private lateinit var listing: Listing
    private var user: FirebaseUser? = null

    private lateinit var currentPhotoPath: String
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var photoURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_listing)

        user = FirebaseAuth.getInstance().currentUser

        if( user == null) {
            makeToast("You need to be logged in to add a listing")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        firestore = FirebaseFirestore.getInstance()

        storage = Firebase.storage

        photoButton.setOnClickListener {
            dispatchTakePictureIntent()
        }

        saveListingButton.setOnClickListener {
            if(validateFormFilled()) {
                buildListingObjectAndSendToFirestore()
            }

        }
    }

    private fun buildListingObjectAndSendToFirestore() {
        listing = Listing()

        listing.sellerId = user?.uid.toString()
        listing.sellerEmail = user?.email.toString()
        listing.itemPhoto = photoURL
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
//Validating if all the fields are filled on the form
    private fun validateFormFilled(): Boolean {
        val isFormFilled: Boolean
        when {
            (photoURL.trim().isEmpty())->{
                makeToast("You need to add a photo.")
                isFormFilled = false
            }
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
        return editText.text.toString().trim().isEmpty()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestPhotoCode && resultCode == RESULT_OK) {
            val file = File(currentPhotoPath)
            setPic()
            uploadImageToFirestore(file)
        }
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "uni.leeds.comp3222.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, requestPhotoCode)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun setPic() {
        // Get the dimensions of the View
        val targetW: Int = imageView.width
        val targetH: Int = imageView.height

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
            imageView.setImageBitmap(bitmap)
        }
    }

    //Upload the Image to Firestore in the form of URL
    private fun uploadImageToFirestore(file: File) {
        val storageRef = storage.reference

        val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

        val fileUri = Uri.fromFile(file)

        val uploadTask = imageRef.putFile(fileUri)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnSuccessListener {
        //Download the URL
            imageRef.downloadUrl.addOnSuccessListener { url ->
                photoURL = url.toString()
            }

        }
    }
}
