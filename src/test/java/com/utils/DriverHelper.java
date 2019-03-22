package com.utils;

import com.testProject.base.BaseTest;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.IntStream;

public class DriverHelper implements SearchContext {
  
  public static final int TIMEOUT_DEFAULT = 15;
  public static final int TIMEOUT_EXTRA_LONG = 60;
  public static final int TIMEOUT_LONG = 30;
  public static final int TIMEOUT_SHORT = 5;
  public static final int TIMEOUT_MINIMAL = 2;
  
  public static final String HOME_URL = System.getProperty("home.page.url"); // staging site
  public static final String TARGET_SITE_URL = "http://www.abbawabba.com/";
  private static final String MENU_CSS_SELECTOR = "div[role='presentation']>div[role='menu']";
  public static final String JS_HOVER_SCRIPT = "var evObj = document.createEvent('MouseEvents');" +
          "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);" +
          "arguments[0].dispatchEvent(evObj);";
  
  private SearchContext context;
  private WebDriverWait wait;
  private JavascriptExecutor js;
  
  public DriverHelper(SearchContext context, int timeoutSec) {
    this.context = context;
    this.wait = new WebDriverWait(getDriver(), timeoutSec);
    js = (JavascriptExecutor) getDriver();
  }
  
  public DriverHelper() {
    this(TIMEOUT_DEFAULT);
  }
  
  public DriverHelper(int timeoutSec) {
    this(getDriver(), timeoutSec);
  }
  
  @Override
  public List<WebElement> findElements(By by) {
    return context.findElements(by);
  }
  
  @Override
  public WebElement findElement(By by) {
    return context.findElement(by);
  }
  
  public static WebDriver getDriver() {
    return BaseTest.getDriver();
  }
  
  public WebDriverWait getWait() {
    return wait;
  }
  
  public static void waitForPageLoad() {
    WebDriverWait w = new WebDriverWait(getDriver(), TIMEOUT_LONG);
    w.until(driver -> String
            .valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
            .equals("complete"));
  }
  
  @Step("Open URL - {url}")
  public static void openUrl(String url) {
    getDriver().get(url);
    waitForPageLoad();
  }
  
  @Step("Open page - {url}")
  public static void openPage(String url) {
    openUrl(HOME_URL + url);
  }
  
  @Step
  public static void openHomePage() {
    openUrl(HOME_URL);
  }
  
  @Step
  public static void refreshPage() {
    getDriver().navigate().refresh();
    waitForPageLoad();
  }
  
  public static <T extends Function<? super WebDriver, V>, V> V waitForResult(T input,
                                                                              int secondsToTry) {
    WebDriverWait w = new WebDriverWait(getDriver(), secondsToTry);
    w.ignoring(StaleElementReferenceException.class, NoSuchElementException.class);
    return w.until(driver -> input.apply(driver));
  }
  
  public boolean isElementPresent(By locator) {
    try {
      WebElement element = getDriver().findElement(locator);
      element.getTagName();
      return true;
    } catch (WebDriverException ignored) {
      return false;
    }
  }
  
  public boolean isElementPresent(WebElement element) {
    try {
      element.getTagName();
      return true;
    } catch (WebDriverException ignored) {
      return false;
    }
  }
  
  public boolean isElementDisplayed(WebElement element) {
    try {
      return waitForVisibility(element).isDisplayed();
    } catch (WebDriverException ignored) {
      return false;
    }
  }
  
  public boolean isElementDisplayed(By locator) {
    try {
      return waitForVisibility(locator).isDisplayed();
    } catch (WebDriverException ignored) {
      return false;
    }
  }
  
  public WebElement waitForPresence(By locator) {
    return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
  }
  
  public WebElement waitForPresence(WebElement element) {
    wait.until(driver -> isElementPresent(element));
    return element;
  }
  
  public void waitForAbsence(WebElement element) {
    wait.until(driver -> !isElementPresent(element));
  }
  
  public void waitForAbsence(By locator) {
    wait.until(driver -> !isElementPresent(locator));
  }
  
  public void waitForSelected(WebElement element) {
    wait.until(driver -> element.isSelected());
  }
  
  public void waitForNotSelected(WebElement element) {
    wait.until(driver -> !element.isSelected());
  }
  
  public void waitForEnabled(WebElement element) {
    wait.until(driver -> element.isEnabled());
  }
  
  public void waitForNotEnabled(WebElement element) {
    wait.until(driver -> !element.isEnabled());
  }
  
  public WebElement waitForVisibility(WebElement element) {
    return wait.until(ExpectedConditions.visibilityOf(element));
  }
  
  public WebElement waitForVisibility(By locator) {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
  }
  
  public void waitForNumberOfElementsToBe(By locator, int number) {
    wait.until(ExpectedConditions.numberOfElementsToBe(locator, number));
  }
  
  public WebElement waitToBeClickable(WebElement element) {
    return wait.until(ExpectedConditions.elementToBeClickable(element));
  }
  
  public WebElement waitToBeClickable(By locator) {
    return wait.until(ExpectedConditions.elementToBeClickable(locator));
  }
  
  public WebElement waitAndClickWebElement(WebElement element) {
    waitToBeClickable(element).click();
    return element;
  }
  
  public WebElement waitAndClickWebElement(By locator) {
    WebElement element = waitToBeClickable(locator);
    element.click();
    return element;
  }
  
  public WebElement scrollToElementJS(WebElement element) {
    js.executeScript("arguments[0].scrollIntoView(true);", element);
    return element;
  }
  
  public WebElement hoverJS(WebElement element) {
    waitForPresence(element);
    js.executeScript(JS_HOVER_SCRIPT, element);
    return element;
  }
  
