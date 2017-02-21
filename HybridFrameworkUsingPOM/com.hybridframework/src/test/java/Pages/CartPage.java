package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Reporter;

public class CartPage {

	WebDriver driver;
	
	@FindBy(xpath="//*[@name='quantity']") WebElement CartQuantity;
	@FindBy(xpath="//*[text()='Proceed to Checkout']") WebElement ProceedToCheckout;
	By button = By.xpath("");
	
	public CartPage(WebDriver ldriver) {
		 
		this.driver = ldriver;
		
	}
	
	public void CartQuantityEdit(){
		
		CartQuantity.click();
		CartQuantity.clear();
		CartQuantity.sendKeys("4");
		Reporter.log("--> Edited Cart quantity to 4",true);
		
	}
	
	public void proceedToCheckout(){
		
		ProceedToCheckout.click();
		Reporter.log("--> Clicked on 'Proceed To Checkout'",true);
	}
	
}
