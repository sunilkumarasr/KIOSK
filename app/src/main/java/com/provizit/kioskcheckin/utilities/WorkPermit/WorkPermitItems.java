package com.provizit.kioskcheckin.utilities.WorkPermit;

import com.provizit.kioskcheckin.utilities.Getdocuments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkPermitItems implements Serializable {

    private CommonObject _id;
    private String companyName;
    private long start,end;

    private List<ContractorsData> contractorsData;
    private ArrayList<String> starts;
    private ArrayList<String> ends;

    private WorkTypeData worktypeData;
    private WorkLocationData worklocationData;


    public CommonObject get_id() {
        return _id;
    }

    public void set_id(CommonObject _id) {
        this._id = _id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public List<ContractorsData> getContractorsData() {
        return contractorsData;
    }

    public void setContractorsData(List<ContractorsData> contractorsData) {
        this.contractorsData = contractorsData;
    }


    public ArrayList<String> getStarts() {
        return starts;
    }

    public void setStarts(ArrayList<String> starts) {
        this.starts = starts;
    }

    public ArrayList<String> getEnds() {
        return ends;
    }

    public void setEnds(ArrayList<String> ends) {
        this.ends = ends;
    }

    public WorkTypeData getWorktypeData() {
        return worktypeData;
    }

    public void setWorktypeData(WorkTypeData worktypeData) {
        this.worktypeData = worktypeData;
    }

    public WorkLocationData getWorklocationData() {
        return worklocationData;
    }

    public void setWorklocationData(WorkLocationData worklocationData) {
        this.worklocationData = worklocationData;
    }
}
