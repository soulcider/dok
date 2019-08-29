package com.dok.common.callable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.dok.common.http.Request;

public class Executor {

  private Request setupRequest = null;


  public void setup(Request request) {
    this.setupRequest = request;
  }

  public List<Future<Stats>> execute(Request request, int userCount, int clickCount, long interval) {
    System.out.println("Init...");
    //System.out.printf(" > query=%s%n", query);
    System.out.printf(" > users=%d, clicks=%d, interval=%d%n", userCount, clickCount, interval);
    List<Callable<Stats>> tasks = new ArrayList<>();
    for (int i = 0; i < userCount; i++) {

      String name = String.format("TASK%d", i + 1);

        Callable<Stats> caller = new Callable<Stats>() {

          @Override
          public Stats call() throws Exception {
            Stats stats = new Stats(name);

            /*
             * 순차적인 클릭
            */
            for (int j = 0; (clickCount < 0 ? true : j < clickCount); j++) {
                if(interval > 0) {
                    try { Thread.sleep(interval); } catch(Exception ex) { }
                }
                //
                stats.incrementCall();


                int status = request.execute();
                //String str = (res == null ? "NULL" : (res.length() > 100 ? res.substring(0, 100) : res));
                if(status >= 200 && status < 300) {
                    stats.incrementSuccess();
                } else {
                    System.err.println("        -Error:: URLConnector");
                    stats.incrementFailure();
                }
                System.out.printf(" > %s-%06d: %s%n", name, stats.getCall(), ((status >= 200 && status < 300) ? "SUCCESS" : "FAIL"));
            }



            /*
             * 동시 클릭

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

            //return String.format("[%s] Clicks=%d (success=%d, failure:%d)", name, stats.getCall(), stats.getSuccess(), stats.getFailure());
            return stats;
          }

        };

        if(setupRequest != null) {
          int status = setupRequest.execute();
          //System.out.printf("---------------> %s%n", status);
          if(status >= 200 && status < 300) {
              System.out.println(" * Init Success");
          } else {
              System.err.println(" * Init Failure");
          }
        }

        tasks.add(caller);
    }


    List<Future<Stats>> futures = null;
    int size = tasks == null ? 0 : tasks.size();
    if(size > 0) {
      ExecutorService executor = Executors.newFixedThreadPool(size);
      try {
          System.out.println("Execute...");
          futures = executor.invokeAll(tasks);
          executor.shutdown();
          //executor.awaitTermination(10, TimeUnit.MINUTES);
      } catch(Exception e) {
          //e.printStackTrace();
          System.err.println(e.getMessage());
      }

      /*
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
      */
    }
    return futures;
  }

}
