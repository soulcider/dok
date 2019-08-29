package com.dok.common.http;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;


public class Connector {

//    /** log4j */
//    private final Logger             log                = LoggerFactory.getLogger(this.getClass());

    public static final CookieStore BASIC_COOKIE_STORE = new BasicCookieStore();;

    private static final String     USER_AGENT         = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0";
    private static final String     CONTENT_TYPE       = "application/x-www-form-urlencoded";  //"application/json; charset=UTF-8";
    private static final String     ACCEPT_LANGUAGE    = "en,ko-KR;q=0.5";                     // "en,ko-KR;q=0.5",  "en-US,en;q=0.5"
    private static final String     ACCEPT             = "text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8";
    private static final int        CONNECTION_TIMEOUT = 30;
    private static final int        SOCKET_TIMEOUT     = 120;

    private String                  contentType;
    private String                  acceptLanguage;
    private String                  accept;
    private int                     connectionTimeout;
    private int                     socketTimeout;


    public Connector() {
        this.contentType = CONTENT_TYPE;
        this.acceptLanguage = ACCEPT_LANGUAGE;
        this.accept = ACCEPT;
        this.connectionTimeout = CONNECTION_TIMEOUT;
        this.socketTimeout = SOCKET_TIMEOUT;
    }

    public Connector(String contentType, String acceptLanguage, String accept, int connectionTimeout, int socketTimeout) {
        this.contentType = contentType;
        this.acceptLanguage = acceptLanguage;
        this.accept = accept;
        this.connectionTimeout = connectionTimeout;
        this.socketTimeout = socketTimeout;
    }

    public String getUserAgent() {
        return USER_AGENT;
    }

    public String getContentType() {
        return contentType;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public String getAccept() {
        return accept;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public static Connector.Builder custom() {
        return new Connector.Builder();
    }


    public static class Builder {

        private String contentType       = CONTENT_TYPE;
        private String acceptLanguage    = ACCEPT_LANGUAGE;
        private String accept            = ACCEPT;
        private int    connectionTimeout = CONNECTION_TIMEOUT;
        private int    socketTimeout     = SOCKET_TIMEOUT;


        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder acceptLanguage(String acceptLanguage) {
            this.acceptLanguage = acceptLanguage;
            return this;
        }

        public Builder accept(String accept) {
            this.accept = accept;
            return this;
        }

        public Builder connectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder socketTimeout(int socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        public Connector build() {
            //if(url == null || url.isEmpty()) {
            //    throw new RuntimeException("URL can not be 'NULL'");
            //}
            return new Connector(contentType, acceptLanguage, accept, connectionTimeout, socketTimeout);
        }

    }

}
