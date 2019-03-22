package com.testProject.base;


import com.utils.ScreenshotListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

@Listeners({ScreenshotListener.class})
public abstract class BaseTest {

    private static ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();
    private HashMap<String, Object> capabilities = new HashMap<>();
    private ChromeOptions options = new ChromeOptions();

    private void initDriver(ChromeOptions newOptions) {
        String env = System.getProperty("target.environment");
        newOptions.addArguments("start-maximized");
        if (env != null && env.equals("remote")) {
            URL host = null;
            try {
               // host = new URL("http://10.4.0.72:4444/wd/hub");
                  host = new URL("http://172.19.0.0:4444/wd/hub");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            RemoteWebDriver driver = new RemoteWebDriver(host, newOptions);
            driver.setFileDetector(new LocalFileDetector());
            driverPool.set(driver);
        } else {
            System.setProperty("webdriver.chrome.driver",
                    new File(BaseTest.class.getResource("/chromedriver.exe").getFile())
                            .getPath());
            driverPool.set(new ChromeDriver(newOptions));
        }
    }

    public ChromeOptions getOptions() {
        if (!capabilities.isEmpty()) {
            capabilities.forEach((k, v) -> options.setCapability(k, v));
        }
        return options;
    }

    public void setCapabilities(HashMap<String, Object> newCapabilities) {
        capabilities.putAll(newCapabilities);
    }

    public void clearCapabilities() {
        capabilities.clear();
    }

    public static WebDriver getDriver() {
        return driverPool.get();
    }

    @BeforeTest(alwaysRun = true)
    public void setUp() {
        initDriver(getOptions());
    }

    @AfterTest(alwaysRun = true)
    public void quit() {
        driverPool.get().quit();
    }

}
