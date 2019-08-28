package dok;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.dok.common.support.UrlConnector;

public class TestUrlConnector {

  class Caller implements Callable<String> {

    private String       name        = null;
    private int          actionCount = 1000;
    private long         sleep       = 1000;

    private UrlConnector urlc        = null;


    public Caller(String name, String query, int actionCount) {
      this.name = name;
      this.actionCount = actionCount;

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
            try { Thread.sleep(sleep); } catch(Exception ex) { }
          }
          new Thread(() -> {
              String s = urlc.get();
            System.out.printf("> %04d - %s %n ", count.increment(), s == null ? "NULL" : s.length() > 100 ? s.substring(0, 100): s);
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


  public void execute(String query, int actionCount) {
      System.out.println("Init...");
      int taskCount = Runtime.getRuntime().availableProcessors();
      int sleep = 1000;
      System.out.printf(">> tasks=%d, actions=%d, sleep=%d%n", taskCount, actionCount, sleep);

      List<Callable<String>> tasks = new ArrayList<>();
      for(int i = 0; i < taskCount; i++) {
          Caller caller = new Caller(String.format("Task%04d", i+1), query, actionCount);
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
    String query;
    int actionCount;
      if(args.length == 2) {
        query = (args[0] == null || args[0].isEmpty()) ? "" : args[0];
        actionCount = (args[1] == null || args[1].isEmpty()) ? 10 : Integer.parseInt(args[1]);
      } else {
        query = "조국힘내세요";
        actionCount = 1000;
      }

      TestUrlConnector test = new TestUrlConnector();
      test.execute(query, actionCount);
  }


}
