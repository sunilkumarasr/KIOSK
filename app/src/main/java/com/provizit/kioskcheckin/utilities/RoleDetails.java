package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;

public class RoleDetails implements Serializable {
    public float getLevel() {
        return level;
    }

    public void setLevel(float level) {
        this.level = level;
    }


    private OidModel _id;
    private CreatedTime created_time;
    private String  name,desc;
    private boolean checkin,location,hierarchy,meeting,reports,smeeting,visitors,dvisitors,operations,bydefault,rmeeting,uvisitors,behalfof,aworkflow,security;
    private float status,level;



    public OidModel get_id() {
        return _id;
    }

    public void set_id(OidModel _id) {
        this._id = _id;
    }

    public CreatedTime getCreated_time() {
        return created_time;
    }

    public void setCreated_time(CreatedTime created_time) {
        this.created_time = created_time;
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

    public boolean isCheckin() {
        return checkin;
    }

    public void setCheckin(boolean checkin) {
        this.checkin = checkin;
    }

    public boolean isLocation() {
        return location;
    }

    public void setLocation(boolean location) {
        this.location = location;
    }

    public boolean isHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(boolean hierarchy) {
        this.hierarchy = hierarchy;
    }

    public boolean isMeeting() {
        return meeting;
    }

    public void setMeeting(boolean meeting) {
        this.meeting = meeting;
    }

    public boolean isReports() {
        return reports;
    }

    public void setReports(boolean reports) {
        this.reports = reports;
    }

    public boolean isSmeeting() {
        return smeeting;
    }

    public void setSmeeting(boolean smeeting) {
        this.smeeting = smeeting;
    }

    public boolean isVisitors() {
        return visitors;
    }

    public void setVisitors(boolean visitors) {
        this.visitors = visitors;
    }

    public boolean isDvisitors() {
        return dvisitors;
    }

    public void setDvisitors(boolean dvisitors) {
        this.dvisitors = dvisitors;
    }

    public boolean isOperations() {
        return operations;
    }

    public void setOperations(boolean operations) {
        this.operations = operations;
    }

    public boolean isBydefault() {
        return bydefault;
    }

    public void setBydefault(boolean bydefault) {
        this.bydefault = bydefault;
    }

    public boolean isRmeeting() {
        return rmeeting;
    }

    public void setRmeeting(boolean rmeeting) {
        this.rmeeting = rmeeting;
    }

    public boolean isUvisitors() {
        return uvisitors;
    }

    public void setUvisitors(boolean uvisitors) {
        this.uvisitors = uvisitors;
    }

    public boolean isBehalfof() {
        return behalfof;
    }

    public void setBehalfof(boolean behalfof) {
        this.behalfof = behalfof;
    }

    public boolean isAworkflow() {
        return aworkflow;
    }

    public void setAworkflow(boolean aworkflow) {
        this.aworkflow = aworkflow;
    }

    public boolean isSecurity() {
        return security;
    }

    public void setSecurity(boolean security) {
        this.security = security;
    }

    public float getStatus() {
        return status;
    }

    public void setStatus(float status) {
        this.status = status;
    }
}
