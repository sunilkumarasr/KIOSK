package com.provizit.kioskcheckin.utilities.WorkPermit;

import java.io.Serializable;

public class WorkPermitModel implements Serializable {

    private Integer result;
    private WorkPermitItems items;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public WorkPermitItems getItems() {
        return items;
    }

    public void setItems(WorkPermitItems items) {
        this.items = items;
    }
}
