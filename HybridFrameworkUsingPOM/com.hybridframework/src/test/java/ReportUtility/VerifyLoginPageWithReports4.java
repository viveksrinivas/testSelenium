package ReportUtility;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.LogStatus;
import Pages.HomePage;
import factory.DataProviderFactory;
import utility.BaseClass;

public class VerifyLoginPageWithReports4 extends BaseClass
{
	
	@BeforeMethod
	public void setUP()
	{
		 driver.get(DataProviderFactory.getConfig().getApplicationUrl());

	}
	
	
	@Test
	public void testHomePage()
	{
		

		System.out.println("Driver instance is "+driver);		
		
		HomePage home=PageFactory.initElements(driver, HomePage.class);
	 
		String title=home.getApplicationTitle(); 
	
		Assert.assertTrue(title.contains("Demo Store"));	
	 
		ExtentTestManager.getTest().log(LogStatus.PASS, "Log from threadId: " + Thread.currentThread().getId());
		ExtentTestManager.getTest().log(LogStatus.PASS, "Test Title verify");
	}
	
	
	
	
	
}
