package com.run.cheating.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class DateHelper {
  private static final Logger log = Logger.getLogger(DateHelper.class);
  public static String getLogDate(){
    return formatDate(new Date());
  }

  public static String formatDate(final java.util.Date date) {
    return formatDateByFormat(date, "yyyy-MM-dd HH:mm:ss");
  }

  public static String formatDateByFormat(final java.util.Date date, final String format) {
    String result = "";
    if (date != null) {
      try {
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        result = sdf.format(date);
      }
      catch (final Exception ex) {
        log.error(ex);
      }
    }
    return result;
  }

}
