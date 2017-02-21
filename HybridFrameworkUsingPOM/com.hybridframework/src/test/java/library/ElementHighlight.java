package library;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class ElementHighlight {
	
	public static void elementHighlight(WebDriver driver,WebElement Webelement) {
		
		  JavascriptExecutor js=(JavascriptExecutor)driver; 
		  String originalStyle = Webelement.getAttribute("style");
		  js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", Webelement);
		  try {
			Thread.sleep(250);
		} catch (Exception e) {
			
		}
		  js.executeScript("arguments[0].setAttribute('style', '" + originalStyle + "');", Webelement);
		  
	}

}
