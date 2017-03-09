package com.example.kazuhiroshigenobu.googlemaptraining;

import android.location.Location;

/**
 * Created by KazuhiroShigenobu on 14/2/17.
 */

public class Toilet {



    String key;
    //Added static March 9
    String type;
    String name;
    Double latitude;
    Double longitude;
    String urlOne;
    String urlTwo;
    String urlThree;
    String distance;
    Boolean washlet = false;
    Boolean wheelchair = false;
    Boolean onlyFemale = false;
    Boolean unisex = false;
    Boolean makeuproom = false;
    Boolean milkspace = false;
    Boolean omutu = false;
    Boolean ostomate = false;
    Boolean japanesetoilet = false;
    Boolean westerntoilet = false;
    Boolean warmSeat = false;
    Boolean baggageSpace = false;
    Boolean autoOpen = false;
    Boolean sensor = false;
    Boolean otohime = false;
    Boolean fancy = false;
    Boolean conforatableWide= false;
    Boolean smell = false;
    Boolean clothes = false;
    Boolean parking = false;
    Boolean english = false;
    Boolean braille = false;
    Integer openHours;
    Integer closeHours;
    String openAndCloseHours;
    String howtoaccess;
    Boolean available = true;
    String tid;
    String addedBy;
    String editedBy;
    Integer reviewCount;
    Location loc;
    String averageStar;
    Integer averageWait;
    String address;


    //Constructor



//    public Toilet(String key, String type, Double star, String urlOne, String urlTwo, String urlThree, Double distance, Boolean washlet, Boolean wheelchair, Boolean onlyFemale, Boolean unisex, Boolean makeuproom, Boolean milkspace, Boolean omutu, Boolean ostomate, Boolean japanesetoilet, Boolean westerntoilet, Boolean warmSeat, Boolean baggageSpace, String openinghours, String howtoaccess, Boolean available, String tid, String addedBy, String editedBy, Integer reviewCount, Location loc, Integer star1, Integer star2, Integer star3, Integer star4, Integer star5, Integer star6, Integer star7, Integer star8, Integer star9, Integer star10, String averageStar, Integer waitingtime, Integer wait1, Integer wait2, Integer wait3, Integer wait4, Integer wait5, Integer averageWait) {
//        this.key = key;
//        this.type = type;
//        this.star = star;
//        this.urlOne = urlOne;
//        this.urlTwo = urlTwo;
//        this.urlThree = urlThree;
//        this.distance = distance;
//        this.washlet = washlet;
//        this.wheelchair = wheelchair;
//        this.onlyFemale = onlyFemale;
//        this.unisex = unisex;
//        this.makeuproom = makeuproom;
//        this.milkspace = milkspace;
//        this.omutu = omutu;
//        this.ostomate = ostomate;
//        this.japanesetoilet = japanesetoilet;
//        this.westerntoilet = westerntoilet;
//        this.warmSeat = warmSeat;
//        this.baggageSpace = baggageSpace;
//        this.openinghours = openinghours;
//        this.howtoaccess = howtoaccess;
//        this.available = available;
//        this.tid = tid;
//        this.addedBy = addedBy;
//        this.editedBy = editedBy;
//        this.reviewCount = reviewCount;
//        this.loc = loc;
//        this.star1 = star1;
//        this.star2 = star2;
//        this.star3 = star3;
//        this.star4 = star4;
//        this.star5 = star5;
//        this.star6 = star6;
//        this.star7 = star7;
//        this.star8 = star8;
//        this.star9 = star9;
//        this.star10 = star10;
//        this.averageStar = averageStar;
//        this.waitingtime = waitingtime;
//        this.wait1 = wait1;
//        this.wait2 = wait2;
//        this.wait3 = wait3;
//        this.wait4 = wait4;
//        this.wait5 = wait5;
//        this.averageWait = averageWait;
//    }

