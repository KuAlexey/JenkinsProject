package com.testProject.validation;

import com.testProject.enums.VpaidMode;

import java.util.ArrayList;
import java.util.List;

public class VastContainer {
  
  private List<VastEntity> vastEntityList = new ArrayList<>();
  
  public List<VastEntity> getVastEntityList() {
    return vastEntityList;
  }
  
  public void containVastEntity(VastEntity vastEntity) {
    vastEntityList.add(vastEntity);
  }
  
  public VastEntity getVastEntityByVpaid(VpaidMode vpaidMode) {
    return vastEntityList.stream()
                         .filter(entity -> entity.getVpaidMode().equals(vpaidMode))
                         .findAny()
                         .get();
  }
}
