package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;
import java.util.ArrayList;

public class TotalCounts implements Serializable {
    private Request request;
    private long checkin;
    private Float hstatus,rstatus;
    private String purpose,badge,host,user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    private ArrayList<String>livepic;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public long getCheckin() {
        return checkin;
    }

    public void setCheckin(long checkin) {
        this.checkin = checkin;
    }

    public Float getHstatus() {
        return hstatus;
    }

    public void setHstatus(Float hstatus) {
        this.hstatus = hstatus;
    }

    public Float getRstatus() {
        return rstatus;
    }

    public void setRstatus(Float rstatus) {
        this.rstatus = rstatus;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public ArrayList<String> getLivepic() {
        return livepic;
    }

    public void setLivepic(ArrayList<String> livepic) {
        this.livepic = livepic;
    }
}
