package com.run.cheating.punch;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.apache.log4j.Logger;

public class Main {
  private static final Logger log = Logger.getLogger(ConfigProperties.class);
  /**
   * @param args
   * @throws Exception
   */
  public static void main(final String[] args) throws Exception {
    printWelcomeInfo();
    final PunchManager t = new PunchManager();
    t.run();
    restartWhenConfigFileChanged(t);
  }

  private static void printWelcomeInfo(){
    log.debug("Start punch cheating V1.1 .....");
    log.info("");
    log.info("****************************************************************");
    log.info(" Punch Cheating is runing, do not close this window, good luck!");
    log.info("****************************************************************");
    log.info("");
  }

  private static void restartWhenConfigFileChanged(final PunchManager t) throws IOException, InterruptedException{
    final String watchDir = PropertiesUtils.getPropertiesFileDir();
    final WatchService watchService=FileSystems.getDefault().newWatchService();
    Paths.get(watchDir).register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
    while(true){
      final WatchKey key=watchService.take();
      for(final WatchEvent<?> event:key.pollEvents()){
        final String configFileName = PropertiesUtils.getPropertiesFileName();
        final String watchFileName = event.context().toString();
        if(configFileName.equals(watchFileName)){
          t.restart();
        }
      }
      final boolean valid=key.reset();
      if(!valid){
        break;
      }
    }
  }
}
