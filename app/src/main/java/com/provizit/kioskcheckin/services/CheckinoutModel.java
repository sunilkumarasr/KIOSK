package com.provizit.kioskcheckin.services;

import java.io.Serializable;

public class CheckinoutModel implements Serializable {
  Integer result;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
