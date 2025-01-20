package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;

public class UserDetailsData implements Serializable {
    private String email,comp_id,supertype,type,emp_id,mobile,mobilecode;
    private OidModel _id;
    private Boolean access;
    private Float mverify,status;
    private MobileDataAddress mobiledata;


    public Boolean getAccess() {
        return access;
    }

    public void setAccess(Boolean access) {
        this.access = access;
    }

    public String getSupertype() {
        return supertype;
    }

    public void setSupertype(String supertype) {
        this.supertype = supertype;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobilecode() {
        return mobilecode;
    }

    public void setMobilecode(String mobilecode) {
        this.mobilecode = mobilecode;
    }

    public OidModel get_id() {
        return _id;
    }

    public void set_id(OidModel _id) {
        this._id = _id;
    }

    public Float getMverify() {
        return mverify;
    }

    public void setMverify(Float mverify) {
        this.mverify = mverify;
    }

    public Float getStatus() {
        return status;
    }

    public void setStatus(Float status) {
        this.status = status;
    }

    public MobileDataAddress getMobiledata() {
        return mobiledata;
    }

    public void setMobiledata(MobileDataAddress mobiledata) {
        this.mobiledata = mobiledata;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
