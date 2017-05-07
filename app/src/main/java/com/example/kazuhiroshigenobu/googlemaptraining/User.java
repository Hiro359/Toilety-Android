package com.example.kazuhiroshigenobu.googlemaptraining;

/**
 * Created by KazuhiroShigenobu on 13/2/17.
 *
 */

class User {

        public String userName;
        public String password;
        public String userPhoto;
        public String userEmail;
        public int totalLikedCount;
        public int totalHelpedCount;
        public int totalFavoriteCount;


        public User(){
            // ...

    }

    User(String userName, String password, String userPhoto, String userEmail, int totalLikedCount, int totalHelpedCount, int totalFavoriteCount) {
        this.userName = userName;
        this.password = password;
        this.userPhoto = userPhoto;
        this.userEmail = userEmail;
        this.totalLikedCount = totalLikedCount;
        this.totalHelpedCount = totalHelpedCount;
        this.totalFavoriteCount = totalFavoriteCount;
    }
}
