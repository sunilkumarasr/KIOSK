package com.provizit.kioskcheckin.utilities;

import com.provizit.kioskcheckin.services.LocationAddressList;

import java.io.Serializable;
import java.util.ArrayList;

public class CompanyDetails implements Serializable {

    private String supertype, comp_id, name,email, mobile, mobilecode;
    private Float start, end;
    private BillingCompanyDetails other;
    private Visitor visitor;
    private ArrayList<String> pic;
    private ArrayList<String> a_pic;
    private ArrayList<LocationAddressList> address;
    public ArrayList<String> getA_pic() {
        return a_pic;
    }

    public ArrayList<String> getPic() {
        return pic;
    }

    public void setPic(ArrayList<String> pic) {
        this.pic = pic;
    }

    public String getSupertype() {
        return supertype;
    }

    public void setSupertype(String supertype) {
        this.supertype = supertype;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

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

    public String getMobilecode() {
        return mobilecode;
    }

    public void setMobilecode(String mobilecode) {
        this.mobilecode = mobilecode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Float getStart() {
        return start;
    }

    public void setStart(Float start) {
        this.start = start;
    }

    public Float getEnd() {
        return end;
    }

    public void setEnd(Float end) {
        this.end = end;
    }

    public BillingCompanyDetails getOther() {
        return other;
    }

    public void setOther(BillingCompanyDetails other) {
        this.other = other;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public ArrayList<LocationAddressList> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<LocationAddressList> address) {
        this.address = address;
    }
}


