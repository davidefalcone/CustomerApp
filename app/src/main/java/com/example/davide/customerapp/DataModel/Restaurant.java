package com.example.davide.customerapp.DataModel;

import java.io.Serializable;
import java.net.URI;

public class Restaurant implements Serializable {
    private String name;
    private String mail;
    private String description;
    private String restaurantAddress;
    private String ID;

    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public String getID() {
        return ID;
    }

    public void setId(String imageURL) {
        this.ID = imageURL;
    }
}
