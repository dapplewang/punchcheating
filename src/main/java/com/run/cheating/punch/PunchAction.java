package com.run.cheating.punch;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.run.cheating.utils.EscapeUnescape;

public class PunchAction {

  private static final Logger log = Logger.getLogger(PunchAction.class);
  private final Configuration config;

  public PunchAction(final Configuration config) {
    this.config = config;
  }

  public boolean login(final CloseableHttpClient httpClient) throws Exception {
    boolean result = false;
    final HttpPost postRequest = new HttpPost(config.getLoginUrl());
    postRequest.addHeader("http.useragent", "Custom Browser");
    //    httpClient.getParams().setParameter("http.useragent", "Custom Browser");
    //    httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
    final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    nameValuePairs.add(new BasicNameValuePair("loginName", EscapeUnescape.escape(config.getUserName())));
    nameValuePairs.add(new BasicNameValuePair("password", config.getPassword()));
    final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
    entity.setContentType("application/x-www-form-urlencoded");
    postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    final HttpResponse response = httpClient.execute(postRequest);
    String resopnseStr = "";
    if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
      resopnseStr = IOUtils.toString(new InputStreamReader(response.getEntity().getContent(),"UTF-8"));
      if(resopnseStr.toString().contains(config.getCheckLoginKeyWords())){
        result = true;
      }
      log.debug("Login response:"+resopnseStr);
    }

    if(result == false){
      log.warn("=======================================");
      log.warn("Login faild, server resonse:");
      log.warn(resopnseStr);
      log.warn("=======================================");
    }
    return result;
  }

  public void punch(final CloseableHttpClient httpClient, final String type) throws Exception {
    final HttpPost postRequest = new HttpPost(config.getPunchUrl());
    //httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
    final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
    nameValuePairs.add(new BasicNameValuePair("punchType", type));
    final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
    entity.setContentType("application/x-www-form-urlencoded");
    postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    final HttpResponse response = httpClient.execute(postRequest);
    String resopnseStr = "";
    if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
      resopnseStr = IOUtils.toString(new InputStreamReader(response.getEntity().getContent(),"UTF-8"));
    }
    else{
      log.warn("=======================================");
      log.warn("punch faild!");
      log.warn("=======================================");
    }
    if(resopnseStr.contains(config.getCheckPunchKeyWords())){
      log.debug("Punch response:"+resopnseStr);
    }
    else{
      log.warn("=======================================");
      log.warn("Punch error,server response:"+resopnseStr);
      log.warn("=======================================");
    }

  }

}
