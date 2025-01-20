package com.provizit.kioskcheckin.services;

import com.provizit.kioskcheckin.utilities.Getpurposes;

import java.io.Serializable;
import java.util.ArrayList;

public class GetpurposesModel implements Serializable {
    private Integer result;
    private ArrayList<Getpurposes>items;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public ArrayList<Getpurposes> getItems() {
        return items;
    }

    public void setItems(ArrayList<Getpurposes> items) {
        this.items = items;
    }
}
