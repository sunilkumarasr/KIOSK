package com.provizit.kioskcheckin.services;

import com.provizit.kioskcheckin.utilities.Items;

import java.io.Serializable;

public class QrCodeStatusModel implements Serializable {

    private Integer result;

    private QrCodeStatusItems items;


    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }


    public QrCodeStatusItems getItems() {
        return items;
    }

    public void setItems(QrCodeStatusItems items) {
        this.items = items;
    }
}
