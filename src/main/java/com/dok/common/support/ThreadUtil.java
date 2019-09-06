package com.dok.common.support;


public class ThreadUtil {

    private ThreadUtil() {

    }

    public static long sleep(long delay) {
        try {
            Thread.sleep(delay);
            return delay == 0 ? 0 : delay/1000;
        } catch(Exception ex) {
            return -1;
        }
    }
}
