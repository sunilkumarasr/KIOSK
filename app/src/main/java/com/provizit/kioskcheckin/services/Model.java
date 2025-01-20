package com.provizit.kioskcheckin.services;

import com.provizit.kioskcheckin.utilities.CompanyData;

import java.io.Serializable;

public class Model implements Serializable {
    public Integer result;

    public CompanyData items;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public CompanyData getItems() {
        return items;
    }

    public void setItems(CompanyData items) {
        this.items = items;
    }

}


