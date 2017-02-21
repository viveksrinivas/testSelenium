package utility;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import factory.BrowserFactory;

public class BaseClass {

	protected static WebDriver driver;
	protected   static ExtentReports report;
	public static  ExtentTest logger;
	private static String reportPath;

	@BeforeClass
	@Parameters("Browser")
	public void startbrowser(@Optional("firefox") String browserName) {
		System.out.println("Browser name is " + browserName);
		if (browserName.equalsIgnoreCase("Firefox")) {
			driver = BrowserFactory.getBrowser("Firefox");
		} else if (browserName.equalsIgnoreCase("Chrome")) {
			driver = BrowserFactory.getBrowser("Chrome");
		} else if (browserName.equalsIgnoreCase("IE")) {
			driver = BrowserFactory.getBrowser("IE");
		}
		
		System.out.println("===========Browser Started=============");

	}




	@BeforeSuite
	public void reportSetUp() {
		System.out.println("==============Setup Started==================");
		reportPath=System.getProperty("user.dir")+"\\Reports\\lajish"+System.currentTimeMillis()+".html";
		//reportPath=System.getProperty("user.dir") +"\\AdvanceReports\\Kaplan"+System.currentTimeMillis()+".html";
		report = new ExtentReports(reportPath, false);
	
		System.out.println("==============Setup End==================");
	}

	@AfterMethod
	public void endTestCase(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE)
		{
			try {
				//test.log(LogStatus.INFO, test.addScreenCapture(Library.captureScreenshotwithpath(driver)));
				logger.log(LogStatus.INFO, logger.addScreenCapture(Helper.captureScreenshot(driver, result.getName())));
				String tc_name = result.getName();
				logger.log(LogStatus.FAIL, "Validation Failed Taken Screenshot"+tc_name);
			} catch (Exception e) {
				System.out.println("Exception while taking screen shot"+e.getMessage());
			}
	
		}
		report.endTest(logger);
	}

	@AfterSuite
	public void tearDown() throws Throwable {
		
    report.flush();
	driver.get(reportPath);
	Thread.sleep(5000);
	driver.quit();
     System.out.println("Report is generated >>>> Browser is closed >>>>"+reportPath);
	}
}
