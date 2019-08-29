package com.dok.common.support;


import com.dok.common.helper.DokObjectDeserializer;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
public class GsonUtil {

  private GsonUtil(){

  }


  public static String json2xml(String json) {
    String xml = null;
    if(json == null || json.isEmpty()) {
      return xml;
    } else {
      TypeReference<Object> typeRef = new TypeReference<Object>(){};
      try {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Object object = mapper.readValue(json, typeRef);
        //System.out.println(map);

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.setSerializationInclusion(Include.NON_NULL);
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xml = xmlMapper.writeValueAsString(object);
      } catch(Exception ex) {
        //System.err.println(ex.getMessage());
      }
      return xml;
    }
  }

  public static String xml2json(String xml) {
    String json = null;
    if(xml == null || xml.isEmpty()) {
      return json;
    } else {
      TypeReference<Object> typeRef = new TypeReference<Object>(){};
      try {
        //XmlMapper xmlMapper = new XmlMapper();
        //xmlMapper.setSerializationInclusion(Include.NON_NULL);
        DokObjectDeserializer uod = new DokObjectDeserializer();
        SimpleModule module = new SimpleModule().addDeserializer(Object.class, uod);
        XmlMapper xmlMapper = (XmlMapper) new XmlMapper().registerModule(module);
        Object object = xmlMapper.readValue(xml, typeRef);
        //System.out.println(object);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        json = mapper.writeValueAsString(object);
      } catch(Exception ex) {
        //System.err.println(ex.getMessage());
      }
      return json;
    }
  }

	public static void main(String[] args) throws Exception {
		String jsonData = "{\"IF_ID\":\"INF-001\", \"AccountInfoDto\":[ {\"IN_ACT_NO\":\"213042321234\", \"IN_ACT_NO1\":\"ABC\"}, {\"IN_ACT_NO\":\"213042321234\", \"IN_ACT_NO1\":\"123\"} ],\"IF_ID1\":\"INF1-001\"}";
		String xml = GsonUtil.json2xml(jsonData);
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
		String json = GsonUtil.xml2json(xmlData);
    System.out.println("result :: " + json);
	}

}
