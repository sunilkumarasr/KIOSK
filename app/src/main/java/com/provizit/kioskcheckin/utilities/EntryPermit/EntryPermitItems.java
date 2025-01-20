package com.provizit.kioskcheckin.utilities.EntryPermit;

import com.provizit.kioskcheckin.utilities.WorkPermit.CommonObject;
import com.provizit.kioskcheckin.utilities.WorkPermit.ContractorsData;

import java.io.Serializable;
import java.util.List;

public class EntryPermitItems implements Serializable {

    private CommonObject _id;
    private boolean purpose_return;
    private String supplier_name;
    private String type;
    private List<SupplierDetails> supplier_details;
    private List<MaterialDetails> material_details;
    private long checkin,checkout;

    public CommonObject get_id() {
        return _id;
    }

    public void set_id(CommonObject _id) {
        this._id = _id;
    }

    public boolean getPurpose_return() {
        return purpose_return;
    }

    public void setPurpose_return(boolean purpose_return) {
        this.purpose_return = purpose_return;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SupplierDetails> getSupplier_details() {
        return supplier_details;
    }

    public void setSupplier_details(List<SupplierDetails> supplier_details) {
        this.supplier_details = supplier_details;
    }

    public List<MaterialDetails> getMaterial_details() {
        return material_details;
    }

    public void setMaterial_details(List<MaterialDetails> material_details) {
        this.material_details = material_details;
    }


    public long getCheckin() {
        return checkin;
    }

    public void setCheckin(long checkin) {
        this.checkin = checkin;
    }

    public long getCheckout() {
        return checkout;
    }

    public void setCheckout(long checkout) {
        this.checkout = checkout;
    }
}
