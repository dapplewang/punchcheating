package com.run.cheating.punch;

import org.apache.log4j.Logger;

public class Main {
  private static final Logger log = Logger.getLogger(ConfigProperties.class);
  /**
   * @param args
   */
  public static void main(final String[] args) {
    log.info("");
    log.info("****************************************************************");
    log.info(" Punch Cheating is runing, do not close this window, good luck!");
    log.info("****************************************************************");
    log.info("");
    final PunchManager t = new PunchManager();
    t.run();
  }

}
