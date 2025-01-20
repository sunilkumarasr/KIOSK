package com.provizit.kioskcheckin.utilities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MobileDataAddress implements Serializable {
    private String number,internationalNumber,nationalNumber,e164Number,dialCode, countryCode;

    public JSONObject getMobiledata(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("number", this.number);
            jo.put("internationalNumber", this.internationalNumber);
            jo.put("nationalNumber", this.nationalNumber);
            jo.put("e164Number", this.e164Number);
            jo.put("dialCode", this.dialCode);
            jo.put("countryCode", this.countryCode);
        }catch (Exception e) {
            Log.e(TAG, "Error processing vehicles: ", e);  // Proper logging

        }
        return jo;
    }
    public String getInternationalNumber() {
        return internationalNumber;
    }

    public void setInternationalNumber(String internationalNumber) {
        this.internationalNumber = internationalNumber;
    }

    public String getNationalNumber() {
        return nationalNumber;
    }

    public void setNationalNumber(String nationalNumber) {
        this.nationalNumber = nationalNumber;
    }

    public String getE164Number() {
        return e164Number;
    }

    public void setE164Number(String e164Number) {
        this.e164Number = e164Number;
    }

    public String getDialCode() {
        return dialCode;
    }

    public void setDialCode(String dialCode) {
        this.dialCode = dialCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
