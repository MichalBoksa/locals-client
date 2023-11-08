package com.example.locals.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;


public class Favorites implements Parcelable {

    private int id;
    private String name;
    private String placeIds;
    private String listImageUrl;
    private Date startDate;
    private Date endDate;
    private User user;

    public Favorites() {
        this.listImageUrl = "https://www.lifeadventures.us/wp-content/uploads/2014/05/109-800x400.jpg";
    }

    public Favorites(Parcel in) {
        name = in.readString();
        listImageUrl = in.readString();
        placeIds = in.readString();
        startDate = new Date(in.readLong());
        endDate = new Date(in.readLong());
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaceIds() {
        return placeIds;
    }

    public void setPlaceIds(String placeIds) {
        this.placeIds = placeIds;
    }

    public String getListImageUrl() {
        return listImageUrl;
    }

    public void setListImageUrl(String listImageUrl) {
        this.listImageUrl = listImageUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(listImageUrl);
        parcel.writeString(placeIds);
        parcel.writeLong(startDate.getTime());
        parcel.writeLong(endDate.getTime());
    }

    public static final Parcelable.Creator<Favorites> CREATOR
            = new Parcelable.Creator<Favorites>() {
        public Favorites createFromParcel(Parcel in) {
            return new Favorites(in);
        }

        @Override
        public Favorites[] newArray(int i) {
            return new Favorites[0];
        }


    };
}
