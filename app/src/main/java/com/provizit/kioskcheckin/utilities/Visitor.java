package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;

public class Visitor implements Serializable {
    private String primary,cpurpose,cvisitor,workflow;
    private Boolean blocking,amobile,purpose,belongings,vreg,aemail,pic,badge,tvisitor,documents,hierarchy,employee,acceptance,pbadge,bcard,idcard,slots, ndaform,p_policy;

    public Boolean getNdaform() {
        return ndaform;
    }

    public void setNdaform(Boolean ndaform) {
        this.ndaform = ndaform;
    }

    public String getPrimary() {
        return primary;
    }

//    public void setNdaform(Boolean ndaform) {
//        this.ndaform = ndaform;
//    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getCpurpose() {
        return cpurpose;
    }

    public void setCpurpose(String cpurpose) {
        this.cpurpose = cpurpose;
    }

    public String getCvisitor() {
        return cvisitor;
    }

    public void setCvisitor(String cvisitor) {
        this.cvisitor = cvisitor;
    }

    public String getWorkflow() {
        return workflow;
    }

    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }

    public Boolean getAmobile() {
        return amobile;
    }

    public void setAmobile(Boolean amobile) {
        this.amobile = amobile;
    }

    public Boolean getPurpose() {
        return purpose;
    }

    public void setPurpose(Boolean purpose) {
        this.purpose = purpose;
    }

    public Boolean getBelongings() {
        return belongings;
    }

    public void setBelongings(Boolean belongings) {
        this.belongings = belongings;
    }

    public Boolean getVreg() {
        return vreg;
    }

    public void setVreg(Boolean vreg) {
        this.vreg = vreg;
    }

    public Boolean getAemail() {
        return aemail;
    }

    public void setAemail(Boolean aemail) {
        this.aemail = aemail;
    }

    public Boolean getPic() {
        return pic;
    }

    public void setPic(Boolean pic) {
        this.pic = pic;
    }

    public Boolean getBadge() {
        return badge;
    }

    public void setBadge(Boolean badge) {
        this.badge = badge;
    }

    public Boolean getTvisitor() {
        return tvisitor;
    }

    public void setTvisitor(Boolean tvisitor) {
        this.tvisitor = tvisitor;
    }

    public Boolean getDocuments() {
        return documents;
    }

    public void setDocuments(Boolean documents) {
        this.documents = documents;
    }

    public Boolean getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(Boolean hierarchy) {
        this.hierarchy = hierarchy;
    }

    public Boolean getEmployee() {
        return employee;
    }

    public void setEmployee(Boolean employee) {
        this.employee = employee;
    }

    public Boolean getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(Boolean acceptance) {
        this.acceptance = acceptance;
    }

    public Boolean getPbadge() {
        return pbadge;
    }

    public void setPbadge(Boolean pbadge) {
        this.pbadge = pbadge;
    }

    public Boolean getBcard() {
        return bcard;
    }

    public void setBcard(Boolean bcard) {
        this.bcard = bcard;
    }

    public Boolean getIdcard() {
        return idcard;
    }

    public void setIdcard(Boolean idcard) {
        this.idcard = idcard;
    }

    public Boolean getSlots() {
        return slots;
    }

    public void setSlots(Boolean slots) {
        this.slots = slots;
    }

    public Boolean getP_policy() {
        return p_policy;
    }

    public void setP_policy(Boolean p_policy) {
        this.p_policy = p_policy;
    }

    public Boolean getBlocking() {
        return blocking;
    }

    public void setBlocking(Boolean blocking) {
        this.blocking = blocking;
    }
}
