package com.provizit.kioskcheckin.utilities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import com.provizit.kioskcheckin.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Vehicles implements Serializable {
    private String label,model,data;
    private Boolean status;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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


    public JSONObject getvehicles(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("label", this.label);
            jo.put("model", this.model);
            jo.put("data", this.data);
            jo.put("status", this.status);
        } catch (JSONException e) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "Error creating JSON for sign out", e);
            }
        }
        return jo;
    }
}
