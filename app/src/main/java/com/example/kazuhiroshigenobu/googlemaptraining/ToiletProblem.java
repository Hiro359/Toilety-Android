package com.example.kazuhiroshigenobu.googlemaptraining;

/**
 * Created by KazuhiroShigenobu on 14/5/17.
 */

class ToiletProblem {
    String tid;
    String uid;
    String time;
    Double timeNumbers;
    String problem;


    public ToiletProblem(String tid, String uid, String time, Double timeNumbers, String problem) {
        this.tid = tid;
        this.uid = uid;
        this.time = time;
        this.timeNumbers = timeNumbers;
        this.problem = problem;
    }
}
