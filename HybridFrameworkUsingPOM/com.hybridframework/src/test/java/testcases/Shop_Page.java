package testcases;

import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.Reporter;

import utility.Helper;

public class Shop_Page {

	WebDriver driver;
	
	@FindBy(id="select-a-store") WebElement SelectYourStore;
	@FindBy(xpath="//*[@id='edit-state']") WebElement Location;
	@FindBy(xpath="//*[@id='edit-store']") WebElement Store;
	@FindBy(xpath="//*[@id='store-select-submit']")WebElement ContinueButton;
	//@FindBy(xpath="//*[contains(text(),'Welcome to Whole Foods Market!')]/following::h2/a") WebElement AvailableItems;
	@FindBy(xpath="//a[text()='Seafood']") WebElement Seafood;
	@FindBy(xpath="//h1[text()='Seafood']/following::a[1]") WebElement FirstSeafoodItem;
	@FindBy(xpath="//*[text()='Select your option']/following::select") WebElement ItemToSelect;
	@FindBy(xpath="//*[text()='Select Option']/following::input[1]") WebElement ItemQuantity;
	@FindBy(xpath="//button[@class='addToCart button']") WebElement AddToCartButton;
	@FindBy(xpath="//*[text()='Checkout']/ancestor::button") WebElement CheckoutButton;	
	
	public Shop_Page(WebDriver ldriver) {
		 
		this.driver = ldriver;
		
	}
	
	public void verifyStore() throws InterruptedException{
		
		Assert.assertTrue(driver.getCurrentUrl().contains("shop/LAJ"), "Not landed to correct shop");
		Reporter.log("--> Verified shop name",true);
		
	}
	
	public void footerLinks() throws InterruptedException, IOException{
		
		ArrayList<WebElement> aLinks = new ArrayList<WebElement>();
		WebElement footer = driver.findElement(By.xpath(".//*[@id='region-footer']"));
		aLinks.addAll(footer.findElements(By.tagName("a")));
		System.out.println("No. of anchor links are "+aLinks.size());
		for(int i=0; i<aLinks.size(); i++){
			
			String url = aLinks.get(i).getAttribute("href");
			System.out.println(url);
			try {
				Helper.verifyLinks(url);
			} catch (Exception e) {
				
			}
		}
		Reporter.log("--> Footer links check",true);
	}

	
	public void availableItems(){
		
		ArrayList<WebElement> availableItems = new ArrayList<WebElement>();
		availableItems.addAll(driver.findElements(By.xpath("//*[contains(text(),'Welcome to Whole Foods Market!')]/following::h2/a")));
		System.out.println("Items available are ");
		for(int i=0; i<availableItems.size();i++){
			System.out.println("Item-->> "+(i+1)+". "+availableItems.get(i).getAttribute("innerHTML"));
		}
		Reporter.log("--> List of Item available",true);
		
	}
	
	public void selectSecondItem(){
		
		Seafood.click();
		Reporter.log("--> Click on 'Seafood' link",true);
	}
	
	
	public void firstSeafoodItem(){
		
		FirstSeafoodItem.click();
		Reporter.log("--> Selected seafood item",true);
		
	}

	public void selectYourOption(){
		
		new Select(ItemToSelect).selectByValue("23155");
		Reporter.log("--> Selected second item",true);
	}
	
	public void quantityOfItem(){
		
		ItemQuantity.click();	
		ItemQuantity.clear();
		ItemQuantity.sendKeys("2");
		Reporter.log("--> No. of quantity as 2",true);
		
	}
	
	public void AddToCart() throws InterruptedException{
		
		Thread.sleep(2000);
		AddToCartButton.click();
		Reporter.log("--> Clicked on 'Add To Cart' button",true);
	
	}
	
	public void checkoutAction(){
		
		CheckoutButton.click();
		Reporter.log("--> Click on 'Checkout' button",true);
		
	}
	
}
