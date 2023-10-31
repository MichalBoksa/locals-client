package com.example.locals.models;

import java.time.LocalDate;
import java.util.List;

public class Favourites {
   private String name;
   private LocalDate date;
    private List<Place> placeList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Place> getPlaceList() {
        return placeList;
    }

    public void setPlaceList(List<Place> placeList) {
        this.placeList = placeList;
    }
}
