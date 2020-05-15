package uni.leeds.comp3222

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

data class Listing (
    val sellerId : String,
    val sellerEmail : String,
    val postcode : String,
    var itemName : String,
    val category : String,
    var shortDesc : String,
    var itemPhoto : String,
    var cost : Float,
    var longDesc : String )
{
   constructor():this("", "", "", "","", "", "",0.0f, "")
}