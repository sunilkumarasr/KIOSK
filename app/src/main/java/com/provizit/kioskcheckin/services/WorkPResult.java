package com.provizit.kioskcheckin.services;

import com.provizit.kioskcheckin.utilities.OidModel;

import java.io.Serializable;

public class WorkPResult implements Serializable {

    private OidModel _id;


    public OidModel get_id() {
        return _id;
    }

    public void set_id(OidModel _id) {
        this._id = _id;
    }
}
