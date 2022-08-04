package com.example.i_campus.object;

public class BookedEvent {
    private String UID;
    private String event_id;
    private String id;
    private String status;
    private String check_in;

    public BookedEvent(String UID, String event_id, String id, String status, String check_in) {
        this.UID = UID;
        this.event_id = event_id;
        this.id = id;
        this.status = status;
        this.check_in = check_in;
    }

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
