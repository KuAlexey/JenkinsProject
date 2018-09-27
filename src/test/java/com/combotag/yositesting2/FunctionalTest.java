package com.combotag.yositesting2;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.net.MalformedURLException;
import java.net.URL;

public class FunctionalTest {
    private static WebDriver driver;
    private final static String APP_URL = "https://yositesting2.combotag.com/";
    private final static String HUB_URL = "http://10.4.0.109:4444/wd/hub";

    public static WebDriver getDriver() {
        return driver;
    }

    @BeforeTest
    public static void setUp () throws MalformedURLException {

     //   System.setProperty("webdriver.chrome.driver","src/test/resources/chromedriver.exe");
      //    WebDriverManager.chromedriver().setup();
        driver = new RemoteWebDriver(new URL(HUB_URL), new ChromeOptions());
        driver = new ChromeDriver();

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
