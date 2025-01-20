package com.provizit.kioskcheckin.services;

import com.provizit.kioskcheckin.utilities.VisitorformDetails;

import java.io.Serializable;

public class VisitorformDetailsModel implements Serializable {
    private Integer result;
    private VisitorformDetails item;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public VisitorformDetails getItem() {
        return item;
    }

    public void setItem(VisitorformDetails item) {
        this.item = item;
    }
}
