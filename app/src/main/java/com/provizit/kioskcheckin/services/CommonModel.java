package com.provizit.kioskcheckin.services;

import com.provizit.kioskcheckin.utilities.Items;

import java.io.Serializable;

public class CommonModel implements Serializable {
private Integer result;
private Float total_counts;
private String incomplete_data;
private Items items;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Float getTotal_counts() {
        return total_counts;
    }

    public void setTotal_counts(Float total_counts) {
        this.total_counts = total_counts;
    }

    public String getIncomplete_data() {
        return incomplete_data;
    }

    public void setIncomplete_data(String incomplete_data) {
        this.incomplete_data = incomplete_data;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }
}
