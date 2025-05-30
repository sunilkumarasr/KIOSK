package com.provizit.kioskcheckin.services;

import com.provizit.kioskcheckin.utilities.MobileDataAddress;
import com.provizit.kioskcheckin.utilities.OidModel;

import java.io.Serializable;
import java.util.ArrayList;

public class CommoncheckModel implements Serializable {

    private Integer result;
    private String name,comp_id,code,fname,lname,mname,email,mobile,mobilecode,location,hierarchy_indexid,hierarchyname,hierarchy_id,roleid,rolename,designation,branch,department,supertype;
    private Boolean approver,badgeno,vtypes,coordinator,departments,employees,confirmation,access,o_status;
    private Float status;
    private OidModel _id;
    private MobileDataAddress mobiledata;
    private ArrayList<String> pic;
    private ArrayList<String>pics;

    public String getHierarchyname() {
        return hierarchyname;
    }

    public void setHierarchyname(String hierarchyname) {
        this.hierarchyname = hierarchyname;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobilecode() {
        return mobilecode;
    }

    public void setMobilecode(String mobilecode) {
        this.mobilecode = mobilecode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHierarchy_indexid() {
        return hierarchy_indexid;
    }

    public void setHierarchy_indexid(String hierarchy_indexid) {
        this.hierarchy_indexid = hierarchy_indexid;
    }

    public String getHierarchy_id() {
        return hierarchy_id;
    }

    public void setHierarchy_id(String hierarchy_id) {
        this.hierarchy_id = hierarchy_id;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSupertype() {
        return supertype;
    }

    public void setSupertype(String supertype) {
        this.supertype = supertype;
    }

    public Boolean getApprover() {
        return approver;
    }

    public void setApprover(Boolean approver) {
        this.approver = approver;
    }

    public Boolean getBadgeno() {
        return badgeno;
    }

    public void setBadgeno(Boolean badgeno) {
        this.badgeno = badgeno;
    }

    public Boolean getVtypes() {
        return vtypes;
    }

    public void setVtypes(Boolean vtypes) {
        this.vtypes = vtypes;
    }

    public Boolean getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(Boolean coordinator) {
        this.coordinator = coordinator;
    }

    public Boolean getDepartments() {
        return departments;
    }

    public void setDepartments(Boolean departments) {
        this.departments = departments;
    }

    public Boolean getEmployees() {
        return employees;
    }

    public void setEmployees(Boolean employees) {
        this.employees = employees;
    }

    public Boolean getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Boolean confirmation) {
        this.confirmation = confirmation;
    }

    public Boolean getAccess() {
        return access;
    }

    public void setAccess(Boolean access) {
        this.access = access;
    }

    public Boolean getO_status() {
        return o_status;
    }

    public void setO_status(Boolean o_status) {
        this.o_status = o_status;
    }

    public Float getStatus() {
        return status;
    }

    public void setStatus(Float status) {
        this.status = status;
    }

    public OidModel get_id() {
        return _id;
    }

    public void set_id(OidModel _id) {
        this._id = _id;
    }

    public MobileDataAddress getMobiledata() {
        return mobiledata;
    }

    public void setMobiledata(MobileDataAddress mobiledata) {
        this.mobiledata = mobiledata;
    }

    public ArrayList<String> getPic() {
        return pic;
    }

    public void setPic(ArrayList<String> pic) {
        this.pic = pic;
    }

    public ArrayList<String> getPics() {
        return pics;
    }

    public void setPics(ArrayList<String> pics) {
        this.pics = pics;
    }

    public ArrayList<String> getEmployee() {
        return employee;
    }

    public void setEmployee(ArrayList<String> employee) {
        this.employee = employee;
    }

    private ArrayList<String>employee;
}
