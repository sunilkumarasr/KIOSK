package com.provizit.kioskcheckin.services;

import com.provizit.kioskcheckin.utilities.GetSearchEmployees;

import java.io.Serializable;
import java.util.ArrayList;

public class GetSearchEmployeesModel implements Serializable {
    private Integer result;

    private ArrayList<GetSearchEmployees> items;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public ArrayList<GetSearchEmployees> getItems() {
        return items;
    }

    public void setItems(ArrayList<GetSearchEmployees> items) {
        this.items = items;
    }
}
