package com.combotag.yositesting2;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestMain extends FunctionalTest{
    public class TestSiteMain extends FunctionalTest {

        @Test
        public void mainTest() {
			/*
            LoginPage loginPage = new LoginPage();

            loginPage.setTextToInput("Username","QATestLab");
            loginPage.setTextToInput("Password","QATestLab119933");
            loginPage.clickForSignIn();

            HomePage homePage = new HomePage();

            homePage.clickTabButton("sites");
            Assert.assertTrue(homePage.appearanceOfElementInTheWidget("Site1537233134"));
            homePage.clickChevronOfElement("Site1537233134");
            Assert.assertTrue(homePage.appearanceOfElementInTheWidget("Layout1537233134"));
            homePage.clickChevronOfElement("Layout1537233134");
            Assert.assertTrue(homePage.appearanceOfElementInTheWidget("Banner1537233134"));
			*/
			System.out.println(getDriver());
        }
    }
}
