package dok;

import java.io.InputStream;


public class ResourceUtil {

  public static final String SEPERATOR = "/";

  public static final ClassLoader[] CLASSLOADER = new ClassLoader[] {
      Thread.currentThread().getContextClassLoader(),
      ResourceUtil.class.getClassLoader(),
      ClassLoader.getSystemClassLoader() };

  public static InputStream getResourceAsStream(String resource) {

    for (ClassLoader cl : CLASSLOADER) {
      if(null != cl) {
        // try to find the resource as passed
        InputStream returnValue = cl.getResourceAsStream(resource);
        // now, some class loaders want this leading "/", so we'll add it and try again if we didn't find the resource
        if(null == returnValue) {
          returnValue = cl.getResourceAsStream(SEPERATOR + resource);
        }

        if(null != returnValue) {
          return returnValue;
        }
      }
    }
    return null;
  }
}
