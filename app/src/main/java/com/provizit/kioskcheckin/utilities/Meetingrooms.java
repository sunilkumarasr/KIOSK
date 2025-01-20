package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;

public class Meetingrooms implements Serializable {
    private String name;
    private String pointer;

    public String getPointer() {
        return pointer;
    }

    public void setPointer(String pointer) {
        this.pointer = pointer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
