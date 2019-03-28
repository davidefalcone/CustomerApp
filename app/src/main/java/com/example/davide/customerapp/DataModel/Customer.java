package com.example.davide.customerapp.DataModel;

public class Customer {
    private String name;
    private String mail;
    private String description;
    private String deliveryAddress;

    //singleton class
    private static Customer istance;
    //TODO: Could be necessary remove this class and use sharedPreferences instead!
    private Customer() {

    }

    public static Customer getIstance() {
        if(istance == null){
            istance = new Customer();
        }
        return istance;
    }

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

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
