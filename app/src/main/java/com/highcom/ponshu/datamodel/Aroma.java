package com.highcom.ponshu.datamodel;

import java.util.Date;

public class Aroma {
    private long elapsedCount;
    private long aromaLevel;
    private Date elapsedDate;

    public Aroma(long elapsedCount, long aromaLevel, Date elapsedDate) {
        this.elapsedCount = elapsedCount;
        this.aromaLevel = aromaLevel;
        this.elapsedDate = elapsedDate;
    }

    public Long getElapsedCount() {
        return elapsedCount;
    }

    public Long getAromaLevel() {
        return aromaLevel;
    }

    public Date getElapsedDate() {
        return elapsedDate;
    }
}
