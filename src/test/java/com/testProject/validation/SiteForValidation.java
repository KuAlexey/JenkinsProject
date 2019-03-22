package com.testProject.validation;

import com.testProject.base.BasePage;
import com.testProject.enums.VpaidMode;
import com.utils.DriverHelper;
import com.utils.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;


import java.util.List;

public class SiteForValidation extends BasePage {
  
  @FindBy(css = "input#googima")
  private WebElement googleIMA;
  @FindBy(css = "textarea.custom")
  private WebElement textArea;
  @FindBy(css = "input.apply-btn")
  private WebElement applyButton;
  @FindBy(css = "div#player")
  private WebElement player;
  @FindBy(xpath = "//div[contains(@class,'volume') and @role='button']")
  private List<WebElement> volumeButtonList;
  
  public SiteForValidation clickVpaidMode(VpaidMode vpaidModeId) {
    h.waitAndClickWebElement(By.cssSelector("input#vpaid-" + vpaidModeId.toString()));
    return this;
  }
  
  public SiteForValidation clickPlayerVersion(String player) {
    h.waitAndClickWebElement(By.cssSelector("input#" + player));
    return this;
  }
  
  public SiteForValidation clickToPlayVAST() {
    h.waitAndClickWebElement(applyButton);
    Utils.delay(0, 3);
    h.waitAndClickWebElement(player);
    return this;
  }
  
  public SiteForValidation enableVolumeInVast(boolean enableVolume) {
    new Actions(DriverHelper.getDriver()).moveToElement(player).perform();
    WebElement relevantVolumeButton = volumeButtonList.stream()
                                                      .filter(WebElement::isDisplayed)
                                                      .findAny()
                                                      .get();
    String stateOfMuteButton = relevantVolumeButton.getAttribute("aria-label");
    switch (stateOfMuteButton) {
      case "Mute":
        if (!enableVolume) {
          h.waitAndClickWebElement(relevantVolumeButton);
        }
        break;
      case "Unmute":
        if (enableVolume) {
          h.waitAndClickWebElement(relevantVolumeButton);
        }
        break;
    }
    return this;
  }
  
  public SiteForValidation clickGoogleIma() {
    h.waitAndClickWebElement(googleIMA);
    return this;
  }
  
  public SiteForValidation inputTextArea(String vastSample) {
    h.clearAndTypeIntoInputField(textArea, vastSample);
    return this;
  }
  
  public SiteForValidation stayInTheView(boolean isVideoInTheView) {
    WebElement footerElement = h.waitForPresence(By.cssSelector("div[class*=footer-links]"));
    if (isVideoInTheView) {
      h.scrollToElementJS(player);
    } else {
      h.scrollToElementJS(footerElement);
    }
    return this;
  }

}
