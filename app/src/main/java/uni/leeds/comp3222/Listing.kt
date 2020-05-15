package uni.leeds.comp3222

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
 * Data class for adding and retrieving Listings
 * Stores:
 *      Seller Id, which links back to the id of the user who created the listing.
 *      Item name
 *      Item short description
 *      Item photo (link to its location)
 *      Item cost
 *      Item long description
 *
 * Each listing is attached to the user who posted it.
 */
@Parcelize
data class Listing (
    val sellerId : String = "",
    val sellerEmail : String = "",
    val postcode : String = "",
    val itemName : String = "",
    val category : String = "",
    val shortDesc : String = "",
    val itemPhoto : String = "",
    val cost : Float = 0.0f,
    val longDesc : String = "" ) : Parcelable



