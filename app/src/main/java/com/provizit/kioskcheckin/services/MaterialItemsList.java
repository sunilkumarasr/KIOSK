package com.provizit.kioskcheckin.services;

import com.google.gson.annotations.SerializedName;
import com.provizit.kioskcheckin.utilities.WorkPermit.CommonObject;

import java.io.Serializable;

public class MaterialItemsList implements Serializable {

    private CommonObject _id;
    private String comp_id;
    private String location;
    private String name;
    private boolean active;
    private String supertype;

    @SerializedName("return")
    private boolean returnType;

    public CommonObject get_id() {
        return _id;
    }

    public void set_id(CommonObject _id) {
        this._id = _id;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSupertype() {
        return supertype;
    }

    public void setSupertype(String supertype) {
        this.supertype = supertype;
    }

    public boolean getReturnType() {
        return returnType;
    }

    public void setReturnType(boolean returnType) {
        this.returnType = returnType;
    }
}
