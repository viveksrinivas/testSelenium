package testcases;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Pages.HomePage;
import Pages.LoginPage;
import factory.BrowserFactory;
import factory.DataProviderFactory;
import utility.Helper;

public class NewTest_1 {

	WebDriver driver;
	ExtentReports report;
	ExtentTest   logger;
	
	@BeforeMethod
	public void setUP()
	{
		 
		 report=new ExtentReports(".\\Reports\\"+this.getClass().getSimpleName()+".html",true);
		 
		 logger=report.startTest("Verify Test Login");
		 
		 driver=BrowserFactory.getBrowser("Firefox");
		 
		 driver.get(DataProviderFactory.getConfig().getApplicationUrl());
		 
		 logger.log(LogStatus.INFO, "Application is up and running");
	}
	
	
	@Test
	public void testHomePage()
	{

	 HomePage home=PageFactory.initElements(driver, HomePage.class);
	 
	 String title=home.getApplicationTitle(); 
	
	 Assert.assertTrue(title.contains("Demo Store"));	
	 
	 logger.log(LogStatus.PASS, "HOme Page loaded and verified");
	 
	 logger.log(LogStatus.PASS, logger.addScreenCapture(System.getProperty("user.dir")+"\\Screenshots\\TestCase_"+System.currentTimeMillis()+".png"));
	 logger.log(LogStatus.PASS, logger.addScreenCapture(System.getProperty("user.dir")+"\\Screenshots\\730 jenkins_06.png"));
	 //System.getProperty("user.dir")+"\\Screenshots\\"+screenshotName+System.currentTimeMillis()+".png"; 730 jenkins_06.png
	 
	 //home.clickOnSigninLink();
	 
	 logger.log(LogStatus.INFO, "Click on Signout link");
	 
	 LoginPage login=PageFactory.initElements(driver, LoginPage.class);
	 
	 //login.loginApplication(DataProviderFactory.getExcel().getData(0, 0, 0), DataProviderFactory.getExcel().getData(0, 0, 1));
	   
	 logger.log(LogStatus.INFO, "Login to application");
	 
	 login.verifySignOutLink();
	 
	 logger.log(LogStatus.INFO, logger.addScreenCapture(Helper.captureScreenshot(driver, "validation2")));
	 
	 logger.log(LogStatus.PASS, "Signout link is present");
	}
	
	@AfterMethod
	public void tearDown(ITestResult result)
	{
		
		if(result.getStatus()==ITestResult.FAILURE)
		{
			
			String path=Helper.captureScreenshot(driver, result.getName());
			
			logger.log(LogStatus.FAIL, logger.addScreenCapture(path));
		}
		
		 BrowserFactory.closeBrowser(driver);
		 report.endTest(logger);
		 report.flush();
	}
	
	
}
