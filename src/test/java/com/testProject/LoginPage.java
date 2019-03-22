package com.testProject;


import com.testProject.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    @FindBy(xpath = "//div[contains(@class,'LoginForm__button')]/button")
    private WebElement signInButton;

    private void setInputManage(String element, String textToInput) {
        String stringFormat = String.format("input[id*='%s']", element);
        WebElement loginOrPassElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(stringFormat)));
        loginOrPassElement.clear();
        loginOrPassElement.sendKeys(textToInput);
    }

    @Step("Write login and password")
    public void setTextToInput(String userOrPassword, String setText) {
        setInputManage(userOrPassword, setText);
    }

    @Step("Click sign in button")
    public void clickForSignIn() {
        signInButton.click();
    }
}

