package com.provizit.kioskcheckin.services;

public class SupplierMobile {

    public String number;
    public String internationalNumber;
    public String nationalNumber;
    public String e164Number;
    public String countryCode;
    public String dialCode;

    public SupplierMobile(String number, String internationalNumber, String nationalNumber, String e164Number, String countryCode, String dialCode) {
        this.number = number;
        this.internationalNumber = internationalNumber;
        this.nationalNumber = nationalNumber;
        this.e164Number = e164Number;
        this.countryCode = countryCode;
        this.dialCode = dialCode;
    }

    @Override
    public String toString() {
        return "supplier_mobile{" +
                "number='" + number + '\'' +
                ", internationalNumber='" + internationalNumber + '\'' +
                ", nationalNumber='" + nationalNumber + '\'' +
                ", e164Number='" + e164Number + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", dialCode='" + dialCode + '\'' +
                '}';
    }

}
