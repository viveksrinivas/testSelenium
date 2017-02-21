package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;

import library.ElementHighlight;

public class OrderingPage {
	
	WebDriver driver;
	
	@FindBy(id="select-a-store") WebElement SelectYourStore;
	@FindBy(xpath="//*[@id='edit-state']") WebElement Location;
	@FindBy(xpath="//*[@id='edit-store']") WebElement Store;
	@FindBy(xpath="//*[@id='store-select-submit']")WebElement ContinueButton;
	
	
	public OrderingPage(WebDriver ldriver) {
		 
		this.driver = ldriver;
		
	}
	
	public void selectYourStore() throws InterruptedException{
		
		ElementHighlight.elementHighlight(driver, SelectYourStore);
		SelectYourStore.click();
		Reporter.log("--> Click on 'Select Your Store'",true);
		Thread.sleep(2000);
		
	}
	
	public void selectLocation() throws InterruptedException{
		
		ElementHighlight.elementHighlight(driver, Location);
		new Select(Location).selectByVisibleText("California");
		Reporter.log("--> Selected location as 'California'",true);
		Thread.sleep(2000);
		
	}
	
	public void selectStore(){
		
		ElementHighlight.elementHighlight(driver, Store);
		new Select(Store).selectByVisibleText("La Jolla - 8825 Villa La Jolla Dr");
		Reporter.log("--> Selected store 'La Jolla - 8825 Villa La Jolla Dr'",true);
		
	}
	
	public void clickOnContinue(){
		
		ElementHighlight.elementHighlight(driver, ContinueButton);
		ContinueButton.click();
		Reporter.log("--> Clicked on 'Continue'",true);
		
	}
	
}
