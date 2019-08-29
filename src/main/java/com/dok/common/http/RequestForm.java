package com.dok.common.http;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;


public class RequestForm implements Request {

  private String              url;
  private Method              method;
  private boolean             ping;

  private Connector           urlc;
  private List<NameValuePair> params;

  private String              responseBody;


  private RequestForm(String url, Method method, boolean ping, Connector urlc, List<NameValuePair> params) {
    this.url = url;
    this.method = method;
    this.ping = ping;
    this.urlc = urlc;
    this.params = params;
  }

  @Override
  public int execute() {
    int rtval;
    switch (method) {
      case GET:
        rtval = this.get();
        break;
      case POST:
          rtval = this.post();
          break;
      case PUT:
          rtval = this.put();
          break;
      case DELETE:
          rtval = this.delete();
          break;
      default:
          rtval = 0;
    }
    return rtval;
  }

private int get() {

    // set HttpClient
    try(CloseableHttpClient httpClient = HttpClients.createDefault()) {

      HttpContext httpContext = new BasicHttpContext();
      httpContext.setAttribute(HttpClientContext.COOKIE_STORE, Connector.BASIC_COOKIE_STORE);

      RequestConfig config = RequestConfig.custom()
                                          .setConnectTimeout(urlc.getConnectionTimeout() * 1000)
                                          .setConnectionRequestTimeout(urlc.getSocketTimeout() * 1000)
                                          .setSocketTimeout(urlc.getSocketTimeout() * 1000)
                                          .build();

      HttpGet httpGet = new HttpGet(url);
      httpGet.setHeader("User-Agent", urlc.getUserAgent());
      httpGet.setHeader("Accept", urlc.getAccept());
      httpGet.setHeader("Accept-Language", urlc.getAcceptLanguage());
      httpGet.setConfig(config);

      if(params != null && params.size() > 0) {
        URI uri = new URIBuilder(httpGet.getURI()).addParameters(params).build();
        httpGet.setURI(uri);
        //System.out.println(" * URI : " + uri);
      }

      CloseableHttpResponse httpResponse = httpClient.execute(httpGet, httpContext);
      // HTTP status error
      int status = httpResponse.getStatusLine().getStatusCode();
      //System.out.println(" * http response status : " + status);
      if(ping) {
        this.responseBody = null;
      } else {
        if(status >= 200 && status < 300) {
          HttpEntity entity = httpResponse.getEntity();
          this.responseBody = (entity == null) ? "" : EntityUtils.toString(entity);
        } else {
          this.responseBody = null;
        }
      }
      return status;
    } catch(Exception ex) {
      System.err.println(ex.getMessage());
      //throw new RuntimeException("URLConnector Post Error", ex);
      return 909;
    }
  }

  private int post() {

    // set HttpClient
    try(CloseableHttpClient httpClient = HttpClients.createDefault()) {

      HttpContext httpContext = new BasicHttpContext();
      httpContext.setAttribute(HttpClientContext.COOKIE_STORE, Connector.BASIC_COOKIE_STORE);

      RequestConfig config = RequestConfig.custom()
                                          .setConnectTimeout(urlc.getConnectionTimeout() * 1000)
                                          .setConnectionRequestTimeout(urlc.getSocketTimeout() * 1000)
                                          .setSocketTimeout(urlc.getSocketTimeout() * 1000)
                                          .build();

      HttpPost httpPost = new HttpPost(url);
      httpPost.setHeader("User-Agent", urlc.getUserAgent());
      httpPost.setHeader("Content-Type", urlc.getContentType());
      httpPost.setHeader("Accept", urlc.getAccept());
      httpPost.setHeader("Accept-Language", urlc.getAcceptLanguage());
      httpPost.setConfig(config);

      if(params != null && params.size() > 0) {
        HttpEntity httpEntity = new UrlEncodedFormEntity(params);
        httpPost.setEntity(httpEntity);
        //System.out.println(" * QUERY : " + EntityUtils.toString(httpEntity));
      }

      CloseableHttpResponse httpResponse = httpClient.execute(httpPost, httpContext);
      // HTTP status error
      int status = httpResponse.getStatusLine().getStatusCode();
      //System.out.println(" * http response status : " + status);
      if(ping) {
        this.responseBody = null;
      } else {
        if(status >= 200 && status < 300) {
          HttpEntity entity = httpResponse.getEntity();
          this.responseBody = (entity == null) ? "" : EntityUtils.toString(entity);
        } else {
          this.responseBody = null;
        }
      }
      return status;
    } catch(Exception ex) {
      System.err.println(ex.getMessage());
      //throw new RuntimeException("URLConnector Post Error", ex);
      return 909;
    }
  }

    private int put() {
        // TODO Auto-generated method stub
        return 0;
    }

    private int delete() {
        // TODO Auto-generated method stub
        return 0;
    }

//  public String getUrl() {
//      return url;
//  }
//
//  public Method getMethod() {
//      return method;
//  }
//
//  public boolean isPing() {
//      return ping;
//  }
//
//  public HttpConnector getConnector() {
//      return urlc;
//  }
//
//  public List<NameValuePair> getParams() {
//      return params;
//  }

  @Override
  public String getResponseBody() {
    return responseBody;
  }

  public static RequestForm.Builder configure() {
    return new RequestForm.Builder();
  }


  public static class Builder {

    private String              url    = null;
    private Method              method = null;
    private boolean             ping   = false;
    private Connector           urlc   = null;
    private List<NameValuePair> params = new ArrayList<NameValuePair>();


    public Builder url(String url) {
      this.url = url;
      return this;
    }

    public Builder method(Method method) {
      this.method = method;
      return this;
    }

    public Builder ping(boolean ping) {
      this.ping = ping;
      return this;
    }

    public Builder connector(Connector urlc) {
      this.urlc = urlc;
      return this;
    }

    public Builder param(String name, String value) {
      this.params.add(new BasicNameValuePair(name, value));
      return this;
    }

    public Builder params(List<NameValuePair> paramList) {
      if(paramList != null && paramList.size() > 0) {
        this.params.addAll(paramList);
      }
      return this;
    }

    public Builder params(Map<String, String> paramMap) {
      if(paramMap != null && paramMap.size() > 0) {
        paramMap.forEach((k, v) -> {
          if(k != null && !k.isEmpty()) {
            this.params.add(new BasicNameValuePair(k, v));
          }
        });
      }
      return this;
    }

    public RequestForm build() {
      if(url == null || url.isEmpty()) {
        throw new IllegalArgumentException("URL can not be 'NULL'");
      }
      if(method == null) {
        throw new IllegalArgumentException("Method can not be 'NULL'");
      }
      if(urlc == null) {
        urlc = Connector.custom()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .accept("text/html, application/xhtml+xml, application/xml, application/json;q=0.9, */*;q=0.8")
                .acceptLanguage("en,ko-KR;q=0.5")
                .connectionTimeout(30)
                .socketTimeout(60)
                .build();

      }
      return new RequestForm(url, method, ping, urlc, params);
    }

  }

}
