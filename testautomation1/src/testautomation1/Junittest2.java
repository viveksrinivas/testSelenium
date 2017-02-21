package testautomation1;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Junittest2 {
	
public static WebDriver driver;

public Junittest2()
{
	
	System.out.println("constructor call");
	
	}

	
	public  static WebDriver setUpBeforeClass()  {
		System.setProperty("webdriver.chrome.driver", "resources/chromedriver.exe");
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-web-security");
		options.addArguments("--start-maximized");
		options.addArguments("--allow-running-insecure-content");
		capabilities.setCapability("chrome.binary", "chrome.exe");
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		WebDriver driver = new ChromeDriver(capabilities);
		PageFactory.initElements(driver, Junittest2.class);
		return driver;

	}
@FindBy (id="xs_password-inner")
public WebElement login_pwd;


	
	public static void tearDownAfterClass() throws Exception {
	}

	
	public void setUp() throws Exception {
	}


	public void tearDown() throws Exception {
	}

	
	public void test() {
		fail("Not yet implemented");
	}

}
