package uni.leeds.comp3222

/*
 * Data class for adding and retrieving Listings
 * Stores:
 *      Seller (user) who added the listing
 *      Seller's email address
 *      Item name
 *      Item short description
 *      Item photo (link to its location)
 *      Item cost
 *      Item long description
 */

data class Listing (
    val seller : String,
    val email : String,
    var itemName : String,
    var shortDesc : String,
    var itemPhoto : String,
    var cost : Float,
    var longDesc : String )