package com.testProject.enums;

public enum VpaidMode {
  
  ENABLED("enabled"),
  DISABLED("disabled"),
  INSECURE("insecure");
  
  private String description;
  
  VpaidMode(String description) {
    this.description = description;
  }
  
  @Override
  public String toString() {
    return description;
  }
}

