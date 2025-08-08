package com.provizit.kioskcheckin.services;

public class Contractor {

    public String name;
    public String company;
    public String companyName;
    public String id_type;
    public String id_name;
    public String nationality;
    public String id_number;
    public String email;
    public String mobile;
    public MobileData mobileData;
    public String performing_work;
    public boolean common_nation;
    public boolean disabledStatus;

    public Contractor(String name, String company, String companyName, String id_type, String id_name,
                         String nationality, String id_number, String email, String mobile,
                      MobileData mobileData, String performing_work, boolean common_nation,
                         boolean disabledStatus) {
        this.name = name;
        this.company = company;
        this.companyName = companyName;
        this.id_type = id_type;
        this.id_name = id_name;
        this.nationality = nationality;
        this.id_number = id_number;
        this.email = email;
        this.mobile = mobile;
        this.mobileData = mobileData;
        this.performing_work = performing_work;
        this.common_nation = common_nation;
        this.disabledStatus = disabledStatus;
    }


    @Override
    public String toString() {
        return "Contractor{" +
                "name='" + name + '\'' +
                ", company='" + company + '\'' +
                ", companyName='" + companyName + '\'' +
                ", id_type='" + id_type + '\'' +
                ", id_name='" + id_name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", id_number='" + id_number + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", mobileData=" + (mobileData != null ? mobileData.toString() : "null") +
                ", performing_work='" + performing_work + '\'' +
                ", common_nation=" + common_nation +
                ", disabledStatus=" + disabledStatus +
                '}';
    }
}
