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
    var sellerId : String = "",
    var sellerEmail : String = "",
    var postcode : String = "",
    var itemName : String = "",
    var category : String = "",
    var shortDesc : String = "",
    var itemPhoto : String = "",
    var cost : Float = 0.0f,
    var longDesc : String = "" ) : Parcelable



