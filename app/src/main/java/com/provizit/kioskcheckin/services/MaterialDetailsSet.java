package com.provizit.kioskcheckin.services;

public class MaterialDetailsSet {

    public String description;
    public int quantity;
    public String serial_num;
    public boolean status;


    public MaterialDetailsSet(String description, int quantity, String serial_num, boolean status) {
        this.description = description;
        this.quantity = quantity;
        this.serial_num = serial_num;
        this.status = status;
    }

    @Override
    public String toString() {
        return "material_details{" +
                "description" + description + '\'' +
                ", quantity='" + quantity + '\'' +
                ", serial_num='" + serial_num + '\'' +
                ", status='" + status + '\'' +
                '}';
    }


}
