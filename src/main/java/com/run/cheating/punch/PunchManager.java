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
  public static final long PERIOD_MIN = 60 * 1000;
  public static final long PERIOD_DAY = 24 * 60 * PERIOD_MIN;
  
  private  Date punchInAMDate;
  private  Date punchOutAMDate;
  private  Date punchInPMDate;
  private  Date punchOutPMDate;
  private Timer punchInAMTimer;
  private Timer punchOutAMTimer;
  private Timer punchInPMTimer;
  private Timer punchOutPMTimer;
  private PunchTask punchInAMTask;
  private PunchTask punchOutAMTask;
  private PunchTask punchInPMTask;
  private PunchTask punchOutPMTask;
  private Configuration config;

  public PunchManager() {
  }

  public void run() {
    punchInAMTimer = new Timer();
    punchOutAMTimer = new Timer();
    punchInPMTimer = new Timer();
    punchOutPMTimer = new Timer();
    ConfigProperties.getInstance().reloadConfiguration();
    config=ConfigProperties.getInstance().getConfiguration();
    punchInAMDate = getPunchDate(config.getPunchInAMTime());
    punchOutAMDate = getPunchDate(config.getPunchOutAMTime());
    punchInPMDate = getPunchDate(config.getPunchInPMTime());
    punchOutPMDate = getPunchDate(config.getPunchOutPMTime());
    punchInAMTask = new PunchTask(PunchType.PUNCH_IN_AM,config);
    punchOutAMTask = new PunchTask(PunchType.PUNCH_OUT_AM,config);
    punchInPMTask = new PunchTask(PunchType.PUNCH_IN_PM,config);
    punchOutPMTask = new PunchTask(PunchType.PUNCH_OUT_PM,config);
    log.info("The next punch in datetime(AM):"+DateHelper.formatDate(punchInAMDate));
    log.info("The next punch out datetime(AM):"+DateHelper.formatDate(punchOutAMDate));
    log.info("The next punch in datetime(PM):"+DateHelper.formatDate(punchInPMDate));
    log.info("The next punch out datetime(PM):"+DateHelper.formatDate(punchOutPMDate));
    try{
      punchInAMTimer.schedule(punchInAMTask, punchInAMDate, PERIOD_DAY);
      punchOutAMTimer.schedule(punchOutAMTask, punchOutAMDate, PERIOD_DAY);
      punchInPMTimer.schedule(punchInPMTask, punchInPMDate, PERIOD_DAY);
      punchOutPMTimer.schedule(punchOutPMTask, punchOutPMDate, PERIOD_DAY);
    }
    catch(final IllegalStateException e){}

  }

  public void stop(){
    punchInAMTimer.cancel();
    punchOutAMTimer.cancel();
    punchInPMTimer.cancel();
    punchOutPMTimer.cancel();
  }

  public void restart(){
    stop();
    run();
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
