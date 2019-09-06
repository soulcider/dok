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
import com.dok.common.http.RequestForm;
import com.dok.common.support.ThreadUtil;

public class Executor {

    /** log4j */
    private final Logger LOG          = LoggerFactory.getLogger(this.getClass());

    private boolean      simul        = false;


    public boolean setup(List<RequestForm> inits) {
        if(inits != null && inits.size() > 0) {
            inits.forEach(rf -> {
                int status = rf.execute();
                if(status >= 200 && status < 300) {
                    LOG.debug(" * Execute success\n{}", rf.getResponseBody());
                } else {
                    LOG.debug(" * Execute failure");
                }
                ThreadUtil.sleep(1000);
            });
        }
        return true;
    }


    public Future<Stats> single(Request request, long delay) {
        LOG.debug("Init...");
        LOG.debug(" > users={}, delay={}", 1, delay);
        // task name
        String name = "TASK1";
        Callable<Stats> caller = new Callable<Stats>() {

            @Override
            public Stats call() throws Exception {
                Stats stats = new Stats(name);
                if(delay > 0) {
                    ThreadUtil.sleep(delay);
                }
                //
                stats.incrementCall();
                int status = request.execute();
                if(status >= 200 && status < 300) {
                    stats.incrementSuccess();
                } else {
                    LOG.error("        -Error:: URLConnector");
                    stats.incrementFailure();
                }
                String msg = String.format(" > %s-%06d: %s", name, stats.getCall(), ((status >= 200 && status < 300) ? "SUCCESS" : "FAIL"));
                LOG.debug(msg);

                return stats;
            }

        };

        Future<Stats> future = null;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            LOG.debug("Execute...");
            future = executor.submit(caller);
            executor.shutdown();
            //executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch(Exception e) {
            //e.printStackTrace();
            LOG.error(e.getMessage());
        }
        return future;
    }

    public List<Future<Stats>> multiple(Request request, int userCount, int clickCount, long interval) {
        LOG.debug("Init...");
        LOG.debug(" > users={}, clicks={}, interval={}", userCount, clickCount, interval);
        List<Callable<Stats>> tasks = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            // task name
            String name = String.format("TASK%d", i + 1);

            if(simul) {
                // 동시에 실행
                Callable<Stats> caller = new Callable<Stats>() {

                    @Override
                    public Stats call() throws Exception {
                        Stats stats = new Stats(name);
                        List<Callable<Void>> simulCallers = new ArrayList<>();
                        for (int j = 0; (clickCount < 0 ? true : j < clickCount); j++) {
                            Callable<Void> simulCaller = new Callable<Void>() {
                                @Override
                                public Void call() throws Exception {
                                    if(interval > 0) {
                                        ThreadUtil.sleep(interval);
                                    }
                                    //
                                    stats.incrementCall();
                                    int status = request.execute();
                                    if(status >= 200 && status < 300) {
                                        stats.incrementSuccess();
                                    } else if(status == 909) {
                                        LOG.error("     - URLConnector Error");
                                        stats.incrementFailure();
                                    }
                                    String msg = String.format(" > %s-%06d: %s", name, stats.getCall(), ((status >= 200 && status < 300) ? "SUCCESS" : "FAIL"));
                                    LOG.debug(msg);
                                    return null;
                                }

                            };
                            simulCallers.add(simulCaller);
                        }
                        //
                        ExecutorService executor = Executors.newFixedThreadPool(simulCallers.size());
                        executor.invokeAll(simulCallers);
                        executor.shutdown();

                        return stats;
                    }

                };
                tasks.add(caller);
            } else {
                // 순차적인 실행
                Callable<Stats> caller = new Callable<Stats>() {

                    @Override
                    public Stats call() throws Exception {
                        Stats stats = new Stats(name);
                        for (int j = 0; (clickCount < 0 ? true : j < clickCount); j++) {
                            if(interval > 0) {
                                ThreadUtil.sleep(interval);
                            }
                            //
                            stats.incrementCall();
                            int status = request.execute();
                            if(status >= 200 && status < 300) {
                                stats.incrementSuccess();
                            } else {
                                LOG.error("        -Error:: URLConnector");
                                stats.incrementFailure();
                            }
                            String msg = String.format(" > %s-%06d: %s", name, stats.getCall(), ((status >= 200 && status < 300) ? "SUCCESS" : "FAIL"));
                            LOG.debug(msg);
                        }

                        return stats;
                    }

                };
                tasks.add(caller);
            }

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
