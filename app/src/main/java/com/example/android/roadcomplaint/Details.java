package com.example.android.roadcomplaint;

/**
 * Created by Nichay Jain on 11-06-2019.
 */

public class Details {
    public String name;
    public String description;
    public String address;
    public String latitude;
    public String longitude;
    public String image;

    public Details(String name, String description,String address, String latitude, String longitude, String image) {
        this.name = name;
        this.description = description;
        this.address=address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
