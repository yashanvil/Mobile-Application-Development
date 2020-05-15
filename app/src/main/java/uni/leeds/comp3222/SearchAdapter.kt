package uni.leeds.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.listing_item_row.view.*
import uni.leeds.comp3222.Listing
import uni.leeds.comp3222.R

class SearchAdapter(private val listings: ArrayList<Listing>):
    RecyclerView.Adapter<SearchAdapter.ListingHolder>() {

    override fun getItemCount() = listings.size

    override fun onBindViewHolder(holder: SearchAdapter.ListingHolder, position: Int) {
        val itemListing = listings[position]
        holder.bindListing(itemListing)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            SearchAdapter.ListingHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.listing_item_row, parent, false) as View

        return ListingHolder(view)

    }

    class ListingHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var view: View = v
        private var listing: Listing? = null


        init {
            v.setOnClickListener(this)
        }


        override fun onClick(v: View) {
            //TODO add opening view listing
        }

        fun bindListing(listing: Listing) {
            this.listing = listing

            Glide.with(view.itemImage.getContext())
                .load(listing.itemPhoto)
                .centerCrop()
                .into(view.itemImage)

            view.itemTitle.text = listing.itemName
            view.itemDescription.text = listing.shortDesc
        }


    }

}


