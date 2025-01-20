package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;

public class PlanDetails implements Serializable {
    private String name,desc;
    private Float start,end,employee,meeting,status;
    private OidModel _id;
    private CreatedTime created_time;

    public CreatedTime getCreated_time() {
        return created_time;
    }

    public void setCreated_time(CreatedTime created_time) {
        this.created_time = created_time;
    }

    public OidModel get_id() {
        return _id;
    }

    public void set_id(OidModel _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Float getStart() {
        return start;
    }

    public void setStart(Float start) {
        this.start = start;
    }

    public Float getEnd() {
        return end;
    }

    public void setEnd(Float end) {
        this.end = end;
    }

    public Float getEmployee() {
        return employee;
    }

    public void setEmployee(Float employee) {
        this.employee = employee;
    }

    public Float getMeeting() {
        return meeting;
    }

    public void setMeeting(Float meeting) {
        this.meeting = meeting;
    }

    public Float getStatus() {
        return status;
    }

    public void setStatus(Float status) {
        this.status = status;
    }
}
