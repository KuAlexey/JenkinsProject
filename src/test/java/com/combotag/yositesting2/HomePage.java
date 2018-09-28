package com.combotag.yositesting2;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.DataProvider;

public class HomePage extends BasePage {

    @FindBy(xpath = "//div[contains(text(),'Advertisers')][contains(@class,'SearchTree')]/../../../..")
    private WebElement treeAdviserWidgetContainer;

    @FindBy(xpath = "//div[contains(@class,'TreeWidgetToggleButton')]")
    private WebElement toggleButton;

    @FindBy(css = "div[class*='SideBar__container']")
    private WebElement sideBarContainer;

    @FindBy(css = "div[class*='SignOut']")
    private WebElement signOutButton;

    private boolean appearanceOfElementPattern(WebElement webElement) {
        try {
            wait.until(ExpectedConditions.visibilityOf(webElement));
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    private WebElement getWebElement(String name) {
        String xpathLocator = String.format("//span[contains(text(),'%s')]/../../../../*[contains(@class,'TreeNode')]", name);
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathLocator)));
    }

    @Step("Click tab button")
    public void clickTabButton(String nameOfTab) {
        String xpathLocator = String.format("//a[contains(@href,'/%s')]/following-sibling::*/*/*[@filter]", nameOfTab);
        WebElement clickAdviserTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathLocator)));
        wait.until(ExpectedConditions.elementToBeClickable(clickAdviserTab));
        clickAdviserTab.click();
    }

    public boolean appearanceOfElementInTheWidget(String name) {
        WebElement element = getWebElement(name);
        return appearanceOfElementPattern(element);
    }

    @Step("Click chevron of element")
    public void clickChevronOfElement(String name) {
        WebElement chevronOfElement = getWebElement(name).findElement(By.xpath(".//span/i"));
        chevronOfElement.click();
    }
}
