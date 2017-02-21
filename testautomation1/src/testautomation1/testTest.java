package testautomation1;

import org.testng.annotations.Test;
import org.testng.annotations.Test;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.util.NoSuchElementException;

import org.openqa.selenium.*;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import testautomation1.PageObj;

class testTest extends PageObj{
	 
private By username = By.id("xs_username-inner");
	
@BeforeSuite
public void Setup()
{
	System.out.println("start");
	}
	
  @Test
  public void f() throws InterruptedException {
	  
	  driver.get("http://mo-ad3319d93:8000/sap/hana/democontent/epm/ui/NewLaunchpad.html");
	  driver.manage().window().maximize();
	  WebDriverWait wait = new WebDriverWait(driver,10);
	  //WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='xs_username-inner']" )));
	// username = driver.findElement(By.id("xs_username-inner"));
	  WebElement username1 = driver.findElement(username);
	  username1.sendKeys("ADXSA1");
	 // WebElement password = driver.findElement(By.xpath("//*[@id='xs_password-inner']"));
	  //Assert.assertTrue(password.isDisplayed());
	  Login_pwd.sendKeys("Sap12345");
	  WebElement login_button = null ;
	try {
	   login_button = driver.findElement(By.cssSelector("span[id='logon_button-content'][class='sapMBtnContent']"));
	}
	catch (NoSuchElementException e)
	{
		e.printStackTrace();
		System.out.println("message" + e.toString());
		System.out.println("log"+e.getMessage());
	}
	finally
    {
    	System.out.println("test continues");
    }
	
	  login_button.click();
      Thread.sleep(2000);
    
  }
  @AfterClass
  public void Teardown()
  {
//	  driver.quit();
  	}

}
