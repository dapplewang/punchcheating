package com.run.cheating.punch;

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

  private void runPunchAction() {
    try {
      final Random random = new Random();
      Thread.sleep(random.nextInt(config.getRandomTimeRange()));
      final PunchAction punchAction = new PunchAction(config);
      final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
      final CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
      if(punchAction.login(closeableHttpClient)){
        punchAction.punch(closeableHttpClient, punchType.getValue());
        log.info("punch " + punchType + ",ok! Date:" + DateHelper.getLogDate());
      }
      closeableHttpClient.close();
    }
    catch (final Exception e) {
      log.error("punch error!", e);
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

}
