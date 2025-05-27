package com.provizit.kioskcheckin.utilities.WorkPermit;

import com.provizit.kioskcheckin.utilities.OidModel;

import java.io.Serializable;

public class ContractorsData implements Serializable {

    private String name,email, mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private long checkin,checkout;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public long getCheckin() {
        return checkin;
    }

    public void setCheckin(long checkin) {
        this.checkin = checkin;
    }

    public long getCheckout() {
        return checkout;
    }

    public void setCheckout(long checkout) {
        this.checkout = checkout;
    }
}
