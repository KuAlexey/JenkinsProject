package com.combotag.yositesting2;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class BaseTest {
    private static ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();

    private final static String APP_URL = "https://yositesting2.combotag.com/";
    //private final static String HUB_URL = "http://172.23.0.2:4444/wd/hub";
    private final static String HUB_URL = "http://10.4.0.109:4444/wd/hub";

    private WebDriver initDriver() {
        String env = System.getProperty("target.environment");
        ChromeOptions options = new ChromeOptions();
        /*options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
                UnexpectedAlertBehaviour.ACCEPT);*/
        if (env != null && env.equals("remote")) {
            URL host = null;
            try {
                host = new URL(HUB_URL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            RemoteWebDriver driver = new RemoteWebDriver(host, options);
            driver.setFileDetector(new LocalFileDetector());
            driverPool.set(driver);
        } else {
            System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
            driverPool.set(new ChromeDriver());
        }
        return driverPool.get();
    }

    public static WebDriver getDriver() {
        return driverPool.get();
    }

    @BeforeTest(description = "Configure something before test", alwaysRun = true)
    public void setUp() {
        WebDriver driver = initDriver();
        String env = System.getProperty("target.environment");
        if (env != null && env.equals("remote")) {
            driver.manage().window().setSize(new Dimension(1920, 1080));
        } else {
            driver.manage().window().maximize();
        }
        driver.get(APP_URL);
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        driverPool.get().quit();
    }
}
