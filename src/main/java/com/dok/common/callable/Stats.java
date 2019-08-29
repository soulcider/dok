package com.dok.common.callable;

public class Stats {

    private String name = null;
    private long   reqC = 0L;
    private long   resS = 0L;
    private long   resF = 0L;
    //private String status = null;


    public Stats(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public synchronized long incrementCall() {
        return ++reqC;
    }

    public long getCall() {
        return reqC;
    }

    public long getSuccess() {
        return resS;
    }

    public void incrementSuccess() {
        ++resS;
    }

    public long getFailure() {
        return resF;
    }

    public void incrementFailure() {
        ++resF;
    }

}
