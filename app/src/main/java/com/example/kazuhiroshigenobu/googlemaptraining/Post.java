package com.example.kazuhiroshigenobu.googlemaptraining;

/**
 * Created by KazuhiroShigenobu on 1/3/17.
 */

public class Post {
    public String name;
    public String openAndCloseHours;
    public String type;
    public String urlOne;
    public String urlTwo;
    public String urlThree;
    public String addedBy;
    public String editedBy;
    public String averageStar;
    public String address;
    public String howtoaccess;

    public Integer openHours;
    public Integer closeHours;
    public Integer reviewCount;
    public Integer averageWait;
    public Integer toiletFloor;
    public Double latitude;
    public Double longitude;

    public Boolean available;
    public Boolean japanesetoilet;
    public Boolean westerntoilet;
    public Boolean onlyFemale;
    public Boolean unisex;

    public Boolean washlet;
    public Boolean warmSeat;
    public Boolean autoOpen;
    public Boolean noVirus;
    public Boolean paperForBenki;
    public Boolean cleanerForBenki;
    public Boolean nonTouchWash;

    public Boolean sensorHandWash;
    public Boolean handSoap;
    public Boolean nonTouchHandSoap;
    public Boolean paperTowel;
    public Boolean handDrier;

    public Boolean otohime;
    public Boolean napkinSelling;
    public Boolean makeuproom;
    public Boolean clothes;
    public Boolean baggageSpace;

    public Boolean wheelchair;
    public Boolean wheelchairAccess;
    public Boolean handrail;
    public Boolean callHelp;
    public Boolean ostomate;
    public Boolean english;
    public Boolean braille;
    public Boolean voiceGuide;

    public Boolean fancy;
    public Boolean smell;
    public Boolean confortable;
    public Boolean noNeedAsk;
    public Boolean parking;
    public Boolean airCondition;
    public Boolean wifi;

    public Boolean milkspace;
    public Boolean babyRoomOnlyFemale;
    public Boolean babyRoomMaleEnter;
    public Boolean babyRoomPersonalSpace;
    public Boolean babyRoomPersonalSpaceWithLock;
    public Boolean babyRoomWideSpace;

    public Boolean babyCarRental;
    public Boolean babyCarAccess;
    public Boolean omutu;
    public Boolean hipCleaningStuff;
    public Boolean omutuTrashCan;
    public Boolean omutuSelling;

    public Boolean babySink;
    public Boolean babyWashstand;
    public Boolean babyHotwater;
    public Boolean babyMicrowave;
    public Boolean babyWaterSelling;
    public Boolean babyFoodSelling;
    public Boolean babyEatingSpace;

    public Boolean babyChair;
    public Boolean babySoffa;
    public Boolean kidsToilet;
    public Boolean kidsSpace;
    public Boolean babyHeight;
    public Boolean babyWeight;
    public Boolean babyToy;
    public Boolean babyFancy;
    public Boolean babySmellGood;


