package uni.leeds.comp3222

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import uni.leeds.recycler.SearchAdapter
import kotlinx.android.synthetic.main.activity_search.*


class SearchActivity : AppCompatActivity() {

    private val listings: ArrayList<Listing> = ArrayList()

    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        linearLayoutManager = LinearLayoutManager(this)
        searchResultsList.layoutManager = linearLayoutManager

        val listings = loadMockListings()
        adapter = SearchAdapter(listings)

        searchResultsList.adapter = adapter

    }

    private fun loadMockListings(): ArrayList<Listing>{
        val itemList = arrayListOf<Listing>()
        itemList.add(mockListing("Russell Hobbs", "Make an impression every morning with perfect toast every time from the "))
        itemList.add(mockListing("Digital Solo Microwave", "Packed with functionality and housed within contemporary white marble and rose gold accents,"))
        itemList.add(mockListing("Sharp 55-inch 4K UHD HDR", "Enhance your viewing experience with the 4T-C55BL3KF2AB, 55-inch 4K UHD HDR Android TV."))
        itemList.add(mockListing("Russell Hobbs", "Make an impression every morning with perfect toast every time from the "))
        itemList.add(mockListing("Digital Solo Microwave", "Packed with functionality and housed within contemporary white marble and rose gold accents,"))
        itemList.add(mockListing("Sharp 55-inch 4K UHD HDR", "Enhance your viewing experience with the 4T-C55BL3KF2AB, 55-inch 4K UHD HDR Android TV."))
        itemList.add(mockListing("Russell Hobbs", "Make an impression every morning with perfect toast every time from the "))
        itemList.add(mockListing("Digital Solo Microwave", "Packed with functionality and housed within contemporary white marble and rose gold accents,"))
        itemList.add(mockListing("Sharp 55-inch 4K UHD HDR", "Enhance your viewing experience with the 4T-C55BL3KF2AB, 55-inch 4K UHD HDR Android TV."))
        itemList.add(mockListing("Russell Hobbs", "Make an impression every morning with perfect toast every time from the "))
        itemList.add(mockListing("Digital Solo Microwave", "Packed with functionality and housed within contemporary white marble and rose gold accents,"))
        itemList.add(mockListing("Sharp 55-inch 4K UHD HDR", "Enhance your viewing experience with the 4T-C55BL3KF2AB, 55-inch 4K UHD HDR Android TV."))
        itemList.add(mockListing("Russell Hobbs", "Make an impression every morning with perfect toast every time from the "))
        itemList.add(mockListing("Digital Solo Microwave", "Packed with functionality and housed within contemporary white marble and rose gold accents,"))
        itemList.add(mockListing("Sharp 55-inch 4K UHD HDR", "Enhance your viewing experience with the 4T-C55BL3KF2AB, 55-inch 4K UHD HDR Android TV."))
        itemList.add(mockListing("Russell Hobbs", "Make an impression every morning with perfect toast every time from the "))
        itemList.add(mockListing("Digital Solo Microwave", "Packed with functionality and housed within contemporary white marble and rose gold accents,"))
        itemList.add(mockListing("Sharp 55-inch 4K UHD HDR", "Enhance your viewing experience with the 4T-C55BL3KF2AB, 55-inch 4K UHD HDR Android TV."))
        itemList.add(mockListing("Russell Hobbs", "Make an impression every morning with perfect toast every time from the "))
        itemList.add(mockListing("Digital Solo Microwave", "Packed with functionality and housed within contemporary white marble and rose gold accents,"))
        itemList.add(mockListing("Sharp 55-inch 4K UHD HDR", "Enhance your viewing experience with the 4T-C55BL3KF2AB, 55-inch 4K UHD HDR Android TV."))
        itemList.add(mockListing("Russell Hobbs", "Make an impression every morning with perfect toast every time from the "))
        itemList.add(mockListing("Digital Solo Microwave", "Packed with functionality and housed within contemporary white marble and rose gold accents,"))
        itemList.add(mockListing("Sharp 55-inch 4K UHD HDR", "Enhance your viewing experience with the 4T-C55BL3KF2AB, 55-inch 4K UHD HDR Android TV."))
        itemList.add(mockListing("Russell Hobbs", "Make an impression every morning with perfect toast every time from the "))
        itemList.add(mockListing("Digital Solo Microwave", "Packed with functionality and housed within contemporary white marble and rose gold accents,"))
        itemList.add(mockListing("Sharp 55-inch 4K UHD HDR", "Enhance your viewing experience with the 4T-C55BL3KF2AB, 55-inch 4K UHD HDR Android TV."))
        itemList.add(mockListing("Russell Hobbs", "Make an impression every morning with perfect toast every time from the "))
        itemList.add(mockListing("Digital Solo Microwave", "Packed with functionality and housed within contemporary white marble and rose gold accents,"))
        itemList.add(mockListing("Sharp 55-inch 4K UHD HDR", "Enhance your viewing experience with the 4T-C55BL3KF2AB, 55-inch 4K UHD HDR Android TV."))

        return itemList
    }

    private fun mockListing(itemName:String, shortDesc:String): Listing {
        val listing = Listing()
        listing.itemName = itemName
        listing.shortDesc = shortDesc
        return listing
    }
}
