package com.run.cheating.punch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import org.apache.log4j.Logger;

import com.run.cheating.utils.DateHelper;

public class PunchManager {

  public static final Logger log = Logger.getLogger(PunchManager.class);

  private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;
  private final Date punchInAMDate;
  private final Date punchOutAMDate;
  private final Date punchInPMDate;
  private final Date punchOutPMDate;
  private Timer punchInAMTimer;
  private Timer punchOutAMTimer;
  private Timer punchInPMTimer;
  private Timer punchOutPMTimer;
  private PunchTask punchInAMTask;
  private PunchTask punchOutAMTask;
  private PunchTask punchInPMTask;
  private PunchTask punchOutPMTask;
  private final Configuration config;

  public PunchManager() {
    config=ConfigProperties.getInstance().getConfiguration();
    punchInAMDate = getPunchDate(config.getPunchInAMTime());
    punchOutAMDate = getPunchDate(config.getPunchOutAMTime());
    punchInPMDate = getPunchDate(config.getPunchInPMTime());
    punchOutPMDate = getPunchDate(config.getPunchOutPMTime());
  }

  public void run() {
    punchInAMTimer = new Timer();
    punchOutAMTimer = new Timer();
    punchInPMTimer = new Timer();
    punchOutPMTimer = new Timer();
    punchInAMTask = new PunchTask(PunchType.PUNCH_IN_AM,config);
    punchOutAMTask = new PunchTask(PunchType.PUNCH_OUT_AM,config);
    punchInPMTask = new PunchTask(PunchType.PUNCH_IN_PM,config);
    punchOutPMTask = new PunchTask(PunchType.PUNCH_OUT_PM,config);
    log.info("The next punch in datetime(AM):"+DateHelper.formatDate(punchInAMDate));
    log.info("The next punch out datetime(AM):"+DateHelper.formatDate(punchOutAMDate));
    log.info("The next punch in datetime(PM):"+DateHelper.formatDate(punchInPMDate));
    log.info("The next punch out datetime(PM):"+DateHelper.formatDate(punchOutPMDate));
    punchInAMTimer.scheduleAtFixedRate(punchInAMTask, punchInAMDate, PERIOD_DAY);
    punchOutAMTimer.scheduleAtFixedRate(punchOutAMTask, punchOutAMDate, PERIOD_DAY);
    punchInPMTimer.scheduleAtFixedRate(punchInPMTask, punchInPMDate, PERIOD_DAY);
    punchOutPMTimer.scheduleAtFixedRate(punchOutPMTask, punchOutPMDate, PERIOD_DAY);
  }

  private Date addDay(final Date date, final int num) {
    final Calendar startDT = Calendar.getInstance();
    startDT.setTime(date);
    startDT.add(Calendar.DAY_OF_MONTH, num);
    return startDT.getTime();
  }

  public Date getPunchDate(final String time) {
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
    Date punchDate = currentDay.getTime();

    if (punchDate.before(new Date())) {
      punchDate = this.addDay(punchDate, 1);
    }
    return punchDate;
  }

}