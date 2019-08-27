package dok;

import java.io.File;
import java.io.InputStream;
import java.net.URL;



public class ResourceUtil {

  public static final String SEPARATOR = "/";

  public static final ClassLoader[] CLASSLOADER = new ClassLoader[] {
      Thread.currentThread().getContextClassLoader(),
      ResourceUtil.class.getClassLoader(),
      ClassLoader.getSystemClassLoader() };

  public static URL getResource(String resource) {

    for (ClassLoader cl : CLASSLOADER) {
      System.out.printf("Index : %s%n", (cl==null?"NULL":cl.getClass().getName()));
      if(null != cl) {
        // try to find the resource as passed
        URL returnValue = cl.getResource(resource);
        // now, some class loaders want this leading "/", so we'll add it and try again if we didn't find the resource
        if(null == returnValue) {
          returnValue = cl.getResource(SEPARATOR + resource);
        }

        if(null != returnValue) {
          return returnValue;
        }
      }
    }
    return null;
  }

  public static File getResourceAsFile(String resource) {
    for (ClassLoader cl : CLASSLOADER) {
      System.out.printf("Index : %s%n", (cl==null?"NULL":cl.getClass().getName()));
      if(null != cl) {
        // try to find the resource as passed
        URL returnValue = cl.getResource(resource);
        // now, some class loaders want this leading "/", so we'll add it and try again if we didn't find the resource
        if(null == returnValue) {
          returnValue = cl.getResource(SEPARATOR + resource);
        }

        if(null != returnValue) {
          return new File(returnValue.getFile());
        }
      }
    }
    return null;
  }

  public static InputStream getResourceAsStream(String resource) {

    for (ClassLoader cl : CLASSLOADER) {
      System.out.printf("Index : %s%n", (cl==null?"NULL":cl.getClass().getName()));
      if(null != cl) {
        // try to find the resource as passed
        InputStream returnValue = cl.getResourceAsStream(resource);
        // now, some class loaders want this leading "/", so we'll add it and try again if we didn't find the resource
        if(null == returnValue) {
          returnValue = cl.getResourceAsStream(SEPARATOR + resource);
        }

        if(null != returnValue) {
          return returnValue;
        }
      }
    }
    return null;
  }


  public static String getRuntimeHomeDirectory() {
    String homeDirectory = ResourceUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    if(homeDirectory.endsWith(".jar")) {
      // run in /lib/*.jar
      homeDirectory = substringToLast(homeDirectory, SEPARATOR);
      homeDirectory = substringToLast(homeDirectory, SEPARATOR);
    } else if(homeDirectory.endsWith("/")) {
      // run in /classes directory
      homeDirectory = substringToLast(homeDirectory, SEPARATOR);
    } else {
      // run in other directory
      // do nothing
    }
    return homeDirectory;
  }

  private static String substringToLast(String str, String separator) {
    if(str == null || str.isEmpty() || separator == null || separator.isEmpty()) {
      return str;
    }
    int pos = str.lastIndexOf(separator);
    if(pos == -1) {
      return str;
    }
    return str.substring(0, pos);
  }

}
