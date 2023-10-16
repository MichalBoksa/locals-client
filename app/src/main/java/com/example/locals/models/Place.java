package com.example.locals.models;

import java.io.Serializable;
import java.util.List;

public class Place implements Serializable {

    String placeName;
    String description;
    Double price;
    String currency;
    List<String> images;
    String bookingUrl;
    Double rating;

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String cityName) {
        this.placeName = cityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getBookingUrl() {
        return bookingUrl;
    }

    public void setBookingUrl(String bookingUrl) {
        this.bookingUrl = bookingUrl;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
