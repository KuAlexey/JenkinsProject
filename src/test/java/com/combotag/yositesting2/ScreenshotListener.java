package com.combotag.yositesting2;


import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class ScreenshotListener extends TestListenerAdapter {

    @Override
    public void onTestFailure(ITestResult result) {
        saveScreenshot();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        saveScreenshot();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        saveScreenshot();
    }

    @Attachment(type = "image/png")
    private byte[] saveScreenshot() {
        return ((TakesScreenshot) BaseTest.getDriver()).getScreenshotAs(OutputType.BYTES);
    }
}
