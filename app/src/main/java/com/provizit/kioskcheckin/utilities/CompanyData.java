package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;


public class CompanyData implements Serializable {
    private RoleDetails roleDetails;
    private String email, password, comp_id, senderID, subdomain, supertype, type, role, role_id, emp_id, mobile, mobilecode, random, link;
    private OidModel _id;
    private Boolean approver, badgeno, vtypes, coordinator, departments, employees, confirmation, access;
    private Float verify, mverify, status;
    private CreatedTime created_time;
    private MobileDataAddress mobiledata;
    private EmpData empData;

    // Empty constructor - no initial setup required
    public CompanyData() {
        // The constructor is left empty intentionally as the object fields can be set using setters.
        // You can add custom initialization logic if necessary.
    }

    // Getters and Setters

    public Boolean getAccess() {
        return access;
    }

    public void setAccess(Boolean access) {
        // Optional: Add validation if access must not be null
        if (access == null) {
            throw new IllegalArgumentException("Access cannot be null");
        }
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

    public EmpData getEmpData() {
        return empData;
    }


    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getSenderID() {
        return senderID;
    }


    public String getSubdomain() {
        return subdomain;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public RoleDetails getRoleDetails() {
        return roleDetails;
    }


}