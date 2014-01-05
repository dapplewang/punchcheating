package com.run.cheating.punch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
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

  public void login(final CloseableHttpClient httpClient) throws Exception {
    final HttpPost postRequest = new HttpPost(config.getLoginUrl());
    postRequest.addHeader("http.useragent", "Custom Browser");
    //    httpClient.getParams().setParameter("http.useragent", "Custom Browser");
    //    httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
    final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
    nameValuePairs.add(new BasicNameValuePair("loginName", EscapeUnescape.escape(config.getUserName())));
    nameValuePairs.add(new BasicNameValuePair("password", config.getPassword()));
    final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
    entity.setContentType("application/x-www-form-urlencoded");
    postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    final HttpResponse response = httpClient.execute(postRequest);

    final BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

    String line = "";
    while ((line = reader.readLine()) != null) {
      log.debug(line);
    }
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

    final BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

    String line = "";
    while ((line = reader.readLine()) != null) {
      log.debug(line);
    }

  }

}
