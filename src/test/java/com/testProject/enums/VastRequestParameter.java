package com.testProject.enums;

public enum VastRequestParameter {
  VIEW("v"),
  AUDIBLE("a"),
  VIEW_PER_QARTILE("ve"),
  AUDIBLE_PER_QARTILE("ae");
  
  private String description;
  
  VastRequestParameter(String description) {
    this.description = description;
  }
  
  @Override
  public String toString() {
    return description;
  }
}
