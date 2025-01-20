package com.provizit.kioskcheckin.calendar;

import java.util.Calendar;

public class mycalendardata {

    private Calendar calendar;
    private Long timeMilli;
    // constructor
    public mycalendardata(int startdate) {
        this.calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, startdate);
        setThis();
    }

    private void setThis() {
        Calendar day = calendar;
        day.set(Calendar.MILLISECOND, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.HOUR_OF_DAY, 0);
        this.timeMilli = day.getTimeInMillis() / 1000;
    }

    public Long getTimeMilli() {
        return this.timeMilli;
    }


}
