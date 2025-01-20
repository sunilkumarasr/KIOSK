package com.provizit.kioskcheckin.utilities;

import com.provizit.kioskcheckin.services.NdaTime;

import java.io.Serializable;
import java.util.ArrayList;

public class IncompleteData implements Serializable {
    private OidModel _id;
    private String user_id,name,lname,mobile,mobilecode,location,company,cphone,address,supertype,email,fname,mname,comp_id,roleid,rolename,idnumber,designation,caddress,nation,other_2,nda_id,dob,badge;
    private Boolean approver, nda_terms;
    private Float verify,mverify,status,document;
    private MobileDataAddress mobiledata;

    private NdaTime nda_time;

    public ArrayList<String> getPics() {
        return pics;
    }

    public void setPics(ArrayList<String> pics) {
        this.pics = pics;
    }

    private ArrayList<String>pic,pics;
    private ArrayList<VisitorFormDetailsOtherArray> other;

    public ArrayList<VisitorFormDetailsOtherArray> getOther() {
        return other;
    }

    public void setOther(ArrayList<VisitorFormDetailsOtherArray> other) {
        this.other = other;
    }

    //    private ArrayList<String>documents;
    private ArrayList<String>covisitors;
    private CreatedTime created_time;

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public NdaTime getNda_time() {
        return nda_time;
    }

    public void setNda_time(NdaTime nda_time) {
        this.nda_time = nda_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private ArrayList<Belongings> belongings;
    private ArrayList<Vehicles> vehicles;


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

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCphone() {
        return cphone;
    }

    public void setCphone(String cphone) {
        this.cphone = cphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSupertype() {
        return supertype;
    }

    public void setSupertype(String supertype) {
        this.supertype = supertype;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
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

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCaddress() {
        return caddress;
    }

    public void setCaddress(String caddress) {
        this.caddress = caddress;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getOther_2() {
        return other_2;
    }

    public void setOther_2(String other_2) {
        this.other_2 = other_2;
    }

    public String getNda_id() {
        return nda_id;
    }

    public void setNda_id(String nda_id) {
        this.nda_id = nda_id;
    }

    public Boolean getNda_terms() {
        return nda_terms;
    }

    public void setNda_terms(Boolean nda_terms) {
        this.nda_terms = nda_terms;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Boolean getApprover() {
        return approver;
    }

    public void setApprover(Boolean approver) {
        this.approver = approver;
    }

    public Float getVerify() {
        return verify;
    }

    public void setVerify(Float verify) {
        this.verify = verify;
    }

    public Float getMverify() {
        return mverify;
    }

    public void setMverify(Float mverify) {
        this.mverify = mverify;
    }

    public Float getStatus() {
        return status;
    }

    public void setStatus(Float status) {
        this.status = status;
    }

    public Float getDocument() {
        return document;
    }

    public void setDocument(Float document) {
        this.document = document;
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

//    public ArrayList<String> getDocuments() {
//        return documents;
//    }
//
//    public void setDocuments(ArrayList<String> documents) {
//        this.documents = documents;
//    }

    public ArrayList<String> getCovisitors() {
        return covisitors;
    }

    public void setCovisitors(ArrayList<String> covisitors) {
        this.covisitors = covisitors;
    }

    public CreatedTime getCreated_time() {
        return created_time;
    }

    public void setCreated_time(CreatedTime created_time) {
        this.created_time = created_time;
    }

    public ArrayList<Belongings> getBelongings() {
        return belongings;
    }

    public void setBelongings(ArrayList<Belongings> belongings) {
        this.belongings = belongings;
    }

    public ArrayList<Vehicles> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<Vehicles> vehicles) {
        this.vehicles = vehicles;
    }
}
