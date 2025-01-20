package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;

public class BillingCompanyDetails implements Serializable {

    private String mobile,mobilecode;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobilecode() {
        return mobilecode;
    }

    public void setMobilecode(String mobilecode) {
        this.mobilecode = mobilecode;
    }
}
