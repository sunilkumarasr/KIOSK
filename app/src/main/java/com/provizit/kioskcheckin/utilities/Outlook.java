package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;

public class Outlook implements Serializable {
    private String password,ihost,ohost;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIhost() {
        return ihost;
    }

    public void setIhost(String ihost) {
        this.ihost = ihost;
    }

    public String getOhost() {
        return ohost;
    }

    public void setOhost(String ohost) {
        this.ohost = ohost;
    }
}
