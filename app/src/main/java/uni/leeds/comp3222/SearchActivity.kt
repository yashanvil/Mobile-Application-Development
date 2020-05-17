package uni.leeds.comp3222

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.search.saas.AlgoliaException
import com.algolia.search.saas.Client
import com.algolia.search.saas.CompletionHandler
import com.algolia.search.saas.Query
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_search.*
import org.json.JSONArray
import org.json.JSONObject
import uni.leeds.recycler.SearchAdapter


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

        clearResults.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchField.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false)
        }

        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                performSearch(query)
                clearResults.visibility = View.VISIBLE
            }
        } else {
            loadListings()
        }
        
    }

    private fun performSearch(queryString: String) {

        var client:Client = Client(getString(R.string.algolia_app_id),
            getString(R.string.algolia_search_api_key))

        var index = client.getIndex("listings")

        var query: Query = Query(queryString)
            .setAttributesToRetrieve("*")
            .setHitsPerPage(50)

        index.searchAsync(query, object : CompletionHandler {
            override fun requestCompleted(content: JSONObject?, error: AlgoliaException?) {
                var hits: JSONArray = content!!.getJSONArray("hits")
                for (i in 0 until hits.length()) {
                    val hit = hits.getJSONObject(i)

                    var listing = firestore.collection("listings")
                        .document(hit.getString("objectID")).get()
                        .addOnSuccessListener { document ->
                            val listing = document.toObject(Listing::class.java)!!
                            listings.add(listing)
                            adapter.notifyItemChanged(listings.size - 1)
                        }

                }
            }
        })
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
