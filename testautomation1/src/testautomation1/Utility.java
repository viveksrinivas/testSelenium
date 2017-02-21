package testautomation1;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;


public class Utility {

	// private WebDriver driver;
	public Properties EnvProperties = new Properties();
	@Rule
	public ErrorCollector errCol = new ErrorCollector();
	private static FileWriter fWriter = null;
	private static BufferedWriter writer = null;
	private static String timestamp;
	private static String modPath;
	private int sceenShotNo = 0;
	public static SoftAssert sAssert = new SoftAssert();;
	public static String DRIVER_PATH;

	public Utility() {
		try{
			if (System.getProperty("webdriver.path").isEmpty() || System.getProperty("webdriver.path").equals(null)){
				this.DRIVER_PATH = Utility.class.getClassLoader().getResource("chromedriver.exe").getFile();
			}else{
				this.DRIVER_PATH = System.getProperty("webdriver.path");
			}
		}catch (Exception e){
			this.DRIVER_PATH = Utility.class.getClassLoader().getResource("chromedriver.exe").getFile();
		}
		
		// this.sAssert = new SoftAssert();
	}
	
	public static void createReportFolder(){
		Calendar cal = Calendar.getInstance();
		timestamp = Integer.toString(cal.get(Calendar.DATE))
				+ Integer.toString(cal.get(Calendar.MONTH) + 1)
				+ Integer.toString(cal.get(Calendar.YEAR))
				+ Integer.toString(cal.get(Calendar.HOUR))
				+ Integer.toString(cal.get(Calendar.MINUTE))
				+ Integer.toString(cal.get(Calendar.SECOND));
		modPath = System.getProperty("user.dir") + "\\target" + "\\Report_"
				+ timestamp + "\\";
		File repFile = new File(modPath);
		repFile.mkdirs();
	}

	private void incrementScreenShotNo() {
		this.sceenShotNo++;
	}

	
	// **********************************************************************************

	public boolean isElementPresentById(String id, WebDriver driver) {
		try {
			driver.findElement(By.id(id));
			return true; // Success!
		} catch (NoSuchElementException ignored) {
			return false;
		}
	}

	// *********************************************************************************
	
	public void CaptureScreenshot(WebDriver driver, String screenshotPath)
			throws Exception {

		// take the screenshot at the end of every test
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		// now save the screenshot to a file some place
		FileUtils.copyFile(scrFile, new File(modPath + screenshotPath));
	}

	public void passReportandScreenshot(String message, WebDriver driver)
			throws Exception {
		
		String screenShotName = "screenshot_" + this.sceenShotNo + ".png";
		this.CaptureScreenshot(driver, screenShotName);
		this.incrementScreenShotNo();
		writeReport("PASS", message, screenShotName);
	}

	// *********************************************************************************

	public void failReportandScreenshot(String message, WebDriver driver)
			throws Exception {

	
		String screenShotName = "screenshot_" + this.sceenShotNo + ".png";
		this.CaptureScreenshot(driver, screenShotName);
		this.incrementScreenShotNo();
		writeReport("FAIL", message, screenShotName);
		sAssert.fail(message);
	}

	// *********************************************************************************

	public void clickElementbyID(WebDriver driver, String ID) throws Exception {
		if (driver.findElements(By.id(ID)).size() != 0){
			driver.findElement(By.id(ID)).click();
		}else {
			this.failReportandScreenshot("Corresponding element is not displayed", driver);
		}
	}
	
	// *********************************************************************************

	public static void initializeHTMLReport(String testName) throws IOException {
		String fPath = modPath + testName+"_Test_Report.html";
		File repFile = new File(fPath);
		repFile.createNewFile();
		fWriter = new FileWriter(fPath);
		writer = new BufferedWriter(fWriter);
		writer.write("<html><head><title>Selenium Report</title></head><body><table>");
		writer.flush();
	}
	// *********************************************************************************

	private void writeReport(String status, String message, String snapShot)
			throws IOException {
		if (status.toUpperCase().equals("PASS")) {
			writer.write("<tr><td><p style='font-family:arial;color:green;font-size:14px'>"
					+ message
					+ "</p></td><td><img src='"
					+ snapShot
					+ "' alt='Smiley Face' width='700' height='600'><td></tr>");
		} else {
			writer.write("<tr><td><p style='font-family:arial;color:red;font-size:14px'>"
					+ message
					+ "</p></td><td><img src='"
					+ snapShot
					+ "' alt='Smiley Face' width='700' height='600'><td></tr>");
		}
		writer.flush();

	}
	// *********************************************************************************
	public static void finalizeHTMLReport() throws IOException {
		writer.write("</table></body></html>");
		writer.flush();
		writer.close();
		fWriter.close();
	}
	 public int getResponseCode(String url) {
		 WebClient client = new WebClient();
	        try {
	            
	            /*ProxyConfig proxyConfig = new ProxyConfig("proxy",8080);
	            client.setProxyConfig(proxyConfig);*/
	            client.setThrowExceptionOnFailingStatusCode(false);
	            return client.getPage(url).getWebResponse().getStatusCode();
	        } catch (IOException ioe) {
	            throw new RuntimeException(ioe);
	        }finally{
	        	client = null;
	        }
	    }
	// *********************************************************************************
	public void loadCockpitURL(final WebDriver driver, String CloudCockpitURL) throws Exception{
		Thread t = new Thread(new Runnable()
        {	
        	public void run()
        	{
        		driver.get(Thread.currentThread().getName());
        	}
        	
        }, CloudCockpitURL);
        t.start();
        
        try
        {
        	t.join(1);
        }
        catch (InterruptedException e)
        { // ignore
        }
        if (t.isAlive())
        { // Thread still alive, we need to abort

        	t.interrupt();
        }
        
        Thread.sleep(10000);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ENTER);
        try{Thread.sleep(1000);}
        catch(InterruptedException e){}
        robot.keyRelease(KeyEvent.VK_ENTER);
		
	}
}