package com.provizit.kioskcheckin.services;

import com.provizit.kioskcheckin.utilities.Getsubhierarchys;

import java.io.Serializable;
import java.util.ArrayList;

public class GetsubhierarchysModel implements Serializable {
    private Integer result;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
    private ArrayList<Getsubhierarchys>items;

    public ArrayList<Getsubhierarchys> getItems() {
        return items;
    }

    public void setItems(ArrayList<Getsubhierarchys> items) {
        this.items = items;
    }
}

