package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;

public class Request implements Serializable {
    private String $numberLong;

    public String get$numberLong() {
        return $numberLong;
    }

    public void set$numberLong(String $numberLong) {
        this.$numberLong = $numberLong;
    }
}
