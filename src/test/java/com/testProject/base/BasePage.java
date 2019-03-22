package com.testProject.base;

import com.utils.DriverHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementLocatorFactory;

public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    public DriverHelper h;

    public BasePage() {
        driver = BaseTest.getDriver();
        HtmlElementLocatorFactory factory = new HtmlElementLocatorFactory(driver);
        PageFactory.initElements(new HtmlElementDecorator(factory), this);
        wait = new WebDriverWait(driver, 30);
    }
}
