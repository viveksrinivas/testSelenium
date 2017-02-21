package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import library.ElementHighlight;
import utility.Helper;

public class HomePage 
{

	WebDriver driver;
	
	public HomePage(WebDriver driver)
	{
		
		this.driver=driver;
	}
	
	
	@FindBy(xpath=".//*[text()='Shop Online']") WebElement shopOnline;
	
	@FindBy(xpath="//a[text()='My Account']") WebElement myAccountLink;
	
	@FindBy(xpath="//a[text()='My cart']") WebElement myCartLink;
	
	@FindBy(xpath="//a[text()='Checkout']") WebElement checkOutink;
	
	
	public void clickOnshopOnlineLink()
	{
		ElementHighlight.elementHighlight(driver, shopOnline);
		shopOnline.click();
	}
	
	public void clickOnMyAccountLink()
	{
		
		myAccountLink.click();
	}
	
	public void clickOnMyCartLink()
	{
		myCartLink.click();
	}
	
	public void clickOnCheckoutlink()
	{
		checkOutink.click();
	}
	
	public String getApplicationTitle()
	{
		 return driver.getTitle();
	}

	public void InitiativesLink_1()
	{
		 //Helper.CountOfJobs(driver, lxpath);
	}
	
}
