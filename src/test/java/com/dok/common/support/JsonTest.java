package com.dok.common.support;

public class JsonTest {

  
  public static void main(String[] args) throws Exception {
  	String jsonData = "{\"IF_ID\":\"INF-001\", \"AccountInfoDto\":[ {\"IN_ACT_NO\":\"213042321234\", \"IN_ACT_NO1\":\"ABC\"}, {\"IN_ACT_NO\":\"213042321234\", \"IN_ACT_NO1\":\"123\"} ],\"IF_ID1\":\"INF1-001\"}";
  	String xml = JsonUtil.json2xml(jsonData);
  	System.out.println("result :: " + xml);
  
  	String xmlData = "<root>"
  	               + "  <IF_ID>INF-001</IF_ID>"
  	               + "  <AccountInfoDto>"
  	               + "    <IN_ACT_NO>213042321234</IN_ACT_NO>"
  	               + "    <IN_ACT_NO1>ABC</IN_ACT_NO1>"
  	               + "  </AccountInfoDto>"
  	               + "  <AccountInfoDto>"
  	               + "    <IN_ACT_NO>213042321234</IN_ACT_NO>"
  	               + "    <IN_ACT_NO1>123</IN_ACT_NO1>"
  	               + "  </AccountInfoDto>"
  	               + "  <IF_ID1>INF1-001</IF_ID1>"
  	               + "</root>";
  	String json = JsonUtil.xml2json(xmlData);
    System.out.println("result :: " + json);
  }
  
}

