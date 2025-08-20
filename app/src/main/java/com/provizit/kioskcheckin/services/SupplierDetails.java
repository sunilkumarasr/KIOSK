package com.provizit.kioskcheckin.services;

public class SupplierDetails {

    public String contact_person;
    public String supplier_email;
    public SupplierMobile supplier_mobile;
    public String id_number;
    public String nationality;
    public String vehicle_no;
    public String vehicle_type;
    public boolean disabledStatus;

    public SupplierDetails(String contact_person, String supplier_email, SupplierMobile supplier_mobile, String id_number,
                           String nationality, String vehicle_no, String vehicle_type, boolean disabledStatus) {
        this.contact_person = contact_person;
        this.supplier_email = supplier_email;
        this.supplier_mobile = supplier_mobile;
        this.id_number = id_number;
        this.nationality = nationality;
        this.vehicle_no = vehicle_no;
        this.vehicle_type = vehicle_type;
        this.disabledStatus = disabledStatus;
    }

    @Override
    public String toString() {
        return "supplier_details{" +
                "contact_person='" + contact_person + '\'' +
                ", supplier_email='" + supplier_email + '\'' +
                ", supplier_mobile='" + supplier_mobile + '\'' +
                ", id_number='" + id_number + '\'' +
                ", nationality='" + nationality + '\'' +
                ", vehicle_no='" + vehicle_no + '\'' +
                ", vehicle_type='" + vehicle_type + '\'' +
                ", disabledStatus=" + disabledStatus +
                '}';
    }

}
