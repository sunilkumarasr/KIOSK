package com.provizit.kioskcheckin.utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Belongings implements Serializable {
    private String label,data;
    private Boolean status;

    public JSONObject getbelongings(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("label", this.label);
            jo.put("data", this.data);
            jo.put("status", this.status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
