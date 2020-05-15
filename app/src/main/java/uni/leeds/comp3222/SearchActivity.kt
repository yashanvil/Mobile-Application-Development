package uni.leeds.comp3222

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import uni.leeds.recycler.SearchAdapter
import kotlinx.android.synthetic.main.activity_search.*


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

        loadListings()
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
