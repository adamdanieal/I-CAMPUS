package com.example.i_campus.object;

public class Club {
    private String name;
    private String linkURL;
    private String imageURL;
    private String id;

    public Club(String name, String linkURL, String imageURL,String id) {
        this.name = name;
        this.linkURL = linkURL;
        this.imageURL = imageURL;
        this.id = id;
    }

    public Club() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getlinkURL() {
        return linkURL;
    }

    public void setlinkURL(String linkURL) {
        this.linkURL = linkURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
