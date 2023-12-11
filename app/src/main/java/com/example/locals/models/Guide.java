package com.example.locals.models;


import java.io.Serializable;

public class Guide implements Serializable {
   private int id;
   private String name;
   private String city;
   private String email;
   private String languages;
   private Integer price;
   private String phoneNumber;
   private String aboutMe;
   private String whatToOffer;
   private String imageURL;
   private String activities;

    public Guide(int id, String name, String city, String email, String languages, Integer price, String phoneNumber, String aboutMe, String whatToOffer, String imageURL, String activities) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.email = email;
        this.languages = languages;
        this.price = price;
        this.phoneNumber = phoneNumber;
        this.aboutMe = aboutMe;
        this.whatToOffer = whatToOffer;
        this.imageURL = imageURL;
        this.activities = activities;
    }

    public Guide(int id) {
        this.id = id;
    }
    public Guide() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }


    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getWhatToOffer() {
        return whatToOffer;
    }

    public void setWhatToOffer(String whatToOffer) {
        this.whatToOffer = whatToOffer;
    }

    public String getActivities() {
        return activities;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
