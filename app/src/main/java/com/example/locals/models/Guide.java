package com.example.locals.models;

import java.util.List;

public class Guide {
   private String name;
   private List<String> languages;
   private Integer price;
   private String guideCity;
   private String description;
   private List<String> Activities;
   private String ImageURL;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getGuideCity() {
        return guideCity;
    }

    public void setGuideCity(String guideCity) {
        this.guideCity = guideCity;
    }

    public List<String> getActivities() {
        return Activities;
    }

    public void setActivities(List<String> activities) {
        this.Activities = activities;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String ImageURL) {
        this.ImageURL = ImageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
