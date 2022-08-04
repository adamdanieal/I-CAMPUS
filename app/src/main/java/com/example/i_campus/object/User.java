package com.example.i_campus.object;

public class User {
    private String fullname,id,email,password,phonenum,usertype,imageURL;

    public User() {

    }

    public User(String fullname, String id, String email, String password, String phonenum, String usertype, String imageURL) {
        this.fullname = fullname;
        this.id = id;
        this.email = email;
        this.password = password;
        this.phonenum = phonenum;
        this.usertype = usertype;
        this.imageURL = imageURL;

    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


}
