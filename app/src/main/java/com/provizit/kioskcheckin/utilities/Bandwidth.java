package com.provizit.kioskcheckin.utilities;

import java.io.Serializable;

public class Bandwidth implements Serializable {
    private Float size,count,avgObjSize,storageSize;

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public Float getCount() {
        return count;
    }

    public void setCount(Float count) {
        this.count = count;
    }

    public Float getAvgObjSize() {
        return avgObjSize;
    }

    public void setAvgObjSize(Float avgObjSize) {
        this.avgObjSize = avgObjSize;
    }

    public Float getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(Float storageSize) {
        this.storageSize = storageSize;
    }
}
