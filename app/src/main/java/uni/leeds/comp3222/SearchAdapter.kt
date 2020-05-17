package uni.leeds.comp3222

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.listing_item_row.view.*

class SearchAdapter(private val listings: ArrayList<Listing>):
    RecyclerView.Adapter<SearchAdapter.ListingHolder>() {

    override fun getItemCount() = listings.size

    //pass listing object into ListingHolder object
    override fun onBindViewHolder(holder: ListingHolder, position: Int) {
        val itemListing = listings[position]
        holder.bindListing(itemListing)
    }

    //initialize view holder and inflate layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ListingHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.listing_item_row, parent, false) as View

        return ListingHolder(view)

    }

    class ListingHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var view: View = v
        private var listing: Listing? = null

        //set a click listener on view holder
        init {
            v.setOnClickListener(this)
        }

        //handle clicks on search results
        override fun onClick(v: View) {
            val context = itemView.context

            //create an intent to open view listing activity and pass listing object to it
            val intent = Intent(context, ViewListingActivity::class.java)
            intent.putExtra("ListingToLoad", listing)
            context.startActivity(intent)
        }

        //load data for listing object into view holder
        fun bindListing(listing: Listing) {
            this.listing = listing

            Glide.with(view.itemImage.context)
                .load(listing.itemPhoto)
                .centerCrop()
                .into(view.itemImage)

            view.itemTitle.text = listing.itemName
            view.itemDescription.text = listing.shortDesc
        }


    }

}


