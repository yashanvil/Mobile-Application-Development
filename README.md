# University deals (UniDeals)
## Made By Team Impala
* Maaike de Jong
* Barry O'Driscoll
* Yash Mehta

## Description
The application offers a means for students to offer items up for sale, or to buy items put up for sale by others.  
The application consists of 3 activities and makes use of a firebase backend to ensure consistent content access between the activities along with google accounts authentication to manage user sessions. 

For a full breakdown of the application's functionality, visit the wiki:  
[UniDeals Wiki](https://gitlab.com/comp3222/20/impala/-/wikis/UniDeals-Home)

Records of the team's meetings can be found here:  
[Meeting Minutes](https://leeds365-my.sharepoint.com/:f:/g/personal/sc19mald_leeds_ac_uk/EqFJYdrfkQdEjOe31eoliJ8BlVCbhfMCYX_1z5PqlU7JmQ?e=4Kqtte)

## API usage
### Simple API
1. The application allows users to use the device's camera to take a picture
2. The application can open an email application installed on the user's device

## Advanced API
1. Firebase Firestore to store the listings
2. Firebase Authentication to authenticate users, and allow logging in
3 Firebase Storage to store images

## Quick Activity Breakdown
### Add Listing Activity
1. Upload a picture
2. Insert item information

### Search Activity
1. View all listings
2. Search by keywords

### View Listing Activity
1. View full information on a selected listing
2. Contact the listing's seller