package com.testProject.validation;


import com.testProject.enums.VastRequestParameter;
import com.testProject.enums.VpaidMode;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VastEntity {
  
  private String sampleVast = "";
  private List<String> listOfRequests = new LinkedList<>();
  private VpaidMode vpaidMode;
  private boolean isFraudActive;
  private boolean isBrandActive;
  
  private boolean isVolumeEnabled;
  private boolean isVideoInTheView;
  
  public VastEntity(String sampleVast, VpaidMode vpaidMode, boolean isVolumeEnabled, boolean isVideoInTheView) {
    this.sampleVast = sampleVast;
    this.vpaidMode = vpaidMode;
    this.isVolumeEnabled = isVolumeEnabled;
    this.isVideoInTheView = isVideoInTheView;
  }
  
  public String getSampleVast() {
    return sampleVast;
  }
  
  public boolean isVolumeEnabled() {
    return isVolumeEnabled;
  }
  
  public boolean isVideoInTheView() {
    return isVideoInTheView;
  }
  
  public List<String> getListOfRequests() {
    return listOfRequests;
  }
  
  public VpaidMode getVpaidMode() {
    return vpaidMode;
  }
  
  public boolean isFraudActive() {
    return isFraudActive;
  }
  
  public boolean isBrandActive() {
    return isBrandActive;
  }
  
  public VastEntity setFraudActive(boolean fraudActive) {
    isFraudActive = fraudActive;
    return this;
  }
  
  public VastEntity setBrandActive(boolean brandActive) {
    isBrandActive = brandActive;
    return this;
  }
  
  public VastEntity setVpaidMode(VpaidMode vpaidMode) {
    this.vpaidMode = vpaidMode;
    return this;
  }
  
  public VastEntity setListOfRequests(List<String> list) {
    listOfRequests = list;
    return this;
  }
  
  private List<String> getVastEntityList() throws IndexOutOfBoundsException {
    return listOfRequests.subList(2, 6);
  }
  
  public int getAmountOfDuration() {
    int sizeOfList = 0;
    try {
      List<String> listOfDurations = getVastEntityList();
      sizeOfList = listOfDurations.size();
    } catch (IndexOutOfBoundsException e) {
    }
    return sizeOfList;
  }
  
  public boolean isVideoStarted() {
    return getAmountOfDuration() > 0;
  }
  
  public boolean areDurationRequestsOrdered() {
    boolean isOrdered = false;
    List<String> listOfDurations = getVastEntityList();
    Pattern pattern = Pattern.compile("\\&d=(\\d)");
    for (int i = 1; i < listOfDurations.size() + 1; i++) {
      for (String durationReq : listOfDurations) {
        Matcher matcher = pattern.matcher(durationReq);
        while (matcher.find()) {
          isOrdered = i == Integer.parseInt(matcher.group(1));
        }
      }
    }
    return isOrdered;
  }
  
  public int getParameterForQuartile(String quartile, VastRequestParameter parameter) {
    List<String> listOfDurations = getVastEntityList();
    String desiredRequest = listOfDurations.stream()
                                           .filter(request -> request.contains("&d=" + quartile))
                                           .findAny()
                                           .get();
    return getParameter(desiredRequest, parameter);
  }
  
  public int getParameterOfViewability() {
    int parameterState = -1;
    List<String> requestWithViewability = listOfRequests.subList(1, 2);
    Pattern pattern = Pattern.compile("\\&s=(\\d)");
    Matcher matcher = pattern.matcher(requestWithViewability.get(0));
    if (matcher.find()) {
      parameterState = Integer.parseInt(matcher.group(1));
    }
    return parameterState;
  }
  
  private int getParameter(String request, VastRequestParameter parameter) {
    int parameterState = -1;
    Pattern pattern = Pattern.compile(String.format("\\&%s=(\\d)", parameter.toString()));
    Matcher matcher = pattern.matcher(request);
    if (matcher.find()) {
      parameterState = Integer.parseInt(matcher.group(1));
    }
    return parameterState;
  }
  
  public String getIdOfVast(String sampleVast) {
    String result = "";
    Pattern pattern = Pattern.compile(".+id\\=(\\d+)");
    Matcher matcher = pattern.matcher(sampleVast);
    if (matcher.find()) {
      result = matcher.group(1);
    }
    return result;
  }
}
