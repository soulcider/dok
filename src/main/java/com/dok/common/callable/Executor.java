package com.dok.common.callable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dok.common.http.Request;

public class Executor {

  /** log4j */
  private final Logger LOG  = LoggerFactory.getLogger(this.getClass());

  private Request setupRequest = null;


  public void setup(Request request) {
    this.setupRequest = request;
  }

  public List<Future<Stats>> execute(Request request, int userCount, int clickCount, long interval) {
    LOG.debug("Init...");
    //System.out.printf(" > query=%s%n", query);
    LOG.debug(" > users={}, clicks={}, interval={}", userCount, clickCount, interval);
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
                    LOG.error("        -Error:: URLConnector");
                    stats.incrementFailure();
                }
                String msg = String.format(" > %s-%06d: %s", name, stats.getCall(), ((status >= 200 && status < 300) ? "SUCCESS" : "FAIL"));
                LOG.debug(msg);
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
              LOG.debug(" * Init Success");
          } else {
              LOG.debug(" * Init Failure");
          }
        }

        tasks.add(caller);
    }


    List<Future<Stats>> futures = null;
    int size = tasks == null ? 0 : tasks.size();
    if(size > 0) {
      ExecutorService executor = Executors.newFixedThreadPool(size);
      try {
          LOG.debug("Execute...");
          futures = executor.invokeAll(tasks);
          executor.shutdown();
          //executor.awaitTermination(10, TimeUnit.MINUTES);
      } catch(Exception e) {
          //e.printStackTrace();
          LOG.error(e.getMessage());
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
