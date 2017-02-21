package testautomation1;


import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.Proxy;
import com.google.common.base.Function;

//*****************************************FUNCTION CALLING************************************************
public  class   PageObject {
	
	enum Browser {
		FIREFOX {
			@Override
			protected RemoteWebDriver setupDriver() {
				final DesiredCapabilities capabilities = new DesiredCapabilities();
				setupProxy(capabilities);
				return new FirefoxDriver(capabilities);
			}

			private void setupProxy(final DesiredCapabilities capabilities) {
				final String proxyHost = System.getProperty("http.proxyHost");
				final String proxyPort = System.getProperty("http.proxyPort");
				if (proxyHost != null) {
					System.out
					.println("Configuring Firefox Selenium web driver with proxy "
							+ proxyHost
							+ (proxyPort == null ? "" : ":" + proxyPort)
							+ " (requires Firefox browser)");
					final Proxy proxy = new Proxy();
					final String proxyString = proxyHost
							+ (proxyPort == null ? "" : ":" + proxyPort);
					proxy.setHttpProxy(proxyString).setSslProxy(proxyString);
					proxy.setNoProxy("localhost");
					capabilities.setCapability(CapabilityType.PROXY, proxy);
				} else {
					System.out
					.println("Configuring Firefox Selenium web driver without proxy (requires Firefox browser)");
				}
			}

			@Override
			void teardown() throws IOException {
			}
		},
		CHROME {
			ChromeDriverService service;

			@Override
			RemoteWebDriver setupDriver() throws IOException {
				service = ChromeDriverService.createDefaultService();
				service.start();
				return new RemoteWebDriver(service.getUrl(),
						getChromeCapabilities());
			}

			private DesiredCapabilities getChromeCapabilities() {
				ChromeOptions options = new ChromeOptions();
				options.addArguments("allow-file-access-from-files");
				DesiredCapabilities capabilities = DesiredCapabilities.chrome();
				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
				return capabilities;
			}

			@Override
			void teardown() throws IOException {
				service.stop();
			}
		};

		abstract RemoteWebDriver setupDriver() throws IOException;

		abstract void teardown() throws IOException;
	};



	static DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy__hh_mm_ssaa");
	static String execFile = "Execution" + "_" +  dateFormat.format(new Date()) ;



	public static File FailedScreenshotFolder = new File(System.getProperty(
			"webide-automation", "test-output/screenshots/Failed"))
	.getAbsoluteFile();

	public static File PassedScreenshotFolder = new File(System.getProperty(
			"webide-automation", "test-output/screenshots/Passed"))
	.getAbsoluteFile();


	public  WebDriver driver;


	@BeforeMethod
	@Parameters( { "webdriver" })
	public void setUp(ITestContext context,String webdriver) throws Exception {
//		System.out.println("eval id is " + webdriver);
		System.setProperty("webdriver.chrome.driver", webdriver);

		//   WebDriver driver = new ChromeDriver();
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();
		String userName =	new com.sun.security.auth.module.NTSystem().getName();
		//userName = "refapps";
		System.out.println("NT user.name " + userName);
		System.out.println(" user.name " + System.getProperty("user.name"));
		
		String Chromepath = "C:/Users/" + userName + "/AppData/Local/Google/Chrome/Application/chrome.exe"  ;
		
		options.setBinary(new File(Chromepath));
		options.addArguments("--test-type");

		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		driver = new ChromeDriver(capabilities);


		PageFactory.initElements(driver, this);


		driver.manage().window().maximize();

		setupScreenshotFolder();

	}

	private static void setupScreenshotFolder() {
		if (!PassedScreenshotFolder.exists()) {
			PassedScreenshotFolder.mkdirs();

		}
		System.out.println("Screenshots are saved in " + PassedScreenshotFolder);
		if (!FailedScreenshotFolder.exists()) {
			FailedScreenshotFolder.mkdirs();
		}
		System.out.println("Screenshots are saved in " + FailedScreenshotFolder);

	}


	@AfterMethod(alwaysRun = true, description = "take screenshot") 
	public void tearDown(ITestResult result) throws Exception {

		if (!result.isSuccess()) {

		takeScreenshotFailed();
		}
//		driver.close();
//		 driver.quit();
//		
	}

