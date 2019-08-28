package com.dok.common.support;

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
      if(cl != null) {
    	  URL url = cl.getResource(resource);
          if(url == null) {
        	  url = cl.getResource(SEPARATOR + resource);
          }

          if(url != null) {
          return url;
        }
      }
    }
    return null;
  }

  public static File getResourceAsFile(String resource) {
    for (ClassLoader cl : CLASSLOADER) {
      System.out.printf("Index : %s%n", (cl==null?"NULL":cl.getClass().getName()));
      if(null != cl) {
          URL url = cl.getResource(resource);
          if(url == null) {
        	  url = cl.getResource(SEPARATOR + resource);
          }

          if(url != null) {
          return new File(url.getFile());
        }
      }
    }
    return null;
  }

  public static InputStream getResourceAsStream(String resource) {
    for (ClassLoader cl : CLASSLOADER) {
      System.out.printf("Index : %s%n", (cl==null?"NULL":cl.getClass().getName()));
      if(cl != null) {
        InputStream in = cl.getResourceAsStream(resource);
        if(in == null) {
        	in = cl.getResourceAsStream(SEPARATOR + resource);
        }

        if(in != null) {
          return in;
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
