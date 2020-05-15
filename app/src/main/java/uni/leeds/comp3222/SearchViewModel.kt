package uni.leeds.comp3222

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class SearchViewModel: ViewModel() {
    init {
        Log.i("SearchViewModel", "SearchViewModel created!")
    }

    val firestore = FirebaseFirestore.getInstance()

    var searchResults : MutableLiveData<List<Listing>> = MutableLiveData()

    fun getSearchResults(): LiveData<List<Listing>> {

        var searchResultsList = mutableListOf<Listing>()

        firestore.collection("listings")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val currentListing: Listing

                    if (document.exists()) {
                        currentListing = document.toObject(Listing::class.java)
                        searchResultsList.add(currentListing)
                    }
                }

                searchResults.value = searchResultsList
            }
            .addOnFailureListener { exception ->
                //TODO handle exceptions when search fails
            }

        return searchResults
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("SearchViewModel", "SearchViewModel destroyed!")
    }


}