package com.run.cheating.punch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesUtils {

  private static final Logger log = Logger.getLogger(PropertiesUtils.class);

  public static Properties getPropertiesFile() throws UnsupportedEncodingException, FileNotFoundException {
    final String projectPath = System.getProperty("user.dir");
    final InputStream in = new FileInputStream(projectPath+File.separator+Constants.CONFIG_PROPERTIES);
    final Properties prop = new Properties();
    try {
      prop.load(new InputStreamReader(in,"UTF-8"));
      in.close();
    }
    catch (final Exception ex) {
      log.error("Load properties error£ºconfig.properties",ex);
    }
    return prop;
  }

  public Properties getPropertiesFile(final String filePath) {
    final InputStreamReader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filePath));
    final Properties prop = new Properties();
    try {
      prop.load(reader);
      reader.close();
    }
    catch (final IOException ex) {
      log.error("Load properties error,path is" + filePath,ex);
    }
    return prop;
  }

  public static String null2Default(final String str,final String defaultValue){
    String result = defaultValue;
    if(str!=null&&!str.trim().equals("")){
      result = str;
    }
    return result;
  }

  public static int null2Default(final String str,final int defaultValue){
    int result = defaultValue;
    if(str!=null&&!str.trim().equals("")){
      result = Integer.parseInt(str);
    }
    return result;
  }


}
