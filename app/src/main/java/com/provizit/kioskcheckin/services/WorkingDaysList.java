package com.provizit.kioskcheckin.services;

import java.io.Serializable;

public class WorkingDaysList implements Serializable {

    boolean status;
    String start;
    String end;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
