package com.provizit.kioskcheckin.services;

import java.io.Serializable;

public class LocationAddressList implements Serializable {

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
