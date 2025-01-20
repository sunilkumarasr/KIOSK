package com.provizit.kioskcheckin.utilities.WorkPermit;

import java.io.Serializable;

public class Ends implements Serializable {

    private long start,end;


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
}
