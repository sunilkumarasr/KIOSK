package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;
import java.util.ArrayList;

public class VisitorformDetails implements Serializable {
    private OidModel _id;
    private String supertype;
    private String type;
    private String comp_id;
    private String name_format;
    private Boolean email,mobile,name,ids,rids,belongs,rbelongs,vehicles,rvehicles,covisitors,rcovisitors,dob_active,dob_require,dob_calendar;
    private Float nids,incVal;

    private ArrayList<VisitorFormDetailsOtherArray>other;

    public ArrayList<VisitorFormDetailsOtherArray> getOther() {
        return other;
    }

    public void setOther(ArrayList<VisitorFormDetailsOtherArray> other) {
        this.other = other;
    }

    public OidModel get_id() {
        return _id;
    }

    public void set_id(OidModel _id) {
        this._id = _id;
    }
//
//    private ArrayList<VisitorFormDetailsOtherArray>other;
//
//    public ArrayList<VisitorFormDetailsOtherArray> getOther() {
//        return other;
//    }
//
//    public void setOther(ArrayList<VisitorFormDetailsOtherArray> other) {
//        this.other = other;
//    }

    public String getName_format() {
        return name_format;
    }
    public void setName_format(String name_format) {
        this.name_format = name_format;    }



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

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public Boolean getMobile() {
        return mobile;
    }

    public void setMobile(Boolean mobile) {
        this.mobile = mobile;
    }

    public Boolean getName() {
        return name;
    }

    public void setName(Boolean name) {
        this.name = name;
    }

    public Boolean getIds() {
        return ids;
    }

    public void setIds(Boolean ids) {
        this.ids = ids;
    }

    public Boolean getRids() {
        return rids;
    }

    public void setRids(Boolean rids) {
        this.rids = rids;
    }

    public Boolean getBelongs() {
        return belongs;
    }

    public void setBelongs(Boolean belongs) {
        this.belongs = belongs;
    }

    public Boolean getRbelongs() {
        return rbelongs;
    }

    public void setRbelongs(Boolean rbelongs) {
        this.rbelongs = rbelongs;
    }

    public Boolean getVehicles() {
        return vehicles;
    }

    public void setVehicles(Boolean vehicles) {
        this.vehicles = vehicles;
    }

    public Boolean getRvehicles() {
        return rvehicles;
    }

    public void setRvehicles(Boolean rvehicles) {
        this.rvehicles = rvehicles;
    }

    public Boolean getCovisitors() {
        return covisitors;
    }

    public void setCovisitors(Boolean covisitors) {
        this.covisitors = covisitors;
    }

    public Boolean getRcovisitors() {
        return rcovisitors;
    }

    public void setRcovisitors(Boolean rcovisitors) {
        this.rcovisitors = rcovisitors;
    }

    public Boolean getDob_active() {
        return dob_active;
    }

    public void setDob_active(Boolean dob_active) {
        this.dob_active = dob_active;
    }

    public Boolean getDob_require() {
        return dob_require;
    }

    public void setDob_require(Boolean dob_require) {
        this.dob_require = dob_require;
    }

    public Boolean getDob_calendar() {
        return dob_calendar;
    }

    public void setDob_calendar(Boolean dob_calendar) {
        this.dob_calendar = dob_calendar;
    }

    public Float getNids() {
        return nids;
    }

    public void setNids(Float nids) {
        this.nids = nids;
    }

    public Float getIncVal() {
        return incVal;
    }

    public void setIncVal(Float incVal) {
        this.incVal = incVal;
    }

}
