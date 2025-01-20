package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;
import java.util.ArrayList;

public class Invite implements Serializable {
    private Boolean pdfStatus,covisitor,assign,remove,view_status;
    private Float status,verify,pslot,userstatus;
    private String email,mobile,name,company,id,link,approver_roleid;
    private ArrayList<String>pic;
    private ArrayList<String>reschedule;
    private ArrayList<String>approver_roleids;
    private ArrayList<String>approver_statistics;
}
