package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;
import java.util.ArrayList;

public class Getdocuments implements Serializable {
    private OidModel _id;
    private String comp_id, name, supertype;
    private Boolean active, doc_type, common;

    private ArrayList<Getnationality> nationlities;

    public ArrayList<Getnationality> getNationlities() {
        return nationlities;
    }

    public void setNationlities(ArrayList<Getnationality> nationlities) {
        this.nationlities = nationlities;
    }

    public OidModel get_id() {
        return _id;
    }

    public void set_id(OidModel _id) {
        this._id = _id;
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

    public String getSupertype() {
        return supertype;
    }

    public void setSupertype(String supertype) {
        this.supertype = supertype;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(Boolean doc_type) {
        this.doc_type = doc_type;
    }

    public Boolean getCommon() {
        return common;
    }

    public void setCommon(Boolean common) {
        this.common = common;
    }

}