    public Post(String name, String openAndCloseHours, String type, String urlOne, String urlTwo, String urlThree, String addedBy, String editedBy, String averageStar, String address, String howtoaccess, Integer openHours, Integer closeHours, Integer reviewCount, Integer averageWait, Integer toiletFloor, Double latitude, Double longitude, Boolean available, Boolean japanesetoilet, Boolean westerntoilet, Boolean onlyFemale, Boolean unisex, Boolean washlet, Boolean warmSeat, Boolean autoOpen, Boolean noVirus, Boolean paperForBenki, Boolean cleanerForBenki, Boolean nonTouchWash, Boolean sensorHandWash, Boolean handSoap, Boolean nonTouchHandSoap, Boolean paperTowel, Boolean handDrier, Boolean otohime, Boolean napkinSelling, Boolean makeuproom, Boolean clothes, Boolean baggageSpace, Boolean wheelchair, Boolean wheelchairAccess, Boolean handrail, Boolean callHelp, Boolean ostomate, Boolean english, Boolean braille, Boolean voiceGuide, Boolean fancy, Boolean smell, Boolean confortable, Boolean noNeedAsk, Boolean parking, Boolean airCondition, Boolean wifi, Boolean milkspace, Boolean babyRoomOnlyFemale, Boolean babyRoomMaleEnter, Boolean babyRoomPersonalSpace, Boolean babyRoomPersonalSpaceWithLock, Boolean babyRoomWideSpace, Boolean babyCarRental, Boolean babyCarAccess, Boolean omutu, Boolean hipCleaningStuff, Boolean omutuTrashCan, Boolean omutuSelling, Boolean babySink, Boolean babyWashstand, Boolean babyHotwater, Boolean babyMicrowave, Boolean babyWaterSelling, Boolean babyFoodSelling, Boolean babyEatingSpace, Boolean babyChair, Boolean babySoffa, Boolean kidsToilet, Boolean kidsSpace, Boolean babyHeight, Boolean babyWeight, Boolean babyToy, Boolean babyFancy, Boolean babySmellGood) {
        this.name = name;
        this.openAndCloseHours = openAndCloseHours;
        this.type = type;
        this.urlOne = urlOne;
        this.urlTwo = urlTwo;
        this.urlThree = urlThree;
        this.addedBy = addedBy;
        this.editedBy = editedBy;
        this.averageStar = averageStar;
        this.address = address;
        this.howtoaccess = howtoaccess;
        this.openHours = openHours;
        this.closeHours = closeHours;
        this.reviewCount = reviewCount;
        this.averageWait = averageWait;
        this.toiletFloor = toiletFloor;
        this.latitude = latitude;
        this.longitude = longitude;
        this.available = available;
        this.japanesetoilet = japanesetoilet;
        this.westerntoilet = westerntoilet;
        this.onlyFemale = onlyFemale;
        this.unisex = unisex;
        this.washlet = washlet;
        this.warmSeat = warmSeat;
        this.autoOpen = autoOpen;
        this.noVirus = noVirus;
        this.paperForBenki = paperForBenki;
        this.cleanerForBenki = cleanerForBenki;
        this.nonTouchWash = nonTouchWash;
        this.sensorHandWash = sensorHandWash;
        this.handSoap = handSoap;
        this.nonTouchHandSoap = nonTouchHandSoap;
        this.paperTowel = paperTowel;
        this.handDrier = handDrier;
        this.otohime = otohime;
        this.napkinSelling = napkinSelling;
        this.makeuproom = makeuproom;
        this.clothes = clothes;
        this.baggageSpace = baggageSpace;
        this.wheelchair = wheelchair;
        this.wheelchairAccess = wheelchairAccess;
        this.handrail = handrail;
        this.callHelp = callHelp;
        this.ostomate = ostomate;
        this.english = english;
        this.braille = braille;
        this.voiceGuide = voiceGuide;
        this.fancy = fancy;
        this.smell = smell;
        this.confortable = confortable;
        this.noNeedAsk = noNeedAsk;
        this.parking = parking;
        this.airCondition = airCondition;
        this.wifi = wifi;
        this.milkspace = milkspace;
        this.babyRoomOnlyFemale = babyRoomOnlyFemale;
        this.babyRoomMaleEnter = babyRoomMaleEnter;
        this.babyRoomPersonalSpace = babyRoomPersonalSpace;
        this.babyRoomPersonalSpaceWithLock = babyRoomPersonalSpaceWithLock;
        this.babyRoomWideSpace = babyRoomWideSpace;
        this.babyCarRental = babyCarRental;
        this.babyCarAccess = babyCarAccess;
        this.omutu = omutu;
        this.hipCleaningStuff = hipCleaningStuff;
        this.omutuTrashCan = omutuTrashCan;
        this.omutuSelling = omutuSelling;
        this.babySink = babySink;
        this.babyWashstand = babyWashstand;
        this.babyHotwater = babyHotwater;
        this.babyMicrowave = babyMicrowave;
        this.babyWaterSelling = babyWaterSelling;
        this.babyFoodSelling = babyFoodSelling;
        this.babyEatingSpace = babyEatingSpace;
        this.babyChair = babyChair;
        this.babySoffa = babySoffa;
        this.kidsToilet = kidsToilet;
        this.kidsSpace = kidsSpace;
        this.babyHeight = babyHeight;
        this.babyWeight = babyWeight;
        this.babyToy = babyToy;
        this.babyFancy = babyFancy;
        this.babySmellGood = babySmellGood;
    }
}
