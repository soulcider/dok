package com.dok.common.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


public class HttpRequest {

    private String              url;
    private List<NameValuePair> params;
    //private boolean                  allowCookie;


    public HttpRequest(String url, List<NameValuePair> params) {
        this.url = url;
        this.params = params;
        //this.allowCookie = allowCookie;
    }

    public String getUrl() {
        return url;
    }

    public List<NameValuePair> getParams() {
        return params;
    }

    public static HttpRequest.Builder configure() {
        return new HttpRequest.Builder();
    }


    public static class Builder {

        private String              url;
        private List<NameValuePair> params = new ArrayList<NameValuePair>();
        //private boolean             allowCookie = false;


        public Builder url(String url) {
            this.url = url;
            return this;
        }

//        public Builder cookie(boolean allowCookie) {
//            this.allowCookie = allowCookie;
//            return this;
//        }

        public Builder param(String name, String value) {
            this.params.add(new BasicNameValuePair(name, value));
            return this;
        }

        public Builder params(List<NameValuePair> paramList) {
            this.params.addAll(paramList);
            return this;
        }

        public HttpRequest build() {
            if(url == null || url.isEmpty()) {
                throw new RuntimeException("URL can not be 'NULL'");
            }
            return new HttpRequest(url, params);
        }

    }

}
