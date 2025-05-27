package com.provizit.kioskcheckin.services;



import com.provizit.kioskcheckin.utilities.IncompleteData;
import com.provizit.kioskcheckin.utilities.Items;
import com.provizit.kioskcheckin.utilities.TotalCounts;

import java.io.Serializable;
import java.util.ArrayList;

public class GetCVisitorDetailsModel implements Serializable {
    private Integer result;
    private Integer result_type;
    private ArrayList<WorkPResult> result_data;
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

    public Integer getResult_type() {
        return result_type;
    }

    public void setResult_type(Integer result_type) {
        this.result_type = result_type;
    }

    public ArrayList<WorkPResult> getResult_data() {
        return result_data;
    }

    public void setResult_data(ArrayList<WorkPResult> result_data) {
        this.result_data = result_data;
    }
}
