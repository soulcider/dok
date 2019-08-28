package com.dok.common.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.dok.wsc.client.ApiRequest;
import com.dok.wsc.client.ApiResponse;


public class HttpLoadRunner {

    class Caller implements Callable<String> {

        private String        name;
        private int           clickCount;
        private long          interval;

        private HttpConnector urlc;


        public Caller(String name, int clickCount, long interval, String query) {
            this.name = name;
            this.clickCount = clickCount;
            this.interval = interval;

            this.urlc = HttpConnector.configure()
                                     .url("https://search.naver.com/search.naver")
                                     .param("sm", "top_hty")
                                     .param("fbm", "0")
                                     .param("ie", "utf8")
                                     .param("query", query)
                                     .cookie(false)
                                     .build();
        }

        public void init() {
            try {

                Map<String, String> deviceInfo = new HashMap<>();
                deviceInfo.put("tlcmNm", "SKTelecom");
                deviceInfo.put("mdlNm", "SM-G960N");
                deviceInfo.put("osNm", "Android");
                deviceInfo.put("osVer", "26");
                deviceInfo.put("zrpAppVer", "1");

                // zeropay request
                ApiRequest request = ApiRequest.request("MPZ1001")
                                                       .param("userSno", this.userSno)
                                                       .param("deviceInfo", deviceInfo)
                                                       .build();

                ApiResponse response = client.invoke(request, ApiResponse.class);

                String result = response.getRescode();
                System.out.printf("---------------> %s%n", result);
            }catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public String call() throws Exception {
            Stats stats = new Stats();

            /*
             * 순차적인 클릭
            */
            for (int j = 0; (clickCount < 0 ? true : j < clickCount); j++) {
                if(interval > 0) {
                    try { Thread.sleep(interval); } catch(Exception ex) { }
                }
                //
                stats.incrementCall();
                int status = urlc.get();
                //String str = (res == null ? "NULL" : (res.length() > 100 ? res.substring(0, 100) : res));
                if(status >= 200 && status < 300) {
                    stats.incrementSuccess();
                } else if(status == 909) {
                    System.err.printf("     - URLConnector Error");
                    stats.incrementFailure();
                }
                System.out.printf(" > %s-%06d: %s %n", name, stats.getCall(), ((status >= 200 && status < 300) ? "SUCCESS" : "FAIL"));
            }



            /*
             * 동시 클릭
             *
            List<Callable<Void>> callers = new ArrayList<>();
            for (int j = 0; (clickCount < 0 ? true : j < clickCount); j++) {
                Callable<Void> caller = new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        if(interval > 0) {
                            try { Thread.sleep(interval); } catch(Exception ex) { }
                        }
                        //
                        stats.incrementCall();
                        int status = urlc.get();
                        //String str = (res == null ? "NULL" : (res.length() > 100 ? res.substring(0, 100) : res));
                        if(status >= 200 && status < 300) {
                            stats.incrementSuccess();
                        } else if(status == 909) {
                            System.err.printf("     - URLConnector Error");
                            stats.incrementFailure();
                        }
                        System.out.printf(" > %s-%06d: %s %n", name, stats.getCall(), ((status >= 200 && status < 300) ? "SUCCESS" : "FAIL"));
                        return null;
                    }

                };
                callers.add(caller);
            }
            //
            ExecutorService executor = Executors.newFixedThreadPool(callers.size());
            executor.invokeAll(callers);
            executor.shutdown();
            */

            return String.format("[%s] Clicks=%d (success=%d, failure:%d)", name, stats.getCall(), stats.getSuccess(), stats.getFailure());
        }


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


    public void execute(int userCount, int clickCount, long interval, String query) {
        System.out.println("Init...");
        System.out.printf(" > query=%s%n", query);
        System.out.printf(" > users=%d, clicks=%d, interval=%d%n", userCount, clickCount, interval);

        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            Caller caller = new Caller(String.format("TASK%d", i + 1), clickCount, interval, query);
            //caller.init();
            tasks.add(caller);
        }

        List<Future<String>> futures = null;
        ExecutorService executor = Executors.newFixedThreadPool(tasks.size());
        try {
            System.out.println("Execute...");
            futures = executor.invokeAll(tasks);
            executor.shutdown();
            //executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch(Exception e) {
            //e.printStackTrace();
            System.err.println(e.getMessage());
        }

        if(futures != null && futures.size() > 0) {
            System.out.println("Result...");
            for(Future<String> future : futures) {
                String result = null;
                try {
                    result = future.get();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                System.out.printf(" > %s%n", result);
            }
        }
    }

    public static void main(String[] args) {
        int userCount;
        int clickCount;
        long interval;
        //String query = "\uAE30\uB808\uAE30\uC544\uC6C3";
        String query = "가짜뉴스아웃";
//      if(args.length == 3) {
//        userCount  = (args[0] == null || args[0].isEmpty()) ? 10 : Integer.parseInt(args[0]);
//        clickCount = (args[1] == null || args[1].isEmpty()) ? 100 : Integer.parseInt(args[1]);
//        interval   = (args[2] == null || args[2].isEmpty()) ? 1000 : Long.parseLong(args[2]);
//      } else {
            userCount = Runtime.getRuntime().availableProcessors();
            clickCount = 10000;
            interval = 500L;
//      }
        HttpLoadRunner runner = new HttpLoadRunner();
        runner.execute(userCount, clickCount, interval, query);
    }

}
