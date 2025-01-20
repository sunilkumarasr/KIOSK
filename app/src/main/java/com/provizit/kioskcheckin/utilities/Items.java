package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;

public class Items implements Serializable {
    private String subject;
    private String note_val;
    private Float meetingStatus,visitorStatus,checkINStatus;
    private Long start,end;
    private Employee employee;
    private Meetingrooms meetingrooms;
    private OidModel _id;
    private UserDetails userDetails;
    private Boolean trd_access;

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
    private Purpose purpose;

    public String getNote_val() {
        return note_val;
    }

    public void setNote_val(String note_val) {
        this.note_val = note_val;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }


    public Purposes getPurposes() {
        return purposes;
    }

    public void setPurposes(Purposes purposes) {
        this.purposes = purposes;
    }

    private Purposes purposes;

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Float getMeetingStatus() {
        return meetingStatus;
    }

    public void setMeetingStatus(Float meetingStatus) {
        this.meetingStatus = meetingStatus;
    }

    public Float getVisitorStatus() {
        return visitorStatus;
    }

    public void setVisitorStatus(Float visitorStatus) {
        this.visitorStatus = visitorStatus;
    }

    public Float getCheckINStatus() {
        return checkINStatus;
    }

    public void setCheckINStatus(Float checkINStatus) {
        this.checkINStatus = checkINStatus;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Meetingrooms getMeetingrooms() {
        return meetingrooms;
    }

    public void setMeetingrooms(Meetingrooms meetingrooms) {
        this.meetingrooms = meetingrooms;
    }

    public OidModel get_id() {
        return _id;
    }

    public void set_id(OidModel _id) {
        this._id = _id;
    }

    public Boolean getTrd_access() {
        return trd_access;
    }

    public void setTrd_access(Boolean trd_access) {
        this.trd_access = trd_access;
    }
}
