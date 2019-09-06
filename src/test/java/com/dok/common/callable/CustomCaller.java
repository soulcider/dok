package com.dok.common.callable;

import java.util.ArrayList;
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

    public void singleRequest() {

        Connector urlc = Connector.custom()
                                 .contentType("application/x-www-form-urlencoded")
                                 .accept("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                                 .acceptLanguage("en,ko-KR;q=0.5")
                                 .connectionTimeout(30)
                                 .socketTimeout(60)
                                 .build();

        RequestForm search = RequestForm.configure()
            .url("https://search.naver.com/search.naver")
            .method(Method.GET)
            .ping(false)
            .connector(urlc)
            .param("sm", "top_hty")
            .param("fbm", "0")
            .param("ie", "utf8")
            .param("query", "\uD55C\uAD6D\uC5B8\uB860\uC0AC\uB9DD")
            .build();

        int status = search.execute();
        if(status >= 200 && status < 300) {
          LOG.debug(" * Execute success\n{}", search.getResponseBody());
        } else {
          LOG.debug(" * Execute failure");
        }
    }

    public void multiRequest() {

        Connector urlc = Connector.custom()
                                 .contentType("application/x-www-form-urlencoded")
                                 .accept("*/*")
                                 .acceptLanguage("en,ko-KR;q=0.5")
                                 .connectionTimeout(30)
                                 .socketTimeout(60)
                                 .build();

        RequestForm keys = RequestForm.configure()
            .url("https://nid.naver.com/login/ext/keys_js3.nhn")
            .method(Method.GET)
            .ping(false)
            .connector(urlc)
            .header("DNT", "1")
            .header("Referer", "https://nid.naver.com/nidlogin.login")
            .build();

        RequestForm login = RequestForm.configure()
            .url("https://nid.naver.com/nidlogin.login")
            .method(Method.GET)
            .ping(false)
            .connector(urlc)
            .header("DNT", "1")
            .header("Upgrade-Insecure-Requests", "1")
            .header("Referer", "https://nid.naver.com/nidlogin.login")
            .param("localechange", "top_hty")
            .param("encpw", "6641991c308dba7edb4601c38b47ead4af990cda828b7be763bc31cfb4830b41978e52023d0cd5110920ebbebb1760d9d10c8a3e515de959a220e0e836c886714078774116c0154eabd0be5c3325138fec9f275d7a2b88500c1e84b52eb65dd5144c7537f0a8d54266e7814183d328d1c6a871d38958f227d91141f6728e8e2d")
            .param("enctp", "1")
            .param("svctype", "1")
            .param("smart_LEVEL", "1")
            .param("bvsd", "%7B%22uuid%22%3A%22b4b8075a-1a4d-4d6c-9321-b2399248e852-0%22%2C%22encData%22%3A%22N4IghiBcIEYCwwBwAYDsBWMBaAjGOAJloQGwDGWAnAMwBMOWMt1lltciApourVsiAA0sKCBwA6auLhCQZKADMwAGwDOnYQSgBtUAEtRercIiRtAXWEwooUxatRkAX2HzoqgPYBXZWSOcAJ1ktd29ffyDhTkUVdWEFGLVOF31RAAcAd1k7SxFIWx1c60hnV1Fg8qioABcArw0QBMglJKdc6PzwGy7IVkoHXtYywcoXPIKRgb7hvqcxponFnum8lbdZgYmV4rWoWbHipdNtvaG5U9HN5bOds-XWObGAc267EGQAH1pERFQPxFoHxIsk%2BOBIH0%2BqBBHxwf1B6GhYI%2BuC%2BOERiGR1BhCOE8MxMLRuJhcKBiL%2BWEBOFoZOROBhMiJSJRtGBjPJdLBNIp2MR4JRVJpoIZ7xh4NB1EFBN5EKljIxdNZIqRdMQkupjLFX2h1DgMsJYhhP01AmElOo6BlJpFlDglMx6I%2Buv1oPBWIlbJhMOlgKtn3YlBluERAZRWEVLuRFqoXN1xGlWCxCa5ODpuF9otpn2jGtpIZwqo9uETnMZqZDBaVOGoqDQQLpcHdIo4gPlDtoFsoNNoAc7jItOph6qVfxwupxw6%2Bn2FoJH4OdGapXpzi-n%2BfppJztBbXYtFYjtE%2B45nxI%2BjePOD90tdDoDQ%2BPAen2P%2BHyPJ4vL%2B9WJLE9HHyhHofaUkT3Q0YSnK9Twgu8T2vD0vxpC1oKReC4IhaULVXP4LXTWEwLQzd8InMUaWInNSInXUcL%2BSjpRonMsRwjEfWlOFpQYh1sK7Qj924jNmLlEdeMoDleJYT101tT1oPQOARJwudRKdXiSAUqi9W1QF2MZAM6TPJ8tInUEgK1OVT0db1RNrLN5zgT5rNkDl6DpOzZEpJEXKKKAL36c5IHQRAfJCagSFoKpICpOBeBweJHDGAArLzu2EAALUQlEQPBbRgAgVNQAhKDIEhbNQasawUMqSBYeBZAMTpTBAABZDwAC89GUZQwAAenQcRkAAAgACgAdT0AA7AgPAyVQ%2BoAOQAFT6i9eoAbj6kbRqK1aAA9Nr6gIADdIBIRBeoASj6gBxTgyAAaw8TqDwvZALxwPqADE9ACTgFA8LbOuO3rZGKEBOFG2Q3HYTQoFwMLosaKA4GEF4zBwNhkEEC8UFyVKUbRjHkCx4RaqwGT0ZABLwuEG6vOEZQaZAABbUQvFGm7RsmsHhDB6B1roWQPFEfUwByYQ0lEVA4EodApZknAYBgNgWU4MBaDIDA6FoMAMpgc1EHkYQAEdRDAGAyDwGhCpIeXhbADBkCYEhUFQKlUFoAhzRC2QgmgC6PA8J5lE4PqAElRrIcQAD8AEEZougAZABRQbQ%2BqThlAGgAlc6AFUAAkABFLoCMA0mSvQyGmkL%2Bvzz7ruqah85TPr9tUAB9dBW-6tI2475BTtkVREjiEBqiHhovDH4R9snkAsmaWIGi2nR0ZaOJV84XIAE8dBAKOAj0FRZD3g-lD6gAhDrbqP-eVFmsAAgCSbr5Pvrs9G8uPAIIOGoAZVkM%2B-Y3T6lHUa1Q9AGy8BAYQACPA3QZmAUafUADyygCB9R-tUTegdZAAGEVB6BgPvXBYAGaEIPsQ0hN8%2BoNTANUVKwgcGg2qF4AI28GFMJYZvS6Hg6Hl1wR4Bm5d0EIOmr-fho1PAdUHgw7w%2B9Aj8JYXoQIs1OBZGEBde%2BJCPDjVkFdDwAQnjkOELnNO%2B1OBgLIFAkAwcGZpDAGQUewg45eD8AQMAfUcHaMkdEJxLijDuNzggggGR95gNGi8Xxrj3E-xEa-d%2BZBP4%2BMauXR%2BngFDVGERI9BgQ9AJGEE1dmmC0hB08QEVQeh9pWN-tw3h%2BtGo-z6gABV9rU2Q1SM7fUCKDMgQcYlZJ-jkvJ9TMnTQGfvIZ1Sxm5NkI0lQtCxoeD6nHBZRSkkDKeB4IOjT96gNkOszZ6CyD7zSI4kA%2Byg7Z2DnszgGyLnByWXoJ4yVTnnNfvcgZgiYAeFQdc25bz0Gb1Id82Qc0wDJQEVYuaegGacGkSAKFMLpozVUX1DOELObwq%2BjAFxyVzHUL-sIAAaoENxo0rHrSeAQMaTw4UUqpeE6aQ46XUumhKcwcwgA%22%7D")
            .param("encnm", "100014641")
            .param("locale", "en_US")
            .param("url", "http%3A%2F%2Fwww.naver.com")
            .param("id", "")
            .param("pw", "")
            .build();

        RequestForm signin = RequestForm.configure()
            .url("https://nid.naver.com/signin/v3/finalize")
            .method(Method.GET)
            .ping(false)
            .connector(urlc)
            .header("DNT", "1")
            .header("Upgrade-Insecure-Requests", "1")
            .header("Referer", "https://nid.naver.com/nidlogin.login")
            .param("svctype", "1")
            .param("url", "url=http%3A%2F%2Fwww.naver.com")
            .build();

        //https://search.naver.com/search.naver?sm=top_sug.pre&fbm=1&acr=2&acq=%EB%82%98%EA%B2%BD%EC%9B%90&qdt=0&ie=utf8&query=%EB%82%98%EA%B2%BD%EC%9B%90%EC%86%8C%ED%99%98%EC%A1%B0%EC%82%AC
        RequestForm search1 = RequestForm.configure()
                .url("https://search.naver.com/search.naver")
                .method(Method.GET)
                .ping(true)
                .connector(urlc)
                .header("DNT", "1")
                .header("Upgrade-Insecure-Requests", "1")
                .param("sm", "top_sug.pre")
                .param("fbm", "1")
                .param("acr", "2")
                .param("acq", "%EB%82%98%EA%B2%BD%EC%9B%90")
                .param("qdt", "0")
                .param("ie", "utf8")
                .param("query", "\uD55C\uAD6D\uC5B8\uB860\uC0AC\uB9DD")
                .build();


        //https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&query=%EB%82%98%EA%B2%BD%EC%9B%90%EC%86%8C%ED%99%98%EC%A1%B0%EC%82%AC&oquery=%EB%82%98%EA%B2%BD%EC%9B%90%EC%86%8C%ED%99%98%EC%A1%B0%EC%82%AC&tqi=UUbBTsp0JXVsstH1pm4ssssssYR-300127 HTTP/1.1
        RequestForm search2 = RequestForm.configure()
            .url("https://search.naver.com/search.naver")
            .method(Method.GET)
            .ping(true)
            .connector(urlc)
            .header("DNT", "1")
            .header("Upgrade-Insecure-Requests", "1")
            .param("sm", "tab_hty.top")
            .param("where", "nexearch")
            .param("tqi", "UUbDIdp0JywssnRcvl4sssssthR-060648")
            .param("query",  "%EB%82%98%EA%B2%BD%EC%9B%90%EC%86%8C%ED%99%98%EC%A1%B0%EC%82%AC")
            .param("oquery", "%EB%82%98%EA%B2%BD%EC%9B%90%EC%86%8C%ED%99%98%EC%A1%B0%EC%82%AC")
            .build();


        List<RequestForm> inits = new ArrayList<RequestForm>();
        inits.add(keys);
        inits.add(login);
        inits.add(signin);
        inits.add(search1);


      int userCount = Runtime.getRuntime().availableProcessors();
      int clickCount= 1000;
      long interval= 1000;
      Executor executor = new Executor();
      boolean flag = executor.setup(inits);
      LOG.debug("Setup... {}", flag);
      List<Future<Stats>> futures = executor.multiple(search2, userCount, clickCount, interval);
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

