package com.run.cheating.punch;

import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigProperties {
  private static final Logger log = Logger.getLogger(ConfigProperties.class);
  private volatile static ConfigProperties instance;
  private Configuration conf;
  private ConfigProperties() {
    createConfiguration();
  }
  public static ConfigProperties getInstance() {
    if (instance == null) {
      synchronized (ConfigProperties.class) {
        if (instance == null) {
          instance = new ConfigProperties();
        }
      }
    }
    return instance;
  }
  public Configuration getConfiguration(){
    return conf;
  }

  private void createConfiguration(){
    conf = new Configuration();
    try {
      final Properties prop = PropertiesUtils.getPropertiesFile();
      conf.setUserName(prop.getProperty("username"));
      conf.setPassword(prop.getProperty("password"));
      conf.setLoginUrl(PropertiesUtils.null2Default(prop.getProperty("loginUrl"),Constants.LOGIN_URL));
      conf.setPunchUrl(PropertiesUtils.null2Default(prop.getProperty("punchUrl"),Constants.PUNCH_URL));
      conf.setPunchInAMTime(PropertiesUtils.null2Default(prop.getProperty("punchInAMTime"),Constants.AM_IN));
      conf.setPunchInPMTime(PropertiesUtils.null2Default(prop.getProperty("punchInPMTime"),Constants.PM_IN));
      conf.setPunchOutAMTime(PropertiesUtils.null2Default(prop.getProperty("punchOutAMTime"),Constants.AM_OUT));
      conf.setPunchOutPMTime(PropertiesUtils.null2Default(prop.getProperty("punchOutPMTime"),Constants.PM_OUT));
      conf.setRandomTimeRange(PropertiesUtils.null2Default(prop.getProperty("randomTimeRange"),Constants.RANDOM_TIME_RANGE));
      final String strWorkingDay = PropertiesUtils.null2Default(prop.getProperty("workingDays"),"");
      if(!strWorkingDay.equals("")){
        for(final String day:strWorkingDay.split(",")){
          conf.getWorkingDays().add(day);
        }
      }
      final String strVacationDays = PropertiesUtils.null2Default(prop.getProperty("vacationDays"),"");
      if(!strVacationDays.equals("")){
        for(final String day:strVacationDays.split(",")){
          conf.getVacationDays().add(day);
        }
      }
    }
    catch (final Exception e) {
      log.error(e);
    }
  }
  /**
   * @param args
   */
  public static void main(final String[] args) {
    // TODO Auto-generated method stub

  }

}
