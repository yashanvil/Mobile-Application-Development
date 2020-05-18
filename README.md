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

The application's release APK can be found here:  
[Release-APK](https://leeds365-my.sharepoint.com/:f:/g/personal/sc19mald_leeds_ac_uk/EhE-CjB0vSpLpX57KdloQd4BJTHdEzLTM0b0LosoqaUcLg?e=fIEOKI)  

The team's document repository can be found here:  
[Document Repository](https://leeds365-my.sharepoint.com/:f:/g/personal/sc19mald_leeds_ac_uk/EunUaTlRwItPvvRNx4kQv9AB1EQ7jbTh9TpeOk6dcDCz6g?e=BUJKP3)

Asana task tracker board:   
[Screenshot](https://i.imgur.com/23de6wd.png)

## API usage
### Simple API
1. The application allows users to use the device's camera to take a picture
2. The application can open an email application installed on the user's device

## Advanced API
1. Firebase Firestore to store the listings
2. Firebase Authentication to authenticate users, and allow logging in
3. Firebase Storage to store images
4. Algoria indexing to allow full text searching of firebase data

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