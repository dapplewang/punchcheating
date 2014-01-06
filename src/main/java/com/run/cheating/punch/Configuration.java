package com.run.cheating.punch;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
  private String userName;
  private String password;
  private String loginUrl;
  private String punchUrl;
  private String punchInAMTime;
  private String punchOutAMTime;
  private String punchInPMTime;
  private String punchOutPMTime;
  private List<String> workingDays;
  private List<String> vacationDays;
  private int randomTimeRange;
  private String checkLoginKeyWords;
  private String checkPunchKeyWords;

  public String getUserName() {
    return userName;
  }
  public void setUserName(final String userName) {
    this.userName = userName;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(final String password) {
    this.password = password;
  }
  public String getLoginUrl() {
    return loginUrl;
  }
  public void setLoginUrl(final String loginUrl) {
    this.loginUrl = loginUrl;
  }
  public String getPunchUrl() {
    return punchUrl;
  }
  public void setPunchUrl(final String punchUrl) {
    this.punchUrl = punchUrl;
  }
  public String getPunchInAMTime() {
    return punchInAMTime;
  }
  public void setPunchInAMTime(final String punchInAMTime) {
    this.punchInAMTime = punchInAMTime;
  }
  public String getPunchOutAMTime() {
    return punchOutAMTime;
  }
  public void setPunchOutAMTime(final String punchOutAMTime) {
    this.punchOutAMTime = punchOutAMTime;
  }
  public String getPunchInPMTime() {
    return punchInPMTime;
  }
  public void setPunchInPMTime(final String punchInPMTime) {
    this.punchInPMTime = punchInPMTime;
  }
  public String getPunchOutPMTime() {
    return punchOutPMTime;
  }
  public void setPunchOutPMTime(final String punchOutPMTime) {
    this.punchOutPMTime = punchOutPMTime;
  }
  public List<String> getWorkingDays() {
    if(workingDays==null){
      workingDays = new ArrayList<String>();
    }
    return workingDays;
  }

  public List<String> getVacationDays() {
    if(vacationDays==null){
      vacationDays = new ArrayList<String>();
    }
    return vacationDays;
  }

  public int getRandomTimeRange() {
    return randomTimeRange;
  }
  public void setRandomTimeRange(final int randomTimeRange) {
    this.randomTimeRange = randomTimeRange;
  }
  public String getCheckLoginKeyWords() {
    return checkLoginKeyWords;
  }
  public void setCheckLoginKeyWords(final String checkLoginKeyWords) {
    this.checkLoginKeyWords = checkLoginKeyWords;
  }
  public String getCheckPunchKeyWords() {
    return checkPunchKeyWords;
  }
  public void setCheckPunchKeyWords(final String checkPunchKeyWords) {
    this.checkPunchKeyWords = checkPunchKeyWords;
  }

}
