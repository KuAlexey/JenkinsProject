package com.combotag.yositesting2;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.net.MalformedURLException;
import java.net.URL;

public class BaseTest {
    private static RemoteWebDriver driver;
    private final static String APP_URL = "https://yositesting2.combotag.com/";
    //private final static String HUB_URL = "http://172.23.0.2:4444/wd/hub";
    private final static String HUB_URL = "http://10.4.0.109:4444/wd/hub";

    public static RemoteWebDriver getDriver() {
        return driver;
    }

    @BeforeTest(description = "Configure something before test")
    public static void setUp () throws MalformedURLException {

     //   System.setProperty("webdriver.chrome.driver","src/test/resources/chromedriver.exe");
      //    WebDriverManager.chromedriver().setup();
        driver = new RemoteWebDriver(new URL(HUB_URL), new ChromeOptions());
        driver.get(APP_URL);
        driver.manage().window().setSize(new Dimension(1920, 1080));
      //  driver.manage().window().maximize();
    }

    @AfterTest
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
/*
  private WebDriver initDriver() {
    String env = System.getProperty("target.environment");
    ChromeOptions options = new ChromeOptions();
    options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
                          UnexpectedAlertBehaviour.ACCEPT);
    if (env != null && env.equals("remote")) {
      URL host = null;
      try {
        host = new URL("http://172.31.15.102:4444/wd/hub");
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }
      RemoteWebDriver driver = new RemoteWebDriver(host, options);
      driver.setFileDetector(new LocalFileDetector());
      driverPool.set(driver);
    } else {
      System.setProperty("webdriver.chrome.driver",
                         new File(BaseTest.class.getResource("/chromedriver.exe").getFile())
                                 .getPath());
      driverPool.set(new ChromeDriver(options));
    }
    return driverPool.get();
  }

  public static WebDriver getDriver() {
    return driverPool.get();
  }

  @BeforeTest(alwaysRun = true)
  public void setUp() {
    WebDriver driver = initDriver();
    driver.manage().window().maximize();
    String env = System.getProperty("target.environment");
    if (env != null && env.equals("remote")) {
      driver.manage().window().setSize(new Dimension(1920, 1080));
    }

  }

  @BeforeMethod(alwaysRun = true)
  public void basePreconditions() {
    assertThat(Utils.logIn(UserManager.getInstance().getById("main"))).isNotNull();
  }

  @AfterTest(alwaysRun = true)
  public void quit() {
    driverPool.get().quit();
  }
}
 */
