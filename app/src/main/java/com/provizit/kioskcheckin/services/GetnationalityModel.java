package com.provizit.kioskcheckin.services;

import com.provizit.kioskcheckin.utilities.Getnationality;

import java.io.Serializable;
import java.util.ArrayList;

public class GetnationalityModel implements Serializable {
    private Integer result;


    private ArrayList<Getnationality> items;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }


    public ArrayList<Getnationality> getItems() {
        return items;
    }

    public void setItems(ArrayList<Getnationality> items) {
        this.items = items;
    }
}
