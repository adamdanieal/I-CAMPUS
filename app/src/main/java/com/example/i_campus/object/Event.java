package com.example.i_campus.object;

public class Event {
    private String name,venue,imageURL,id,month,date;

    public Event(String name, String venue, String imageURL,String id,String month,String date) {
        this.name = name;
        this.venue = venue;
        this.imageURL = imageURL;
        this.id = id;
        this.month = month;
        this.date = date;
    }

    public Event() {

    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
