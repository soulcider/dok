package com.dok.common.support;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;


public class HttpConnector {

//    /** log4j */
//    private final Logger             log                = LoggerFactory.getLogger(this.getClass());

    private static final CookieStore BASIC_COOKIE_STORE        = new BasicCookieStore();;

    private static final String      USER_AGENT         = "WscClient/1.0";
    private static final String      CONTENT_TYPE       = "application/x-www-form-urlencoded";//"application/json; charset=UTF-8";
    private static final String      ACCEPT_LANGUAGE    = "ko-KR,en;q=0.5"; // "ko-KR,en;q=0.5",  "en-US,en;q=0.5"
    public static final int          CONNECTION_TIMEOUT = 30;
    public static final int          SOCKET_TIMEOUT     = 120;

    private String                   url;
    private List<NameValuePair>      params;
    private boolean                  allowCookie;


    public HttpConnector(String url, List<NameValuePair> params, boolean allowCookie) {
        this.url = url;
        this.params = params;
        this.allowCookie = allowCookie;
    }

    public int get() {

        // set HttpClient
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpContext httpContext = new BasicHttpContext();
            httpContext.setAttribute(HttpClientContext.COOKIE_STORE, BASIC_COOKIE_STORE);

            RequestConfig config = RequestConfig.custom()
                                                .setConnectTimeout(CONNECTION_TIMEOUT * 1000)
                                                .setConnectionRequestTimeout(SOCKET_TIMEOUT * 1000)
                                                .setSocketTimeout(SOCKET_TIMEOUT * 1000)
                                                .build();

            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent", USER_AGENT);
            httpGet.setHeader("Accept", "text/html;q=0.9,*/*;q=0.8");
            httpGet.setHeader("Accept-Language", "en-US,en;q=0.5");
            httpGet.setConfig(config);


            if(params != null && !params.isEmpty()) {
                URI uri = new URIBuilder(httpGet.getURI()).addParameters(params).build();
                httpGet.setURI(uri);
            }

            CloseableHttpResponse httpResponse = null;
            if(allowCookie) {
                httpResponse = httpClient.execute(httpGet, httpContext);
            } else {
                httpResponse = httpClient.execute(httpGet);
            }
            // HTTP status error
            int status = httpResponse.getStatusLine().getStatusCode();
            //System.out.println(" * http response status : " + status);
            //if (status >= 200 && status < 300) {
            //    //return statusLine.getStatusCode();
            //    HttpEntity entity = httpResponse.getEntity();
            //    return (entity == null) ? "" : EntityUtils.toString(entity);
            //} else {
            //    throw new RuntimeException("Response status: " + status);
            //}
            return status;
        } catch(Exception ex) {
            //throw new RuntimeException("URLConnector Post Error", ex);
            return 909;
        }
    }

    public int post() {

        // set HttpClient
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpContext httpContext = new BasicHttpContext();
            httpContext.setAttribute(HttpClientContext.COOKIE_STORE, BASIC_COOKIE_STORE);

            RequestConfig config = RequestConfig.custom()
                                                .setConnectTimeout(CONNECTION_TIMEOUT * 1000)
                                                .setConnectionRequestTimeout(SOCKET_TIMEOUT * 1000)
                                                .setSocketTimeout(SOCKET_TIMEOUT * 1000)
                                                .build();

            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("User-Agent", USER_AGENT);
            httpPost.setHeader("Content-Type", CONTENT_TYPE);
            httpPost.setHeader("Accept-Language", ACCEPT_LANGUAGE);
            httpPost.setConfig(config);

            if(params != null && !params.isEmpty()) {
                HttpEntity httpEntity = new UrlEncodedFormEntity(params);
                httpPost.setEntity(httpEntity);
            }

            CloseableHttpResponse httpResponse = null;
            if(allowCookie) {
                httpResponse = httpClient.execute(httpPost, httpContext);
            } else {
                httpResponse = httpClient.execute(httpPost);
            }
            // HTTP status error
            int status = httpResponse.getStatusLine().getStatusCode();
            //System.out.println(" * http response status : " + status);
            //if (status >= 200 && status < 300) {
            //    //return statusLine.getStatusCode();
            //    HttpEntity entity = httpResponse.getEntity();
            //    return (entity == null) ? "" : EntityUtils.toString(entity);
            //} else {
            //    throw new RuntimeException("Response status: " + status);
            //}
            return status;
        } catch(Exception ex) {
            //throw new RuntimeException("URLConnector Post Error", ex);
            return 909;
        }
    }

    public static HttpConnector.Builder configure() {
        return new HttpConnector.Builder();
    }


    public static class Builder {

        private String              url;
        private List<NameValuePair> params      = new ArrayList<NameValuePair>();
        private boolean             allowCookie = false;


        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder cookie(boolean allowCookie) {
            this.allowCookie = allowCookie;
            return this;
        }

        public Builder param(String name, String value) {
            this.params.add(new BasicNameValuePair(name, value));
            return this;
        }

        public Builder params(List<NameValuePair> paramList) {
            this.params.addAll(paramList);
            return this;
        }

        public HttpConnector build() {
            if(url == null || url.isEmpty()) {
                throw new RuntimeException("URL can not be 'NULL'");
            }
            return new HttpConnector(url, params, allowCookie);
        }

    }

}
