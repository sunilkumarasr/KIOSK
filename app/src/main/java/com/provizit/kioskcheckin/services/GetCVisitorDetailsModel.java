package com.provizit.kioskcheckin.services;



import com.provizit.kioskcheckin.utilities.IncompleteData;
import com.provizit.kioskcheckin.utilities.Items;
import com.provizit.kioskcheckin.utilities.TotalCounts;

import java.io.Serializable;

public class GetCVisitorDetailsModel implements Serializable {
    private Integer result;
    private TotalCounts total_counts;
    private IncompleteData incomplete_data;
    private Items items;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public TotalCounts getTotal_counts() {
        return total_counts;
    }

    public void setTotal_counts(TotalCounts total_counts) {
        this.total_counts = total_counts;
    }

    public IncompleteData getIncomplete_data() {
        return incomplete_data;
    }

    public void setIncomplete_data(IncompleteData incomplete_data) {
        this.incomplete_data = incomplete_data;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }
}
