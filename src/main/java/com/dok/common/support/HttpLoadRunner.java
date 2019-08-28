package com.dok.common.support;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



public class HttpLoadRunner {

    public void execute(int userCount, int clickCount, long interval) {
        System.out.println("Init...");
        //System.out.printf(" > query=%s%n", query);
        System.out.printf(" > users=%d, clicks=%d, interval=%d%n", userCount, clickCount, interval);

        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            NaverCaller caller = new NaverCaller(String.format("TASK%d", i + 1), clickCount, interval);
            caller.init();
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
        //String query = "가짜뉴스아웃";
//      if(args.length == 3) {
//        userCount  = (args[0] == null || args[0].isEmpty()) ? 10 : Integer.parseInt(args[0]);
//        clickCount = (args[1] == null || args[1].isEmpty()) ? 100 : Integer.parseInt(args[1]);
//        interval   = (args[2] == null || args[2].isEmpty()) ? 1000 : Long.parseLong(args[2]);
//      } else {
            userCount = Runtime.getRuntime().availableProcessors();
            clickCount = 10000;
            interval = 1000L;
//      }
        HttpLoadRunner runner = new HttpLoadRunner();
        runner.execute(userCount, clickCount, interval);
    }

}
