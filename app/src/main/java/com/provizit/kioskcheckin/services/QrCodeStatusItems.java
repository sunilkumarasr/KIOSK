package com.provizit.kioskcheckin.services;

import com.provizit.kioskcheckin.utilities.WorkPermit.CommonObject;

import java.io.Serializable;

public class QrCodeStatusItems implements Serializable {

    private long checkin,checkout;

    private CommonObject _id;

    private String emp_id;


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


    public CommonObject get_id() {
        return _id;
    }

    public void set_id(CommonObject _id) {
        this._id = _id;
    }


    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }
}
