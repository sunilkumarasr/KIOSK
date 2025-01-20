package com.provizit.kioskcheckin.mvvm.RequestModels;

public class Login_postmethodModelRequest {

    private String id;
    private Integer mverify;
    private String type;
    private String val;
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMverify() {
        return mverify;
    }

    public void setMverify(Integer mverify) {
        this.mverify = mverify;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
