package testautomation1;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
//import testautomation1.Junittest2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;



public class Junittest1 {
	private By Login_user = By.id("xs_username-inner");
	private By Loginbutton1 = By.xpath("//*[@id='logon_button-content']");
	private By Loginbutton2 = By.cssSelector("span[id='logon_button-content'][class='sapMBtnContent']");
	public WebDriver driver ;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
    System.out.println("start");

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		
	}

	@After
	public void tearDown() throws Exception {
		//driver.quit();
	}
	@Test
	public void test() throws IOException  {
		
		driver = Junittest2.setUpBeforeClass();		
		Junittest2 junitpage = PageFactory.initElements(driver,Junittest2.class);  
		WebElement loging_pwd = junitpage.login_pwd ;
	driver.get("http://mo-49e16f979.mo.sap.corp:8000/sap/hana/democontent/epm/ui/NewLaunchpad.html");
		driver.manage().window().maximize();
		WebDriverWait wait = new WebDriverWait(driver,10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(Login_user));
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("alert('hello world');");
		Alert alert = driver.switchTo().alert();
		System.out.println(alert.getText());		
		alert.accept();
		driver.findElement(Login_user).sendKeys("");
		String keys_double = Keys.chord(Keys.SHIFT,Keys.TAB);
		driver.findElement(Login_user).sendKeys(keys_double);
		driver.findElement(Login_user).sendKeys(Keys.TAB);
		driver.findElement(Login_user).sendKeys("XSA_ADMIN");
		
	   // Actions actions = new Actions(driver);
	    //actions.keyDown(Keys.ENTER).sendKeys("Sap12345").keyUp(Keys.ENTER).perform();
	   loging_pwd.sendKeys("Toor1234");
	   
	   //login_button1 = driver.findElement(By.cssSelector("span[id='logon_button-content'][class='sapMBtnContent']"));	
	   WebElement login_button1 =  driver.findElement(By.cssSelector("span[id='logon_button-content'][class='sapMBtnContent']"));
	   WebElement login_button2 = driver.findElement(By.id("logon_button-content"));
	   WebElement login_button3 = driver.findElement(By.xpath("//*[@id='logon_button-content']" ));
	   
	   
	   login_button1.click();
	  // WebElement ok_button = driver.findElement(By.xpath("//*[@id='welcomePageOkButton']"));
	   //ok_button.click();
	   wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("WelcomeDialog")));
	   WebElement popup_frame = driver.findElement(By.id("WelcomeDialog"));
	   List<WebElement> div_element = popup_frame.findElements(By.tagName("div"));
	   
	   outerloop: for (WebElement cell : div_element){
		   
		   System.out.println("cell value : "+cell.getText());
	    if(cell.getText().contains("OK"))	{
		 System.out.println("cell value"+cell.getText());
	    	 cell.click();
	      break outerloop; 
	    }
	   }
	   
	   for (int k =0;k < div_element.size();k++){
		    WebElement cell = div_element.get(k);
		   if(cell.getText().contains("OK")) 
		   {
		    System.out.println("cell value : "+cell.getText());
	         cell.click();  // Assert.assertTrue(cell.getAttribute("id").contains("welcomePageOkButton"), "element is available");    
		   }
	   }
	   driver.findElement(By.cssSelector("div[id='__tile1-title'][class='sapMStdTileTitle']")).click();
	   driver.findElement(By.xpath("//*[contains(text(),'Continue')]")).click();
	 
		
	   
	   String dest = new String("file.png");
	   final File tempfile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

	   FileUtils.copyFile(tempfile,new File(dest));
	   driver.get("C://Users/I056090/Desktop/report.html");
	   driver.manage().window().maximize();
	   
	   
	   
	}

}
