package com.example.kazuhiroshigenobu.googlemaptraining;

/**
 * Created by KazuhiroShigenobu on 14/5/17.
 */

class ReviewReport {
    String rid;
    String uid;
    String time;
    Double timeNumbers;
    String problem;

    public ReviewReport(String rid, String uid, String time, Double timeNumbers, String problem) {
        this.rid = rid;
        this.uid = uid;
        this.time = time;
        this.timeNumbers = timeNumbers;
        this.problem = problem;
    }
}
