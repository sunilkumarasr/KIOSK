package com.provizit.kioskcheckin.services;

import java.io.Serializable;

public class MeetingDetailsItems implements Serializable {

    private long date;
    private String location;
    private String status;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
