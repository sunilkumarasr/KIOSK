package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;

public class Getnationality implements Serializable {

    private OidModel _id;
    private String comp_id, name, doc_id, supertype;
    private Boolean active, common;


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

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
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

    public Boolean getCommon() {
        return common;
    }

    public void setCommon(Boolean common) {
        this.common = common;
    }
}

