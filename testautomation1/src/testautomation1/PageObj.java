package testautomation1;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;




public class PageObj {

	public WebDriver driver;
	
	
@BeforeClass
@Parameters({"webdriver"})
public  void setup (String Webdriver ){
	
	System.setProperty("webdriver.chrome.driver", "resources/chromedriver.exe");
	 
	  DesiredCapabilities capabilities = DesiredCapabilities.chrome();
	  ChromeOptions options = new ChromeOptions();
	  options.addArguments("test-type");
	  options.addArguments("--disable-web-security");
	  options.addArguments("--start-maximized");
	  options.addArguments("--allow-running-insecure-content");
	  capabilities.setCapability("chrome.binary", "resources/chromedriver.exe");
	  capabilities.setCapability(ChromeOptions.CAPABILITY,options);
	  driver = new ChromeDriver(capabilities);
	  PageFactory.initElements(driver, this);
	  
}

@FindBy (id="xs_password-inner")
public WebElement Login_pwd;
	
}
