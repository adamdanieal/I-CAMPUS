package com.example.i_campus.object;

public class BookedFacility {

    private String uid;
    private String facilityid;
    private String check_in;
    private String check_out;
    private String date;
    private String status;
    private String id;

    public BookedFacility(String uid,String facilityid, String check_in, String check_out, String date, String status, String id) {
        this.uid = uid;
        this.facilityid = facilityid;
        this.check_in = check_in;
        this.check_out = check_out;
        this.date = date;
        this.status = status;
        this.id = id;
    }

    public String getFacilityid() {
        return facilityid;
    }

    public void setFacilityid(String facilityid) {
        this.facilityid = facilityid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }

    public String getCheck_out() {
        return check_out;
    }

    public void setCheck_out(String check_out) {
        this.check_out = check_out;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
