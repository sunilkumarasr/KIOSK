package com.provizit.kioskcheckin.services;

import com.provizit.kioskcheckin.utilities.Getdocuments;

import java.io.Serializable;
import java.util.ArrayList;

public class GetdocumentsModel implements Serializable {
    private Integer result;
    private ArrayList<Getdocuments> items;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }


    public ArrayList<Getdocuments> getItems() {
        return items;
    }

    public void setItems(ArrayList<Getdocuments> items) {
        this.items = items;
    }
}
