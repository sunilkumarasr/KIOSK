package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;

public class Locations implements Serializable {
    private String phone,phonecode,name,timezone,providerid,senderid;
    private Boolean status;
    private MobileDataAddress mobiledata;
    private Float pslots;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(String phonecode) {
        this.phonecode = phonecode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getProviderid() {
        return providerid;
    }

    public void setProviderid(String providerid) {
        this.providerid = providerid;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public MobileDataAddress getMobiledata() {
        return mobiledata;
    }

    public void setMobiledata(MobileDataAddress mobiledata) {
        this.mobiledata = mobiledata;
    }

    public Float getPslots() {
        return pslots;
    }

    public void setPslots(Float pslots) {
        this.pslots = pslots;
    }
}
