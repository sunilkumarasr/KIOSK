package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;
import java.util.ArrayList;


public class GetSearchEmployees implements Serializable {
    private OidModel _id;
    private MobileDataAddress mobiledata;
    private Outlook outlook;
    private RoleDetails roleDetails;
    private ArrayList<AddressCompanyDetails> address;
    private String comp_id,code,name,fname,lname,mname,email,mobile,mobilecode,location,hierarchy_indexid,hierarchy_id,roleid,rolename,designation,branch,department,supertype,language,dob,hierarchyname,hierarchy;
    private Boolean approver,badgeno,vtypes,coordinator,departments,employees,confirmation,access,o_status;
    private Float user_status;
    private ArrayList<String>pic;
    private ArrayList<String>pics;


    public ArrayList<String> getPic() {
        return pic;
    }

    public void setPic(ArrayList<String> pic) {
        this.pic = pic;
    }

    public RoleDetails getRoleDetails() {
        return roleDetails;
    }

    public void setRoleDetails(RoleDetails roleDetails) {
        this.roleDetails = roleDetails;
    }

    public ArrayList<AddressCompanyDetails> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<AddressCompanyDetails> address) {
        this.address = address;
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

    public Outlook getOutlook() {
        return outlook;
    }

    public void setOutlook(Outlook outlook) {
        this.outlook = outlook;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getHierarchyname() {
        return hierarchyname;
    }

    public void setHierarchyname(String hierarchyname) {
        this.hierarchyname = hierarchyname;
    }

    public String getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(String hierarchy) {
        this.hierarchy = hierarchy;
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

    public Float getUser_status() {
        return user_status;
    }

    public void setUser_status(Float user_status) {
        this.user_status = user_status;
    }




}
