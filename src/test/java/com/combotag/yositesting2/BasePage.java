package com.combotag.yositesting2;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Listeners;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

@Listeners({ScreenshotListener.class})
public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage() {
        driver = BaseTest.getDriver();
        HtmlElementLocatorFactory factory = new HtmlElementLocatorFactory(driver);
        PageFactory.initElements(new HtmlElementDecorator(factory), this);
        wait = new WebDriverWait(driver, 30);
    }
}
