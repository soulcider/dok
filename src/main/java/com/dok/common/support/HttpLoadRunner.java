package com.dok.common.support;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


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

        @Override
        public String call() throws Exception {
            Counter count = new Counter();
            //ExecutorService executor = Executors.newSingleThreadExecutor();
            //Runnable runner = () -> {
                for (int j = 0; (clickCount < 0 ? true : j < clickCount); j++) {
                    if(interval > 0) {
                        try {
                            Thread.sleep(interval);
                        } catch(Exception ex) {
                        }
                    }
                    //
                    count.increment();
                    int status = urlc.get();
                    //String str = (res == null ? "NULL" : (res.length() > 100 ? res.substring(0, 100) : res));
                    if(status >= 200 && status < 300) {
                        count.incrementSuccess();
                    } else if(status == 909) {
                        System.err.printf("     - URLConnector Error");
                        count.incrementFailure();
                    }
                    System.out.printf(" > %s-%06d: %s %n", name, count.value(), ((status >= 200 && status < 300) ? "SUCCESS" : "FAIL"));
                }
            //};
            //executor.execute(runner);
            //executor.shutdown();
            return String.format("[%s] Clicks=%d (success=%d, failure:%d)", name, count.value(), count.getSuccess(), count.getFailure());
        }


        public class Counter {

            private long callC  = 0;

            private long rtvalS = 0;
            private long rtvalF = 0;


            public synchronized long increment() {
                return ++callC;
            }

            public long value() {
                return callC;
            }

            public long getSuccess() {
                return rtvalS;
            }

            public void incrementSuccess() {
                ++rtvalS;
            }

            public long getFailure() {
                return rtvalF;
            }

            public void incrementFailure() {
                ++rtvalF;
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
        ExecutorService executor = Executors.newFixedThreadPool(userCount);
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
        String query = "\uC870\uAD6D\uD798\uB0B4\uC138\uC694 \uAE30\uB808\uAE30\uC544\uC6C3";
        //String query = "최종판결";
//      if(args.length == 3) {
//        userCount  = (args[0] == null || args[0].isEmpty()) ? 10 : Integer.parseInt(args[0]);
//        clickCount = (args[1] == null || args[1].isEmpty()) ? 100 : Integer.parseInt(args[1]);
//        interval   = (args[2] == null || args[2].isEmpty()) ? 1000 : Long.parseLong(args[2]);
//      } else {
            userCount = Runtime.getRuntime().availableProcessors();
            clickCount = 10000;
            interval = 200L;
//      }
        HttpLoadRunner runner = new HttpLoadRunner();
        runner.execute(userCount, clickCount, interval, query);
    }

}