	protected void takeScreenshotFailed() throws IOException, InterruptedException {
		//	  if (!result.isSuccess()) {

		Thread.sleep(1000);
		final File tempFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy__hh_mm_ssaa");

		String destFile = getClass().getSimpleName() + "_" +  dateFormat.format(new Date()) + ".png";

		try {
			FileUtils.copyFile(tempFile, new File(FailedScreenshotFolder + "/" + destFile));
			reportLogScreenshotFail(new File(FailedScreenshotFolder + "/" + destFile));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		Reporter.setEscapeHtml(false);
	//	Reporter.log("Failed <a href=../screenshots/" + destFile + ">Screenshot</a>");
		Thread.sleep(1000);
	} 

	protected void takeScreenshotPassed(String Condition) throws IOException, InterruptedException {
		//	  if (!result.isSuccess()) {

		Thread.sleep(1000);
		final File tempFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy__hh_mm_ssaa");

		String destFile = Condition + "_" +  dateFormat.format(new Date()) + ".png";

		try {
			FileUtils.copyFile(tempFile, new File(PassedScreenshotFolder + "/" + destFile));
		
			reportLogScreenshotPass(new File(PassedScreenshotFolder + "/" + destFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Reporter.setEscapeHtml(false);
	//	Reporter.log("Passed <a href=../screenshots/" + destFile + ">Screenshot</a>");
		Thread.sleep(1000);
	} 
	//*****************************************VARIABLE CALLING************************************************
	public WebDriverWait wait1;
	Properties systemproperties = new Properties();


	String varfilepath;

	private static final long TIMEOUT_IN_SECONDS = 50;



	private String errorMessage;


	//***************************FUNCTION********************************************************************
	//	protected PageObject(final WebDriver driver) {
	//		this.driver = driver;
	//	}

	/*
	  @Override
		public void onTestFailure(ITestResult tr) {

			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy__hh_mm_ssaa");
			String destDir = "target/surefire-reports/screenshots";
			new File(destDir).mkdirs();
			String destFile = dateFormat.format(new Date()) + ".png";

//	        try {
//				FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			System.out.println("Screenshots are saved in....................... " );
			Reporter.setEscapeHtml(false);
			Reporter.log("Saved <a href=../screenshots/" + destFile + ">Screenshot</a>");
		}
	 */
	//********************************************ObjectRepo****************************************************

	////***********USE BY ID******************************
	/*	@FindBy (id="xs_username")
	public WebElement Edit_username;

	@FindBy (id="xs_password")
	public WebElement Edit_password;


	 */
	@FindBy (id="CreateFolderDialog_InputFolderName")
	public WebElement InsertPackage;

	//	@FindBy (id="__field2-tf-input")
	//	public WebElement SearchField;

	@FindBy (xpath="//*[@class='sapUiRb sapUiRbInteractive sapUiRbStd']")

	public WebElement Runtime;

	@FindBy (xpath="//*[@class='sapUiRb sapUiRbInteractive sapUiRbSel sapUiRbStd']")
	public WebElement Designtime;


	@FindBy (xpath="//*[@class='sapUiBtn sapUiBtnNorm sapUiBtnS sapUiBtnStd']")
	public WebElement CancelButton;

	@FindBy (id="__box0-input")
	public WebElement SelectObjectType0;

	@FindBy (id="__box1-input")
	public WebElement SelectObjectType1;

	//***********USE BY XPATH***************************

	@FindBy (xpath="//*[@placeholder='Enter Username']")
	public WebElement Edit_username;

	@FindBy (xpath="//*[@placeholder='Enter Password']")
	public WebElement Edit_password;

	@FindBy (xpath="//*[@title='Add role, privilege or object to the list']")
	public WebElement Add_Role;

	@FindBy (xpath="//*[@title='Remove selected roles, privileges or objects from the list.']")
	public WebElement Remove_Role;


	@FindBy (xpath="//*[text()='New']")
	public WebElement NewButton;

	//Button Click login
	@FindBy (xpath="//*[text()='Log On']")
	public WebElement LOGIN_BUTTON;

	@FindBy (xpath="//*[text()='Package (Ctrl+Alt+Shift+N)']")
	public WebElement PackageButton;

	@FindBy (xpath="//*[@class='sapUiTabClose']")
	public WebElement CloseButton;

	@FindBy (xpath="//*[@title='Save (Ctrl+S)']")
	public WebElement SaveButton;

	@FindBy (xpath="//*[text()='Create']")
	public WebElement createButton;

	@FindBy (xpath="//*[text()='Rename (F2)']")
	public WebElement Rename;

	//********************************************FUNCTION CALLING**********************************************

	protected void reportLogScreenshotPass(File file) {
	      System.setProperty("org.uncommons.reportng.escape-output", "false");
	  
	      String absolute = file.getAbsolutePath();
	      int beginIndex = absolute.indexOf(".");
	      String relative = absolute.substring(beginIndex).replace(".\\","");
	      String screenShot = relative.replace('\\','/');
	  
	  
	Reporter.log("<a href=\"" + screenShot + "\"><p align=\"left\">Passed screenshot at " + new Date()+ "</p>");
	Reporter.log("<p><img width=\"1024\" src=\"" + file.getAbsoluteFile()  + "\" alt=\"Passed screenshot at " + new Date()+ "\"/></p></a><br />"); 
	}
	protected void reportLogScreenshotFail(File file) {
	      System.setProperty("org.uncommons.reportng.escape-output", "false");
	  
	      String absolute = file.getAbsolutePath();
	      int beginIndex = absolute.indexOf(".");
	      String relative = absolute.substring(beginIndex).replace(".\\","");
	      String screenShot = relative.replace('\\','/');
	  
	  
	Reporter.log("<a href=\"" + screenShot + "\"><p align=\"left\">Error screenshot at " + new Date()+ "</p>");
	Reporter.log("<p><img width=\"1024\" src=\"" + file.getAbsoluteFile()  + "\" alt=\"Error screenshot at " + new Date()+ "\"/></p></a><br />"); 
	}
	public void login(String userName,String passWord,String URL) throws Exception { 


		Thread.sleep(2000);
		driver.get(URL); 
		wait1 = new WebDriverWait(driver, 5); 							        			         			   	 

		Edit_username.sendKeys(userName); 
		Thread.sleep(100);
		Edit_password.sendKeys(passWord); 
		Thread.sleep(1000); 			
		LOGIN_BUTTON.click();  
		driver.manage().timeouts().implicitlyWait(6,TimeUnit.MINUTES);
		driver.manage().window().maximize();	


	}
	public void loginEmpty() throws Exception { 

		String URL = systemproperties.getProperty("URL");	

		Thread.sleep(2000);
		driver.get(URL); 
	}


	public void wait(int seconds) {

		try {

			Thread.sleep(1000 * seconds);
 
		} catch (InterruptedException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

	}




	protected final boolean checkElementIsDisplayed(final WebElement element) {
		String id = "<unknown>";
		try {
			id = element.getAttribute("id");
		} catch (final WebDriverException e) {
			System.err.println("Error retrieving element id: " + element);
		}
		return checkElementIsDisplayed(element, id);

	}

	/**
	 * To be used by subclasses, inside {@link #isCurrentPage()} or
	 * {@link #waitUntil(Function)} . In addition to checking whether the
	 * element is on the screen it stores an errorMessage if the element is not
	 * found, which is displayed if the page transition or wait condition fails.
	 */
	protected final boolean checkElementIsDisplayed(final WebElement element,
			final String elementName) {
		try {
			if (!element.isDisplayed()) {
				this.errorMessage = "Element not displayed: " + elementName;
				return false;
			}
		} catch (final NoSuchElementException e) {
			this.errorMessage = "Element not found: " + elementName;
			return false;
		}
		return true;
	}

	/**
	 * To be used by subclasses, inside isCurrentPage and stores an errorMessage
	 * if the condition is false, which is displayed if the page transition
	 * fails.
	 */
	protected final boolean check(boolean condition, String errorMessage) {
		this.errorMessage = errorMessage;
		return condition;
	}

	public WebElement waitFor(By by, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, timeOut);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		return element;
	}

	protected void switchToWindowUsingTitle(WebDriver driver, String title) throws InterruptedException {
		
		WebDriver popup = null ;

		Iterator<String> windowIterator = driver.getWindowHandles().iterator();
		  while(windowIterator.hasNext()) { 
	
	//			Thread.sleep(20000);
	//			String xtitle = driver.switchTo().window(windowId).getTitle();
				String windowHandle = windowIterator.next(); 
			    popup = driver.switchTo().window(windowHandle);
				
				if (popup.getTitle().equals(title)){

					break;
				}
			
			
		}
		
	}

	public void highlightElement(WebDriver driver, WebElement element) throws InterruptedException
	{ 
		Thread.sleep(3000);	

		for (int i = 0; i < 5; i++)
		{ 
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", 
					element, "color: yellow; border: 15px solid yellow;"); 
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
		}
	} 
	public WebElement fluentWait(final By locator) {
	    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
	            .withTimeout(30, TimeUnit.SECONDS)
	            .pollingEvery(5, TimeUnit.SECONDS)
	            .ignoring(NoSuchElementException.class);

	    WebElement foo = wait.until(new Function<WebDriver, WebElement>() {
	        public WebElement apply(WebDriver driver) {
	            return driver.findElement(locator);
	        }
	    });

	    return  foo;
	};
	
	public void createPackage(WebDriver driver, String packageName)throws InterruptedException  {
		Thread.sleep(1000);
		Actions action = new Actions(driver);

		/*This will select menu after right click */

		waitFor(By.xpath("//*[text()='New']") , 3);
		// Thread.sleep(1000);

		action.moveToElement(NewButton).click().perform();
		//  Thread.sleep(1000);
		waitFor(By.xpath("//*[text()='Package (Ctrl+Alt+Shift+N)']") , 3);
		action.moveToElement(PackageButton).click().perform();



		waitFor(By.id("CreateFolderDialog_InputFolderName") , 5);

		InsertPackage.sendKeys(packageName);
		Thread.sleep(1000);
		waitFor(By.xpath("//*[text()='Create']") , 5);

		createButton.click();
		Thread.sleep(1000); 

		Reporter.log("Package created with name:" + packageName  );


	}


}

