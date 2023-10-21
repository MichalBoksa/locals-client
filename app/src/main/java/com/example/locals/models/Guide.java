package com.example.locals.models;

import java.util.List;

public class Guide {
    String name;
    String lastName;
    List<String> languages;
    Integer price;
    String guideCity;
    List<String> Activities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        Activities = activities;
    }
}