  public WebElement clickJS(WebElement element) {
    waitToBeClickable(element);
    js.executeScript("arguments[0].click();", element);
    return element;
  }
  
  public WebElement clearAndTypeIntoInputField(WebElement inputElement, String value) {
    clearInputField(inputElement);
    if (value.length() > 0) {
      inputElement.sendKeys(value);
    }
    return inputElement;
  }
  
  public WebElement clearInputField(WebElement element) {
    waitAndClickWebElement(element);
    IntStream.range(0, 10).forEach(value -> {
      element.sendKeys(Keys.DELETE);
      element.sendKeys(Keys.BACK_SPACE);
    });
    element.clear();
    element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
    return element;
  }
  
  public void waitForSuccessMessageDisappear() {
    waitForPresence(By.xpath("//div/span[contains(text(), 'has been successfully')]"));
    waitForAbsence(By.xpath("//div/span[contains(text(), 'has been successfully')]"));
  }
  
  //todo - check if needed, remove if not
  public void waitForHiddenOverlay() {
    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div[style*='position: fixed']")));
  }
  
  /**
   * Waits for appearing the pop-up menu
   *
   * @return the appeared menu
   */
  public WebElement waitForMenuToAppear() {
    return waitForVisibility(By.cssSelector(MENU_CSS_SELECTOR));
  }
  
  /**
   * Waits for disappearing the pop-up menu
   */
  public void waitForMenuToDisappear() {
    waitForAbsence(By.cssSelector(MENU_CSS_SELECTOR));
  }
  
  public void selectOptionFromDropdownMenu(String optionName) {
    waitForMenuToAppear();
    String locator = String.format(".//div[@role='presentation']/div[@role='menu']" +
                                           "/div[contains(.,'%s')]", optionName);
    WebElement item = waitForPresence(By.xpath(locator));
    scrollToElementJS(item);
    waitAndClickWebElement(item);
  }
  
  /**
   * Closes the pop-up menu
   */
  public void closeMenu() {
    getDriver().findElement(By.xpath("//div[contains(@style, 'z-index: 2000;')]")).click();
    waitForMenuToDisappear();
  }
  
  public void closeMenuWithEsc() {
    new Actions(getDriver()).sendKeys(Keys.ESCAPE).perform();
  }
  
  public void closePopUpMenuWithEsc() {
    closeMenuWithEsc();
    waitForMenuToDisappear();
  }
  
  @Step("Test active menu for it's items existence")
  public boolean checkMenuOptions(List<String> expected) {
    List<WebElement> items = getDriver().findElements(By.cssSelector("[role='menu'] [role='menuitem'] > div >div > div"));
    return items.stream().allMatch(act -> expected.stream()
                                                  .anyMatch(act.getText()::equalsIgnoreCase));
  }
  
  public List<WebElement> getOptionsDropdownList() {
    waitForMenuToAppear();
    return getDriver().findElement(By.cssSelector(MENU_CSS_SELECTOR))
                      .findElements(By.cssSelector("span[role='menuitem']"));
  }
  
  public void waitForNumberOfWindowsToBe(int number) {
    wait.until(ExpectedConditions.numberOfWindowsToBe(number));
  }
  
  public void switchToTabWhoseUrlContains(String url) {
    Set<String> existingTabs = getDriver().getWindowHandles();
    for (String tab : existingTabs) {
      getDriver().switchTo().window(tab);
      if (getDriver().getCurrentUrl().contains(url)) {
        break;
      }
    }
  }
  
  public static void closeAllInactiveTabs() {
    WebDriver driver = getDriver();
    String activeTab = driver.getWindowHandle();
    driver.getWindowHandles().stream()
          .filter(t -> !t.equals(activeTab))
          .forEach(t -> driver.switchTo().window(t).close());
    driver.switchTo().window(activeTab);
  }
  
  public boolean isUrlContains(String value) {
    try {
      wait.until(ExpectedConditions.urlContains(value));
      return true;
    } catch (WebDriverException e) {
      return false;
    }
  }
  
  public boolean waitForTextToBePresent(By locator, String value) {
    return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, value));
  }
  
  public void waitForTextToBePresent(WebElement element, String value) {
    wait.until(ExpectedConditions.textToBePresentInElement(element, value));
  }
  
  public void waitForTextToBePresenceInValue(By locator, String value) {
    wait.until(ExpectedConditions.textToBePresentInElementValue(locator, value));
  }
  
  public void waitForTextToBePresenceInValue(WebElement element, String value) {
    wait.until(ExpectedConditions.textToBePresentInElementValue(element, value));
  }
  
  public boolean isElementDisabled(WebElement element) {
    String isDisabled = waitForPresence(element).getAttribute("disabled");
    return (isDisabled != null && isDisabled.equalsIgnoreCase("true"));
  }
  
  public boolean isElementDisabled(By locator) {
    String isDisabled = waitForPresence(locator).getAttribute("disabled");
    return (isDisabled != null && isDisabled.equalsIgnoreCase("true"));
  }
  
  public boolean isElementClickable(By locator) {
    try {
      waitToBeClickable(locator);
      return true;
    } catch (WebDriverException e) {
      return false;
    }
  }
  
  public boolean isElementClickable(WebElement element) {
    try {
      waitToBeClickable(element);
      return true;
    } catch (WebDriverException e) {
      return false;
    }
  }
  
  public void enableImplicitWait(int timeoutSec) {
    getDriver().manage().timeouts().implicitlyWait(timeoutSec, TimeUnit.SECONDS);
  }
  
  public void disableImplicitWait() {
    getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
  }
  
  public void changeTheAttribute(WebElement webElement, String attr, String value){
   js.executeScript("arguments[0].setAttribute(arguments[1],arguments[2]);",webElement,attr,value);
  }
  
}
