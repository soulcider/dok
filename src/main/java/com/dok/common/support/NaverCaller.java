package com.dok.common.support;

public class NaverCaller extends HttpCaller {

    private String        name;
    private int           clickCount;
    private long          interval;

    private HttpConnector urlc;


    public NaverCaller(String name, int clickCount, long interval) {
        super(name, clickCount, interval);
    }

    @Override
    public void init() {
        HttpRequest request = HttpRequest.configure()
                .url("https://nid.naver.com/nidlogin.login")
                .param("localechange", "")
                .param("encpw", "22ba2f1971867970e3e876026662e22fe34f83fffaa7c5222c77fc8b8ed7dd64a13a25cb0adb81ae643043b7aa0168bdb778ddff0ea4456f70213547a8efd4515837f7b443c9fbd633ee125218a74e15fe5e5089aeedcfcb2594fd5a2ae3b07d58cb70d919f847efbc6157a46407e7fd69c65b359332ea67f71d40b6779ea245")
                .param("enctp", "1")
                .param("svctype", "1")
                .param("smart_LEVEL", "1")
                .param("bvsd", "%7B%22uuid%22%3A%2295d12f63-b0f7-4ee1-bdfd-52d4e360ca00-0%22%2C%22encData%22%3A%22N4IghiBcIJwKwBMCMAmAZgNgMwFoBGADGgOw4AsAphUvgmgjnCgpVhgQMZgEE4EgAaEHiggkAOiziygkByhowAGwDOFIQigBtUAEtRuzUIiQtAXSEjIoE1pAEBKgPYBXJR0MUATiAvCoBAC%2BQvLQsprQzm4eCN6yFArKakJoiaoUwXqiAA4A7rK2flY22vZCRQHBcqLhNUIJkAAuXi7qIKmQiumBfg0l1uBQZMOWQyPVkMNkVcWDk%2BNWUyFj01Ud-RtzS-7zZMu7M1D924vjoVOBhwMmxwsr%2Bxej1-c72%2BfDl1UA5kdzdgQAHzgGCQAKQqBgAJQAA4sLJAcQAThEYx4WDQTgkFgkTIhICkCisIDMfw8WDCdCkUhSfYwRgkXBidC0UhKThofTMRgWZziMSCTykRg2eCWWy4GyUEgWSiMGQkUTBYxIZiYEqmFS4WSkJzhkjmdrZfLMbjaQSFbKZRakcQrTgsHAkWrtZysCrbS6FWyPWa2VgUab8QHHSSWSGiUK7ShdWKkSgQ9zDXHjXAw3GUJHtSKA2nMdicCg0RgUDBQWxyQazfKUNjqVayIikM6zfSMI6UIXPU2Ac2g0DIVKrf6wVqzYiyPjpUnh0hU57h5X8fSsPLRUmyO2aX2I1jBSX0VbowDF%2BSoYDA3SR2CrfSt6fsXedWD27GUIDH47u4-KeCAd%2BAZyU60mQP5-rGxKjkuAL5p2Y7QQqgqgo%2B9IZv%2B8rIceYGepCj6IreVq-rh8EYcasH4pSD71lSLLypRSYYpBYIUQh2rtixvpgnGe7sVBEHgZmwE4dRZIYJCSGyKCNZ-lhtIwJS76yKh0n8BUkDQg4EzEHAWAaFADp7CADSwlg0IpJUQgAFZQLOxBCAAFqI6B4NCeAINgGBtlgYDEDAHYEGQbp4GgFB4GACAmdpc4gPozzQAAsk4ABeuhKEoYAAPRwOIBAAAQABQAOq6AAdggTi5CoOUAHIACo5dS2UANw5UVxVys1AAecoAJQ5QAgtk2RKBQBUhQA0rojSZf6kgYPlY0ABI1XFAAyAg5UougANYUDlADiFAcFtTi9QAwnZXhOAAthQ6XEBg2WSOpMDiNSuUAMpgIoXi6NNxCzbIVggMdOBjQASrIoQoAZESmYZ1lmWpQg-KYTZvgI1LqX4Dmo75DiYwQfgxYwE6WQjwPk0o5OJeTV2iC4xVbcV5XFbIrPQK1WCwU49OM8zuSs8YtggOdl03TlAAKAAiABiktKC4XwlZAkAS04XiNGAeDDTlUtOBwLg3cVjQ5TL6tXWAjQq2Ag2bVwjS6E4xXpR1OBfE4ThfMNOAcBd10UDg2R0AAfkHqRCKL-uS7LOUAGq6BQuTeCr1u27o9uO87Yeh3QshVZbugAG67adm0UMbKeQDbQ3pwXTsuzgxVgBwSjBwI1d23Xzuu9kTct8HvhCNkogmRgxATggdAThweD3RQCAwAvBDgmAMB4OwXN4EBACOI8ENG0Jvh2HAOuFsIgRQBBYP6aBwGAFDENCM%2BVj40B7R7Xu7QAksVHDiMHfUqp7RWgAUXyj-RoFAlB5TBr1AAqgtKW%2B0vA2zsunSqxZcpS10F4Q6jQsBS3BDlQuKgAD6cBSG5WyGQihBBuqyBUGkZIIBGhMLaC4NhQhC6cJAPkToSQ2gdW0A4LoyRREUD8AAT1KH1H6yhZCyN0MoHKAAhNKR0FFyKUNVMAXhLr5CEIo5RcDirpycLEHKcV3qyBUR7La-Vja6G3i4CAQhbFOC2hbYqOUADySgEA5Xeo0SRw1ZCnWULoPAP0wlgCulEpRMS4lyMsZbByEdy6NBcF4aR6TjZZMkftJwjQ0HyAjtddOgSwDFUqlYsJTtnBpUYWUrJCcfDNJ%2Bt4aqidZB7V0bEp2RgQAHXVkrVxIAFpQOLg7Lgsgv5XWyM3VhQgVouBiGAHKp16lOFCcs1Zhh1kLSqQgXIP0HbFR%2BLstZlTqk5RMWY2Isg4rp0us4NAJtPo3Pet4XQ4cQAJWZsE7IJd1YqCLmMqxhTinp0ee9SW78oWlL%2BbCsGFBgq4N-rtD5lUvk-V%2BRCrFgTvl4thTin5sgJbKALszHKK0SpFMkUC2QXz3a7Qlj9Y2TKKAssCRwH62QlkgGZU4XacCv6cu5aKmlugvh2QFUKkVX9CVXUidswZ8rbmKvepIuJ2zZA1TAHZa6Yyaq6Buk0kAJqzVdNyDlMGRrBYWtwXgVZdkKAm1qUIWO3gEBVLGa1L4CASpfHNf6wN5zKqwVDUGyqcIzCXCAA%22%7D")
                .param("encnm", "100014616")
                .param("locale", "ko_KR")
                .param("url", "https%3A%2F%2Fwww.naver.com")
                .param("id", "")
                .param("pw", "")
                .build();

        int status = urlc.post(request);
        System.out.printf("---------------> %s%n", status);
        if(status >= 200 && status < 300) {
            System.out.println(" * Login Success");
        } else {
            System.err.println(" * Login Failure");
        }
    }

    @Override
    public String call() throws Exception {
        Stats stats = new Stats();

        /*
         * 순차적인 클릭
        */
        for (int j = 0; (clickCount < 0 ? true : j < clickCount); j++) {
            if(interval > 0) {
                try { Thread.sleep(interval); } catch(Exception ex) { }
            }
            //
            stats.incrementCall();
            HttpRequest request = HttpRequest.configure()
                    .url("https://search.naver.com/search.naver")
                    .param("sm", "top_hty")
                    .param("fbm", "0")
                    .param("ie", "utf8")
                    .param("query", "가짜뉴스아웃")
                    .build();

            int status = urlc.get(request);
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

        return String.format("[%s] Clicks=%d (success=%d, failure:%d)", name, stats.getCall(), stats.getSuccess(), stats.getFailure());
    }


    public class Stats {

        private long reqC = 0L;
        private long resS = 0L;
        private long resF = 0L;
        private String status = null;


        public synchronized long incrementCall() {
            return ++reqC;
        }

        public long getCall() {
            return reqC;
        }

        public long getSuccess() {
            return resS;
        }

        public void incrementSuccess() {
            ++resS;
        }

        public long getFailure() {
            return resF;
        }

        public void incrementFailure() {
            ++resF;
        }

    }

}

