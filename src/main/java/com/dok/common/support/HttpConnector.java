package com.dok.common.support;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;


public class HttpConnector {

//    /** log4j */
//    private final Logger             log                = LoggerFactory.getLogger(this.getClass());

  private static final CookieStore BASIC_COOKIE_STORE = new BasicCookieStore();;

  private static final String      USER_AGENT         = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0";
  private static final String      CONTENT_TYPE       = "application/x-www-form-urlencoded";//"application/json; charset=UTF-8";
  private static final String      ACCEPT_LANGUAGE    = "en,ko-KR;q=0.5"; // "en,ko-KR;q=0.5",  "en-US,en;q=0.5"
  private static final String      ACCEPT             = "text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8";
  private static final int         CONNECTION_TIMEOUT = 30;
  private static final int         SOCKET_TIMEOUT     = 120;

  private String                   contentType;
  private String                   acceptLanguage;
  private String                   accept;
  private int                      connectionTimeout;
  private int                      socketTimeout;


    private HttpConnector(String contentType, String acceptLanguage, String accept, int connectionTimeout, int socketTimeout) {
      this.contentType = contentType;
      this.acceptLanguage = acceptLanguage;
      this.accept = accept;
      this.connectionTimeout = connectionTimeout;
      this.socketTimeout = socketTimeout;
    }

    public int get(HttpRequest request) {

        // set HttpClient
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpContext httpContext = new BasicHttpContext();
            httpContext.setAttribute(HttpClientContext.COOKIE_STORE, BASIC_COOKIE_STORE);

            RequestConfig config = RequestConfig.custom()
                                                .setConnectTimeout(connectionTimeout * 1000)
                                                .setConnectionRequestTimeout(socketTimeout * 1000)
                                                .setSocketTimeout(socketTimeout * 1000)
                                                .build();

            HttpGet httpGet = new HttpGet(request.getUrl());
            httpGet.setHeader("User-Agent", USER_AGENT);
            httpGet.setHeader("Accept", accept);
            httpGet.setHeader("Accept-Language", acceptLanguage);
            httpGet.setConfig(config);


            if(request.getParams() != null && !request.getParams().isEmpty()) {
                URI uri = new URIBuilder(httpGet.getURI()).addParameters(request.getParams()).build();
                httpGet.setURI(uri);
                //System.out.println(" * URI : " + uri);
            }

            CloseableHttpResponse httpResponse = httpClient.execute(httpGet, httpContext);
            // HTTP status error
            int status = httpResponse.getStatusLine().getStatusCode();
            //System.out.println(" * http response status : " + status);
            /*
            if (status >= 200 && status < 300) {
                //return statusLine.getStatusCode();
                HttpEntity entity = httpResponse.getEntity();
                return (entity == null) ? "" : EntityUtils.toString(entity);
            } else {
                throw new RuntimeException("Response status: " + status);
            }
            */
            return status;
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
            //throw new RuntimeException("URLConnector Post Error", ex);
            return 909;
        }
    }

    public int post(HttpRequest request) {

        // set HttpClient
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpContext httpContext = new BasicHttpContext();
            httpContext.setAttribute(HttpClientContext.COOKIE_STORE, BASIC_COOKIE_STORE);

            RequestConfig config = RequestConfig.custom()
                                                .setConnectTimeout(connectionTimeout * 1000)
                                                .setConnectionRequestTimeout(socketTimeout * 1000)
                                                .setSocketTimeout(socketTimeout * 1000)
                                                .build();

            HttpPost httpPost = new HttpPost(request.getUrl());
            httpPost.setHeader("User-Agent", USER_AGENT);
            httpPost.setHeader("Content-Type", contentType);
            httpPost.setHeader("Accept", accept);
            httpPost.setHeader("Accept-Language", acceptLanguage);
            httpPost.setConfig(config);

            if(request.getParams() != null && !request.getParams().isEmpty()) {
                HttpEntity httpEntity = new UrlEncodedFormEntity(request.getParams());
                httpPost.setEntity(httpEntity);
                //System.out.println(" * QUERY : " + EntityUtils.toString(httpEntity));
            }

            CloseableHttpResponse httpResponse = httpClient.execute(httpPost, httpContext);
            // HTTP status error
            int status = httpResponse.getStatusLine().getStatusCode();
            //System.out.println(" * http response status : " + status);
            /*
            if (status >= 200 && status < 300) {
                //return statusLine.getStatusCode();
                HttpEntity entity = httpResponse.getEntity();
                return (entity == null) ? "" : EntityUtils.toString(entity);
            } else {
                throw new RuntimeException("Response status: " + status);
            }
            */
            return status;
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
            //throw new RuntimeException("URLConnector Post Error", ex);
            return 909;
        }
    }

    public static HttpConnector.Builder configure() {
        return new HttpConnector.Builder();
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

        public HttpConnector build() {
            //if(url == null || url.isEmpty()) {
            //    throw new RuntimeException("URL can not be 'NULL'");
            //}
            return new HttpConnector(contentType, acceptLanguage, accept, connectionTimeout, socketTimeout);
        }

    }

}
