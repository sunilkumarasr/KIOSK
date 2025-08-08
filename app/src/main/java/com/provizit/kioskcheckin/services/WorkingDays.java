package com.provizit.kioskcheckin.services;

import java.io.Serializable;
import java.util.ArrayList;

public class WorkingDays implements Serializable {

    private ArrayList<WorkingDaysList> workingdays;


    public ArrayList<WorkingDaysList> getWorkingdays() {
        return workingdays;
    }

    public void setWorkingdays(ArrayList<WorkingDaysList> workingdays) {
        this.workingdays = workingdays;
    }
}
