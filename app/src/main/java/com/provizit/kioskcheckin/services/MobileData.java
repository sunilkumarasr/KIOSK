package com.provizit.kioskcheckin.services;

public class MobileData {
    public String number;
    public String internationalNumber;
    public String nationalNumber;

    public MobileData(String number, String internationalNumber, String nationalNumber) {
        this.number = number;
        this.internationalNumber = internationalNumber;
        this.nationalNumber = nationalNumber;
    }

    public MobileData() {
        // Empty constructor to send {}
    }
}


