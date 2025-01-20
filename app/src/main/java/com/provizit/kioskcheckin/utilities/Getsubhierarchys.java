package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;
import java.util.ArrayList;

public class Getsubhierarchys implements Serializable {
    private OidModel _id;
    private String supertype,comp_id,id,name,type,hierarchy;
    private Float level;
    private Float nslots;
    private Float status;

    private Float coordinator;
    private ArrayList<Purposes>purposes;
    private ArrayList<Visitors>visitors;

    public String getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(String hierarchy) {
        this.hierarchy = hierarchy;
    }

    public OidModel get_id() {
        return _id;
    }

    public void set_id(OidModel _id) {
        this._id = _id;
    }

    public ArrayList<Purposes> getPurposes() {
        return purposes;
    }

    public void setPurposes(ArrayList<Purposes> purposes) {
        this.purposes = purposes;
    }

    public ArrayList<Visitors> getVisitors() {
        return visitors;
    }

    public void setVisitors(ArrayList<Visitors> visitors) {
        this.visitors = visitors;
    }

    public CreatedTime getCreated_time() {
        return created_time;
    }

    public void setCreated_time(CreatedTime created_time) {
        this.created_time = created_time;
    }

    private CreatedTime created_time;

    public Float getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(Float coordinator) {
        this.coordinator = coordinator;
    }



    public String getSupertype() {
        return supertype;
    }

    public void setSupertype(String supertype) {
        this.supertype = supertype;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getLevel() {
        return level;
    }

    public void setLevel(Float level) {
        this.level = level;
    }

    public Float getNslots() {
        return nslots;
    }

    public void setNslots(Float nslots) {
        this.nslots = nslots;
    }

    public Float getStatus() {
        return status;
    }

    public void setStatus(Float status) {
        this.status = status;
    }
}
