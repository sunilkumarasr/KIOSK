package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;
import java.util.ArrayList;

public class Purpose implements Serializable {

    private String supertype,comp_id,name,count,duration;
    private Boolean active;
    private float status;
    private CreatedTime created_time;
    private ArrayList<String> employee;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public float getStatus() {
        return status;
    }

    public void setStatus(float status) {
        this.status = status;
    }

    public CreatedTime getCreated_time() {
        return created_time;
    }

    public void setCreated_time(CreatedTime created_time) {
        this.created_time = created_time;
    }

    public ArrayList<String> getEmployee() {
        return employee;
    }

    public void setEmployee(ArrayList<String> employee) {
        this.employee = employee;
    }
}
