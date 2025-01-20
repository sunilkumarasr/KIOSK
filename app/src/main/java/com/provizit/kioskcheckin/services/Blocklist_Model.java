package com.provizit.kioskcheckin.services;

import com.provizit.kioskcheckin.utilities.C_blacklist_model;
import com.provizit.kioskcheckin.utilities.OidModel;

import java.io.Serializable;

public class Blocklist_Model implements Serializable {

    private Integer result;
    private OidModel _id;

    private C_blacklist_model item;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public OidModel get_id() {
        return _id;
    }

    public void set_id(OidModel _id) {
        this._id = _id;
    }

    public C_blacklist_model getItem() {
        return item;
    }

    public void setItem(C_blacklist_model item) {
        this.item = item;
    }
}
