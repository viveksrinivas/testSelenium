package testcases;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Pages.CartPage;
import Pages.HomePage;
import Pages.OrderingPage;
import factory.BrowserFactory;
import factory.DataProviderFactory;
import utility.Helper;

public class Select_Store {

	WebDriver driver;

	@BeforeMethod
	public void setUP() {
		driver = BrowserFactory.getBrowser("Firefox");

		driver.get(DataProviderFactory.getConfig().getApplicationUrl());
	}

	@Test
	public void testHomePage() throws InterruptedException, IOException {

		HomePage home = PageFactory.initElements(driver, HomePage.class);
		
		home.clickOnshopOnlineLink();
		
		OrderingPage orderPage = PageFactory.initElements(driver, OrderingPage.class);
		
		orderPage.selectYourStore();
		
		Thread.sleep(3000);
		orderPage.selectLocation();
		Thread.sleep(1000);
		orderPage.selectStore();
		Thread.sleep(3000);
		orderPage.clickOnContinue();
		
		Shop_Page shopPage = PageFactory.initElements(driver, Shop_Page.class);
		
		shopPage.verifyStore();
		//shopPage.footerLinks();
		shopPage.availableItems();
		shopPage.selectSecondItem();
		shopPage.firstSeafoodItem();
		shopPage.selectYourOption();
		shopPage.quantityOfItem();
		shopPage.AddToCart();
		shopPage.checkoutAction();
		
		CartPage cartPage = PageFactory.initElements(driver, CartPage.class);
		cartPage.CartQuantityEdit();
		cartPage.proceedToCheckout();
		
	}

	@AfterMethod
	public void tearDown() {
		
		Helper.captureScreenshot(driver, "lastPage");
		
		//BrowserFactory.closeBrowser(driver);
	}

}
