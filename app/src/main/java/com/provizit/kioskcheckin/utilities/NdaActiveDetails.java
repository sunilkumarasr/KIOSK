package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;

public class NdaActiveDetails implements Serializable {
    private String comp_id,name,content,supertype;
    private Boolean active;
    private OidModel _id;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
