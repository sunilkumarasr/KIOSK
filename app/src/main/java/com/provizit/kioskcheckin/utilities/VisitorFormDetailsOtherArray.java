package com.provizit.kioskcheckin.utilities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class VisitorFormDetailsOtherArray implements Serializable {
    private String label, model, data_android_var;
    private Boolean depends, professional, disabled, active, status;
    private OidModel _id;

    public JSONObject getothers() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("label", this.label);
            jo.put("model", this.model);
            jo.put("data", this.data_android_var);
            jo.put("status", this.status);
            jo.put("depends", this.depends);
            jo.put("professional", this.professional);
            jo.put("disabled", this.disabled);
            jo.put("active", this.active);
        } catch (Exception e) {
            Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging

        }
        return jo;
    }

    public String getData() {
        return data_android_var;
    }

    public void setData(String data) {
        this.data_android_var = data;
    }

    public OidModel get_id() {
        return _id;
    }

    public void set_id(OidModel _id) {
        this._id = _id;
    }

    //    public void setData(ArrayList<VisitorFormDetailsDataArray> data) {
//        this.data = data;
//    }
//
//    private ArrayList<VisitorFormDetailsDataArray>data;
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


    public Boolean getDepends() {
        return depends;
    }

    public void setDepends(Boolean depends) {
        this.depends = depends;
    }

    public Boolean getProfessional() {
        return professional;
    }

    public void setProfessional(Boolean professional) {
        this.professional = professional;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
