package com.dok.common.support;

import java.util.concurrent.Callable;

public abstract class HttpCaller implements Callable<String> {

    private String        name;
    private int           clickCount;
    private long          interval;

    private HttpConnector urlc;


    public HttpCaller(String name, int clickCount, long interval) {
        this.name = name;
        this.clickCount = clickCount;
        this.interval = interval;

        this.urlc = HttpConnector.configure()
                                 .contentType("application/x-www-form-urlencoded")
                                 .accept("text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8")
                                 .acceptLanguage("en,ko-KR;q=0.5")
                                 .connectionTimeout(30)
                                 .socketTimeout(60)
                                 .build();

    }

    public abstract void init() ;

    //@Override
    //public String call() throws Exception ;


    public class Stats {

        private long reqC = 0L;
        private long resS = 0L;
        private long resF = 0L;
        private String status = null;


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

}

