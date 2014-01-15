package com.run.cheating.punch;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.run.cheating.utils.DateHelper;

public class PunchTask extends TimerTask {

  private static final Logger log = Logger.getLogger(PunchTask.class);
  private final PunchType punchType;
  private final Configuration config;
  private boolean needRetry = false;

  public PunchTask(final PunchType punchType, final Configuration config) {
    this.config = config;
    this.punchType = punchType;
  }

  @Override
  public void run() {
    final boolean runJob = runJob();
    log.debug("punch runJob:" + runJob);
    if (runJob) {
      runPunchAction();
    }
  }

  private boolean punch(final CloseableHttpClient closeableHttpClient) throws Exception {
    final PunchAction punchAction = new PunchAction(config);
    boolean result = false;
    if (punchAction.login(closeableHttpClient)) {
      punchAction.punch(closeableHttpClient, punchType.getValue());
      log.info("punch " + punchType + ",ok! Date:" + DateHelper.getLogDate());
      result = true;
      needRetry = false;
    }
    return result;
  }

  private void runPunchAction() {
    final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
    final CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
    try {
      final Random random = new Random();
      Thread.sleep(random.nextInt(config.getRandomTimeRange()));
      punch(closeableHttpClient);
    }
    catch (final java.net.SocketException e) {
      log.warn("May be connection reset, need retry.");
      needRetry = true;
      retry(punchType,closeableHttpClient);
    }
    catch (final Exception e) {
      log.error("punch error!", e);
    }
    finally {
      try {
        if (closeableHttpClient != null) {
          closeableHttpClient.close();
        }
      }
      catch (final IOException e) {
      }
    }
  }

  private void retry(final PunchType punchType,final CloseableHttpClient closeableHttpClient) {
    log.warn("Starting retry....");
    try {
      Thread.sleep(PunchManager.PERIOD_MIN);
    }
    catch (final InterruptedException e) {
    }
    final Date currentDate = new Date();
    if(needRetry){
      if(currentDate.before(getStopRetryDate(punchType))){
        try {
          if(!punch(closeableHttpClient)){
            retry(punchType, closeableHttpClient);
          }
        }
        catch (final Exception e) {
          retry(punchType, closeableHttpClient);
        }
      }
    }
  }

  private boolean runJob() {
    final boolean result = true;
    final Date currentDate = new Date();
    if (isVacationDay(currentDate)) {
      log.info("Today is vacation day, no need to " + punchType + ". Date:" + DateHelper.getLogDate());
      return false;
    }
    if (isWorkingDate(currentDate)) {
      return true;
    }
    if (isWeekend(currentDate)) {
      log.info("Today is weekend, no need to " + punchType + ". Date:" + DateHelper.getLogDate());
      return false;
    }
    return result;
  }

  private boolean isWorkingDate(final Date currentDate) {
    boolean result = false;
    final List<String> workingDays = config.getWorkingDays();
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    final String currentDay = sdf.format(currentDate);
    if (workingDays.contains(currentDay)) {
      result = true;
    }
    return result;
  }

  private boolean isVacationDay(final Date currentDate) {
    boolean result = false;
    final List<String> vacationDays = config.getVacationDays();
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    final String currentDay = sdf.format(currentDate);
    if (vacationDays.contains(currentDay)) {
      result = true;
    }
    return result;
  }

  private boolean isWeekend(final Date date) {
    final Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    final int day = cal.get(Calendar.DAY_OF_WEEK);
    if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
      return true;
    }
    else {
      return false;
    }
  }

  private Date getStopRetryDate(final PunchType punchType) {
    String time = "00:00";
    switch (punchType) {
    case PUNCH_IN_AM:
      time= Constants.STOP_RETRY_AM_IN;
      break;
    case PUNCH_OUT_AM:
      time= Constants.STOP_RETRY_AM_OUT;
      break;
    case PUNCH_IN_PM:
      time= Constants.STOP_RETRY_PM_IN;
      break;
    case PUNCH_OUT_PM:
      time= Constants.STOP_RETRY_PM_OUT;
      break;
    }
    final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    final Calendar configTime = Calendar.getInstance();
    try {
      configTime.setTime(sdf.parse(time));
    } catch (final ParseException e) {
      e.printStackTrace();
    }
    final Calendar currentDay = Calendar.getInstance();
    currentDay.set(Calendar.HOUR_OF_DAY, configTime.get(Calendar.HOUR_OF_DAY));
    currentDay.set(Calendar.MINUTE, configTime.get(Calendar.MINUTE));
    currentDay.set(Calendar.SECOND, configTime.get(Calendar.SECOND));
    final Date punchDate = currentDay.getTime();
    return punchDate;
  }
}
