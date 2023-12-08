package com.example.locals.models;

import java.util.List;

public class LocationDetails {
        private int location_id;
        private String name;
        private String description;
        private String web_url;
        private LocationAddress address_obj;
        private Double rating;
        private List<String> imagesURLList;
        private Integer num_reviews;

        public int getLocation_id() {
            return location_id;
        }

        public void setLocation_id(int location_id) {
            this.location_id = location_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getWeb_url() {
            return web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }

        public LocationAddress getAddress_obj() {
            return address_obj;
        }

        public void setAddress_obj(LocationAddress address_obj) {
            this.address_obj = address_obj;
        }

        public Double getRating() {
            return rating;
        }

        public void setRating(Double rating) {
            this.rating = rating;
        }

        public List<String> getImagesURLList() {
            return imagesURLList;
        }

        public void setImagesURLList(List<String> imagesURLList) {
            this.imagesURLList = imagesURLList;
        }

    public Integer getNum_reviews() {
        return num_reviews;
    }

    public void setNum_reviews(Integer num_reviews) {
        this.num_reviews = num_reviews;
    }
}
