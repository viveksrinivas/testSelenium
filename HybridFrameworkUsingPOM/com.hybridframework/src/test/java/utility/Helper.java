package utility;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Helper 
{

	
	public static String captureScreenshot(WebDriver driver,String screenshotName)
	{
		
	   TakesScreenshot ts=(TakesScreenshot)driver;
	   File src=ts.getScreenshotAs(OutputType.FILE);
	   //Date date = new Date();
	   //String destination=System.getProperty("user.dir")+"\\Screenshots\\"+screenshotName+System.currentTimeMillis()+".png";
	   String destination=System.getProperty("user.dir")+"\\Screenshots\\"+screenshotName+"_"+new SimpleDateFormat("dd-MMM_HH-mm-SSS").format(new Date())+".png";
	   try 
	   {
		FileUtils.copyFile(src,new File(destination));
	} catch (IOException e)
	   {
		
		System.out.println("Failed to capture screenshots "+e.getMessage());
	}
		
	   
	   return destination;
	}
	
	public static WebElement checkElement(WebDriver driver, String xpath){
		
		WebDriverWait wait = new WebDriverWait(driver, 30);
		return wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		
	}
	
	public static void verifyLinks(String linkUrl) throws IOException{
		
		try {
			URL url = new URL(linkUrl);
			
			HttpURLConnection httpURLConnect=(HttpURLConnection)url.openConnection();
			
			//httpURLConnect.setConnectTimeout(15000);
			
			httpURLConnect.connect();
			
			if(httpURLConnect.getResponseCode()==200)
			    System.out.println(linkUrl+" -->> "+httpURLConnect.getResponseMessage());
			
			
			if(httpURLConnect.getResponseCode()==HttpURLConnection.HTTP_NOT_FOUND)  
			    System.out.println(linkUrl+" -->> "+httpURLConnect.getResponseMessage() + " - "+ HttpURLConnection.HTTP_NOT_FOUND);
			 
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
		}
		
		
	}
	
	
	public static int CountOfJobs(WebDriver driver, String lxpath) {
		

		String Link = driver.findElement(By.xpath("//span[text()='Initiatives']/following::a[1]")).getText();
		String[] Count = Link.split(" ");
		int JobCount = Integer.parseInt(Count[0]);
		System.out.println("Count is "+JobCount);
		return JobCount;
		
	}
	
	public static int CountOfEntries(WebDriver driver, String lxpath) {
		
		List<WebElement> AllRows = driver.findElements(By.xpath(lxpath));
		int counter = 0;
		System.out.println("No. of rows available "+AllRows.size());
		for(int i=1; i<=AllRows.size();i++){
			
			if(driver.findElement(By.xpath(lxpath+"["+i+"]/td[1]//a")).getAttribute("innerHTML").isEmpty()){
				break;
			}
			System.out.println("Content is "+driver.findElement(By.xpath(lxpath+"["+i+"]/td[1]//a")).getAttribute("innerHTML"));
			System.out.println("Value of i is "+i);
			counter++;
		}
		return counter;
		
	}
	
	
	
}
