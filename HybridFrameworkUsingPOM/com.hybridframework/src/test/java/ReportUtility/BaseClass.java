package ReportUtility;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.relevantcodes.extentreports.LogStatus;

import factory.BrowserFactory;

public abstract class BaseClass {

	protected static WebDriver driver;
    @BeforeMethod
    public void beforeMethod(Method method) {
        ExtentTestManager.startTest(method.getName());
    }
    

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

    
    @AfterMethod
    protected void afterMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            ExtentTestManager.getTest().log(LogStatus.FAIL, result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            ExtentTestManager.getTest().log(LogStatus.SKIP, "Test skipped " + result.getThrowable());
        } else {
            ExtentTestManager.getTest().log(LogStatus.PASS, "Test passed");
        }
        
        ExtentManager.getReporter().endTest(ExtentTestManager.getTest());        
        ExtentManager.getReporter().flush();
    }
    
    protected String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }
}
