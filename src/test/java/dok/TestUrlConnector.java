package dok;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.dok.common.support.UrlConnector;

public class TestUrlConnector {

    class Caller implements Callable<String> {

        private String       name;
        private int          actionCount;
        private long         sleep;

        private UrlConnector urlc;


        public Caller(String name, int actionCount, long sleep, String query) {
            this.name = name;
            this.actionCount = actionCount;
            this.sleep = sleep;

              this.urlc = UrlConnector.configure()
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
                for (int j = 0; j < actionCount; j++) {
                    if(sleep > 0) {
                        try {
                            Thread.sleep(sleep);
                        } catch(Exception ex) {
                        }
                    }
                    new Thread(() -> {
                        String res = urlc.get();
                        String str = (res == null ? "NULL" : (res.length() > 100 ? res.substring(0, 100) : res));
                        System.out.printf("> %s-%06d  %s %n", name, count.increment(), str);
                    }).start();
                }
            };
            executor.execute(runner);
            executor.shutdown();
            return name + " " + count.value();
        }


        public class Counter {

            private int value = 0;


            public synchronized int increment() {
                return ++value;
            }

            public int value() {
                return value;
            }
        }

  }


  public void execute(int taskCount, int actionCount, long sleep, String query) {
      System.out.println("Init...");
      System.out.printf(">> tasks=%d, actions=%d, sleep=%d%n", taskCount, actionCount, sleep);

      List<Callable<String>> tasks = new ArrayList<>();
      for(int i = 0; i < taskCount; i++) {
          Caller caller = new Caller(String.format("Task%d", i+1), actionCount, sleep, query);
          //caller.init();
          tasks.add(caller);
      }

      ExecutorService executor = Executors.newFixedThreadPool(taskCount);
      System.out.println("Execute...");
      try {
          executor.invokeAll(tasks);
          executor.shutdown();
          //executor.awaitTermination(10, TimeUnit.MINUTES);
      } catch(Exception e) {
          e.printStackTrace();
      }
  }

  public static void main(String[] args) {
      int taskCount = Runtime.getRuntime().availableProcessors();
      int actionCount = 100;
      long sleep = 10000L;
      String query = "\uC870\uAD6D\uD798\uB0B4\uC138\uC694";
//      if(args.length == 2) {
//        query = (args[0] == null || args[0].isEmpty()) ? "" : args[0];
//        actionCount = (args[1] == null || args[1].isEmpty()) ? 10 : Integer.parseInt(args[1]);
//      }

      System.out.println(" * query=" + query);
      TestUrlConnector test = new TestUrlConnector();
      test.execute(taskCount, actionCount, sleep, query);
  }


}
