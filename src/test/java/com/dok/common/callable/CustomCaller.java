package com.dok.common.callable;

import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dok.common.http.Connector;
import com.dok.common.http.Request.Method;
import com.dok.common.http.RequestForm;

public class CustomCaller  {

    /** log4j */
    private final Logger LOG  = LoggerFactory.getLogger(this.getClass());

    private Connector urlc = Connector.custom()
                             .contentType("application/x-www-form-urlencoded")
                             .accept("text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8")
                             .acceptLanguage("en,ko-KR;q=0.5")
                             .connectionTimeout(30)
                             .socketTimeout(60)
                             .build();

    public void singleRequest() {

        RequestForm search = RequestForm.configure()
            .url("https://search.naver.com/search.naver")
            .method(Method.GET)
            .ping(true)
            .connector(urlc)
            .param("sm", "top_hty")
            .param("fbm", "0")
            .param("ie", "utf8")
            .param("query", "\uD55C\uAD6D\uC5B8\uB860\uC0AC\uB9DD")
            .build();

        int status = search.execute();
        if(status >= 200 && status < 300) {
          LOG.debug(" * Execute failure");
        } else {
          LOG.debug(" * Execute success");
        }
    }

    public void multiRequest() {

        RequestForm search = RequestForm.configure()
            .url("https://search.naver.com/search.naver")
            .method(Method.GET)
            .ping(true)
            .connector(urlc)
            .param("sm", "top_hty")
            .param("fbm", "0")
            .param("ie", "utf8")
            .param("query", "\uAC00\uC9DC\uB274\uC2A4\uC544\uC6C3")
            .build();

      int userCount = Runtime.getRuntime().availableProcessors();
      int clickCount= 10;
      long interval= 1000;
      Executor executor = new Executor();
      List<Future<Stats>> futures = executor.execute(search, userCount, clickCount, interval);
      if(futures != null && futures.size() > 0) {
        LOG.debug("Result...");
          for(Future<Stats> future : futures) {
              try {
                  Stats stats = future.get();
                  String msg = String.format("[%s] Clicks=%d (success=%d, failure:%d)", stats.getName(), stats.getCall(), stats.getSuccess(), stats.getFailure());
                  LOG.debug(msg);
              } catch (Exception e) {
                LOG.error(e.getMessage());
              }
          }
      }

  }

    public static void main(String[] args) {
      CustomCaller call = new CustomCaller();
      //call.singleRequest();
      call.multiRequest();
    }

}

