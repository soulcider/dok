package dok;

import java.io.InputStream;


public class ResourceUtil {

  public static ClassLoader[] classLoader = new ClassLoader[] {
      Thread.currentThread().getContextClassLoader(),
      ResourceUtil.class.getClassLoader(),
      ClassLoader.getSystemClassLoader() };

  public static InputStream getResourceAsStream(String resource) {

    for (ClassLoader cl : classLoader) {
      if(null != cl) {

        // try to find the resource as passed
        InputStream returnValue = cl.getResourceAsStream(resource);

        // now, some class loaders want this leading "/", so we'll add it and try again if we didn't find the resource
        if(null == returnValue) {
          returnValue = cl.getResourceAsStream("/" + resource);
        }

        if(null != returnValue) {
          return returnValue;
        }
      }
    }
    return null;
  }
}
