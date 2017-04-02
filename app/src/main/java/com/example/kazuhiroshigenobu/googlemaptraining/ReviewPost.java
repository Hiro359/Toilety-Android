package com.example.kazuhiroshigenobu.googlemaptraining;

/**
 * Created by KazuhiroShigenobu on 30/3/17.
 */

    public class ReviewPost {

        Boolean available;
        String feedback;
        Integer likedCount;
        String star;
        String tid;
        String time;
        Double timeNumbers;
        String uid;
        String waitingtime;
        Boolean userLiked = false;


        public ReviewPost(Boolean available, String feedback, Integer likedCount, String star, String tid, String time, Double timeNumbers, String uid, String waitingtime) {
            this.available = available;
            this.feedback = feedback;
            this.likedCount = likedCount;
            this.star = star;
            this.tid = tid;
            this.time = time;
            this.timeNumbers = timeNumbers;
            this.uid = uid;
            this.waitingtime = waitingtime;
        }


}