        //Setter, getter

//        public String getKey() {
//            return key;
//        }public void setKey(String key) {
//            this.key = key;
//        }public String getType() {
//            return type;
//        }public void setType(String type) {
//            this.type = type;
//        }public Double getStar() {
//            return star;
//        }public void setStar(Double star) {
//            this.star = star;
//        }public String getUrlOne() {
//            return urlOne;
//        }public void setUrlOne(String urlOne) {
//            this.urlOne = urlOne;
//        }public String getUrlTwo() {
//            return urlTwo;
//        }public void setUrlTwo(String urlTwo) {
//            this.urlTwo = urlTwo;
//        }public String getUrlThree() {
//            return urlThree;
//        }public void setUrlThree(String urlThree) {
//            this.urlThree = urlThree;
//        }public String getDistance() {
//            return distance;
//        }public void setDistance(String distance) {
//            this.distance = distance;
//        }public Boolean getWashlet() {
//            return washlet;
//        }public void setWashlet(Boolean washlet) {
//            this.washlet = washlet;
//        }public Boolean getWheelchair() {
//            return wheelchair;
//        }public void setWheelchair(Boolean wheelchair) {
//            this.wheelchair = wheelchair;
//        }public Boolean getOnlyFemale() {
//            return onlyFemale;
//        }public void setOnlyFemale(Boolean onlyFemale) {
//            this.onlyFemale = onlyFemale;
//        }public Boolean getUnisex() {
//            return unisex;
//        }public void setUnisex(Boolean unisex) {
//            this.unisex = unisex;
//        }public Boolean getMakeuproom() {
//            return makeuproom;
//        }public void setMakeuproom(Boolean makeuproom) {
//            this.makeuproom = makeuproom;
//        }public Boolean getMilkspace() {
//            return milkspace;
//        }public void setMilkspace(Boolean milkspace) {
//            this.milkspace = milkspace;
//        }public Boolean getOmutu() {
//            return omutu;
//        }public void setOmutu(Boolean omutu) {
//            this.omutu = omutu;
//        }public Boolean getOstomate() {
//            return ostomate;
//        }public void setOstomate(Boolean ostomate) {
//            this.ostomate = ostomate;
//        }public Boolean getJapanesetoilet() {
//            return japanesetoilet;
//        }public void setJapanesetoilet(Boolean japanesetoilet) {
//            this.japanesetoilet = japanesetoilet;
//        }public Boolean getWesterntoilet() {
//            return westerntoilet;
//        }public void setWesterntoilet(Boolean westerntoilet) {
//            this.westerntoilet = westerntoilet;
//        }public Boolean getWarmSeat() {
//            return warmSeat;
//        }public void setWarmSeat(Boolean warmSeat) {
//            this.warmSeat = warmSeat;
//        }public Boolean getBaggageSpace() {
//            return baggageSpace;
//        }public void setBaggageSpace(Boolean baggageSpace) {
//            this.baggageSpace = baggageSpace;
//        }public String getOpeninghours() {
//            return openinghours;
//        }public void setOpeninghours(String openinghours) {
//            this.openinghours = openinghours;
//        }public String getHowtoaccess() {
//            return howtoaccess;
//        }public void setHowtoaccess(String howtoaccess) {
//            this.howtoaccess = howtoaccess;
//        }public Boolean getAvailable() {
//            return available;
//        }public void setAvailable(Boolean available) {
//            this.available = available;
//        }public String getTid() {
//            return tid;
//        }public void setTid(String tid) {
//            this.tid = tid;
//        }public String getAddedBy() {
//            return addedBy;
//        }public void setAddedBy(String addedBy) {
//            this.addedBy = addedBy;
//        }public String getEditedBy() {
//            return editedBy;
//        }public void setEditedBy(String editedBy) {
//            this.editedBy = editedBy;
//        }public Integer getReviewCount() {
//            return reviewCount;
//        }public void setReviewCount(Integer reviewCount) {
//            this.reviewCount = reviewCount;
//        }public Location getLoc() {
//            return loc;
//        }public void setLoc(Location loc) {
//            this.loc = loc;
//        }public Integer getStar1() {
//            return star1;
//        }public void setStar1(Integer star1) {
//            this.star1 = star1;
//        }public Integer getStar2() {
//            return star2;
//        }public void setStar2(Integer star2) {
//            this.star2 = star2;
//        }public Integer getStar3() {
//            return star3;
//        }public void setStar3(Integer star3) {
//            this.star3 = star3;
//        }public Integer getStar4() {
//            return star4;
//        }public void setStar4(Integer star4) {
//            this.star4 = star4;
//        }public Integer getStar5() {
//            return star5;
//        }public void setStar5(Integer star5) {
//            this.star5 = star5;
//        }public Integer getStar6() {
//            return star6;
//        }public void setStar6(Integer star6) {
//            this.star6 = star6;
//        }public Integer getStar7() {
//            return star7;
//        }public void setStar7(Integer star7) {
//            this.star7 = star7;
//        }public Integer getStar8() {
//            return star8;
//        }public void setStar8(Integer star8) {
//            this.star8 = star8;
//        }public Integer getStar9() {
//            return star9;
//        }public void setStar9(Integer star9) {
//            this.star9 = star9;
//        }public Integer getStar10() {
//            return star10;
//        }public void setStar10(Integer star10) {
//            this.star10 = star10;
//        }public String getAverageStar() {
//            return averageStar;
//        }public void setAverageStar(String averageStar) {
//            this.averageStar = averageStar;
//        }public Integer getWaitingtime() {
//            return waitingtime;
//        }public void setWaitingtime(Integer waitingtime) {
//            this.waitingtime = waitingtime;
//        }public Integer getWait1() {
//            return wait1;
//        }public void setWait1(Integer wait1) {
//            this.wait1 = wait1;
//        }public Integer getWait2() {
//            return wait2;
//        }public void setWait2(Integer wait2) {
//            this.wait2 = wait2;
//        }public Integer getWait3() {
//            return wait3;
//        }public void setWait3(Integer wait3) {
//            this.wait3 = wait3;
//        }public Integer getWait4() {
//            return wait4;
//        }public void setWait4(Integer wait4) {
//            this.wait4 = wait4;
//        }public Integer getWait5() {
//            return wait5;
//        }public void setWait5(Integer wait5) {
//            this.wait5 = wait5;
//        }public Integer getAverageWait() {
//            return averageWait;
//        }public void setAverageWait(Integer averageWait) {
//            this.averageWait = averageWait;
//
//        }
}
