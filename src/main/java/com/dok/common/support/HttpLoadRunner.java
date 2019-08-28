package com.dok.common.support;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpLoadRunner {

    class Caller implements Callable<String> {

        private String       name;
        private int          clickCount;
        private long         interval;

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
                                      .cookie(true)
                                      .build();
        }

        @Override
        public String call() throws Exception {
            Counter count = new Counter();
            ExecutorService executor = Executors.newCachedThreadPool();
            Runnable runner = () -> {
                for (int j = 0; j < clickCount; j++) {
                    if(interval > 0) {
                        try { Thread.sleep(interval); } catch(Exception ex) { }
                    }
                    new Thread(() -> {
                        count.increment();
                        int status = urlc.get();
                        //String str = (res == null ? "NULL" : (res.length() > 100 ? res.substring(0, 100) : res));
                        if(status >= 200 && status < 300) {
                          count.incrementSuccess();
                        } else {
                          count.incrementFailure();
                        }
                        System.out.printf(" > %s-%06d: %s %n", name, count.value(), ((status >= 200 && status < 300) ? "SUCCESS" : "FAIL"));
                    }).start();
                }
            };
            executor.execute(runner);
            executor.shutdown();
            return String.format("[%s] Try=%d (success=%d, failure:%d)", name, count.value(), count.getSuccess(), count.getFailure());
        }


      public class Counter {

        private long callC        = 0;

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
      System.out.printf(" > user=%d, click=%d, interval=%d%n", userCount, clickCount, interval);

      List<Callable<String>> tasks = new ArrayList<>();
      for(int i = 0; i < userCount; i++) {
          Caller caller = new Caller(String.format("TASK%d", i+1), clickCount, interval, query);
          //caller.init();
          tasks.add(caller);
      }

      ExecutorService executor = Executors.newFixedThreadPool(userCount);
      try {
          System.out.println("Execute...");
          executor.invokeAll(tasks);
          executor.shutdown();
          //executor.awaitTermination(10, TimeUnit.MINUTES);
      } catch(Exception e) {
          //e.printStackTrace();
          System.err.println(e.getMessage());
      }
  }

  public static void main(String[] args) {
      int userCount = Runtime.getRuntime().availableProcessors();
      int clickCount = 1000;
      long interval = 10000L;
      String query = "\uC870\uAD6D\uD798\uB0B4\uC138\uC694";
//      if(args.length == 2) {
//        query = (args[0] == null || args[0].isEmpty()) ? "" : args[0];
//        clickCount = (args[1] == null || args[1].isEmpty()) ? 10 : Integer.parseInt(args[1]);
//      }

      HttpLoadRunner runner = new HttpLoadRunner();
      runner.execute(userCount, clickCount, interval, query);
  }


}
