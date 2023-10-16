package com.example.locals.models;

public class City {
    String cityName;
    String imageURL;

    public City(String cityName, String imageURL) {
        this.cityName = cityName;
        this.imageURL = imageURL;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
