package com.provizit.kioskcheckin.utilities.EntryPermit;

import com.provizit.kioskcheckin.utilities.WorkPermit.WorkPermitItems;

import java.io.Serializable;

public class EntryPermitModel implements Serializable {

    private Integer result;
    private EntryPermitItems items;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public EntryPermitItems getItems() {
        return items;
    }

    public void setItems(EntryPermitItems items) {
        this.items = items;
    }

}