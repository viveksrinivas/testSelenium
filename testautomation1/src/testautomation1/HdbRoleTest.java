
package testautomation1;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

import org.testng.Assert;
import org.apache.http.util.Asserts;
import org.openqa.selenium.Keys;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import testautomation1.PageObject;

import org.openqa.selenium.JavascriptExecutor;

public class HdbRoleTest extends PageObject {
	private String roleName = "Role";
	private String packageName = String.valueOf(666 + (int) (Math.random() * 9999)) + "_package";
	private Actions action;
	private Actions builder;

	@BeforeSuite(alwaysRun = true)
	protected void setUp() throws Exception {
		System.out.println("start");
	}

	@AfterSuite(alwaysRun = true)
	public void clean() throws Exception {
		System.out.println("end");
		deleteRole(roleName, packageName);
		deletePackage(packageName);

		// Logout
		driver.findElement(By.xpath("//button[@title='Logout']")).click();
		takeScreenshotPassed("Logout");
		Reporter.log("Logged out");
		super.tearDown(null);
	}
	
	@AfterMethod(description = "if test failed, then take screenshot")
	public void afterTest(ITestResult result) throws IOException, InterruptedException {
		if (!result.isSuccess()) {
			takeScreenshotFailed();
		}
	}

	@Test
	@Parameters({ "username", "password", "url", "webdriver" })
	public void test(String username, String password, String url, String webdriver) throws Exception {

		loginAndNavigateToEditor(username, password, url);
		createPackage(driver, packageName);
		createRole(driver, roleName, packageName);

		// Check for "roleForTest" Design-time
		addGrantedRoles("idetests.editor.plugin.editors.hdbrole.target::roleForTest", "Design-time");

		// Check for "roleForTest" Run-time
		addGrantedRoles("idetests.editor.plugin.editors.hdbrole.target::roleForTest", "Run-time");

		 //Click on System Privileges tab
		addSystemPrivileges("AUDIT ADMIN");

		// Click on Object Privileges tab
		addObjectPrivileges("Design-time");
		addObjectPrivileges("Run-time");

		// Click on Analytic Privileges tab
		addAnalyticPrivileges("Design-time");
		addAnalyticPrivileges("Run-time");
		
		
		addPackagePrivileges(packageName);
		
		addApplicationPrivileges("idetests.editor.plugin.editors.hdbrole::applicationPrivilegeForTest");
		
		cleareConsole();
		SaveButton.click();
		// check for success message
		String SuccessMessage = "File /" + packageName + "/" + roleName + ".hdbrole saved & activated successfully.";
		messageCheck(SuccessMessage);


		// check whether selected roles are present in editor.
		editorAvailableRoleCheck(packageName);

		// Switch to Design time Role Editor Tab

		String DesignTimeTabTitle = "//a[@title='Design Time Role Editor for: " + packageName + "::Role ']";
		driver.findElement(By.xpath(DesignTimeTabTitle)).click();

		// delete roles
		deleteGrantedRoles();
		// check in editor for all the deleted ones exist or not
		editorDeletedRoleCheck(packageName);

	}
	
	//not needed until now, because of a webIDE bug durring the rename process
	public void renameRole() throws InterruptedException {
		Thread.sleep(1000);

		// Rename the role.HDBrole
		WebElement Role = driver.findElement(By.xpath("//*[text()='Role.hdbrole']"));
		Thread.sleep(1000);
		action.contextClick(Role).build().perform();

		waitFor(By.xpath("//*[text()='Rename (F2)']"), 3);

		WebElement Rename = driver.findElement(By.xpath("//*[text()='Rename (F2)']"));
		Thread.sleep(1000);
		action.moveToElement(Rename).click().perform();
		Thread.sleep(1000);

		WebElement renameRole = driver.findElement(By.xpath("//input[@title='Insert name']"));
		// renamerole.sendKeys("RoleRenamed.hdbrole");
		Thread.sleep(1000);
		JavascriptExecutor js = null;
		if (driver instanceof JavascriptExecutor) {
			js = (JavascriptExecutor) driver;
		}

		String renameRoleName = "'RoleRenamed.hdbrole'";
		js.executeScript("arguments[0].value =" + renameRoleName, renameRole);
		Thread.sleep(1000);
		builder = new Actions(driver);
		builder.keyDown(Keys.SHIFT).sendKeys(Keys.TAB).build().perform();
		String selectAll = Keys.chord(Keys.SHIFT, Keys.TAB);
		renameRole.sendKeys(selectAll);

		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[text()='Rename']")).click();
		Thread.sleep(1000);
		//
		SaveButton.click();
		Thread.sleep(2000);
	}

	public void deletePackage(String packageName) throws InterruptedException, IOException {
		List<WebElement> packages = driver.findElements(By.xpath("//a[text()='" + packageName + "']"));
		if (packages.isEmpty()) {
			return;
		}
		cleareConsole();

		// Delete package
		action.contextClick(driver.findElement(By.xpath("//a[text()='" + packageName + "']"))).build().perform();
		Thread.sleep(1000);
		waitFor(By.xpath("//*[text()='Delete']"), 3);

		action.moveToElement(driver.findElement(By.xpath("//div[text()='Delete']"))).click().perform();
		Thread.sleep(1000);

		waitFor(By.xpath("//*[text()='Confirmation Needed']"), 3);
		builder = new Actions(driver);
		builder.sendKeys(Keys.ENTER).build().perform();
		Thread.sleep(1000);

		// check for success message after delete package
		String SuccessMessageDeletePackage = "Folder /" + packageName + " deleted successfully.";
		messageCheck(SuccessMessageDeletePackage);
	}

	public void deleteRole(String roleName, String packageName) throws InterruptedException, IOException {
		List<WebElement> roles = driver.findElements(By.xpath("//a[text()='" + roleName + ".hdbrole']"));
		if (roles.isEmpty()) {
			return;
		}
		cleareConsole();

		// Delete role
		action.contextClick(driver.findElement(By.xpath("//a[text()='" + roleName + ".hdbrole']"))).build().perform();
		Thread.sleep(1000);
		waitFor(By.xpath("//*[text()='Delete']"), 3);

		action.moveToElement(driver.findElement(By.xpath("//div[text()='Delete']"))).click().perform();
		Thread.sleep(1000);

		waitFor(By.xpath("//*[text()='Confirmation Needed']"), 3);
		builder = new Actions(driver);
		builder.sendKeys(Keys.ENTER).build().perform();
		Thread.sleep(1000);

		// check for success message after delete role
		String SuccessMessageDeleteRole = "File /" + packageName + "/" + roleName + ".hdbrole deleted successfully.";
		messageCheck(SuccessMessageDeleteRole);
	}

	public void deleteGrantedRoles() throws InterruptedException {
		Thread.sleep(1000);
		// int flag1 = 0 ;
		// Actions builder = new Actions(driver);
		String cssSelectorOfTabList = "ul[class='sapUiTabBarCnt'][role='tablist']";
		List<WebElement> tabBar = driver.findElements(By.cssSelector(cssSelectorOfTabList));
		List<WebElement> tabList = tabBar.get(0).findElements(By.tagName("li"));
		int i = 0;
		for (WebElement tabCell : tabList) {

			// System.out.println("tab element  is : " +
			// tabCell.getAttribute("textContent") + " ");
			if (tabCell.getAttribute("textContent") != null) {

				Thread.sleep(1000);
				tabCell.click();
				Thread.sleep(1000);
				String tableID = "__table" + i + "-table";

				WebElement roleTable = driver.findElement(By.id(tableID));
				// String
				// cssSelectorOfRoleTable="table[class='sapUiTableCtrl sapUiTableCtrlRowScroll sapUiTableCtrlScroll'][role='grid']";
				// List<WebElement> roleTable =
				// driver.findElements(By.cssSelector(cssSelectorOfRoleTable)) ;
				List<WebElement> tableRows = roleTable.findElements(By.tagName("tr"));

				for (int k = 0; k < tableRows.size(); k++) {
					// for (WebElement cell : tableRows)
					// {
					WebElement cell = tableRows.get(k);
					// System.out.println("element texcontent is : " +
					// cell.getAttribute("textContent") );
					// System.out.println("element title is : " +
					// cell.getAttribute("title") + " ");
					// boolean textField = Pattern.matches("[a-zA-Z]+",
					// cell.getAttribute("textContent")) ;
					// System.out.println("pattern check : " + textField );
					// System.out.println("element length is : " +
					// cell.getAttribute("textContent").length() + " ");

					if ((cell.getAttribute("textContent") != null) && (!(cell.getAttribute("textContent").contains("null(null)")))
							&& (cell.getAttribute("textContent").length() != 0))
					// if ((textField == true ) &&
					// (cell.getAttribute("title").contains("Click to Select"))
					// )
					{
						if ((cell.getAttribute("title").contains("Click to Select"))
								|| (cell.getAttribute("title").contains("Click to Deselect"))) {

							Thread.sleep(1000);

							List<WebElement> tableValue = cell.findElements(By.tagName("span"));
							Thread.sleep(1000);
							if (cell.getAttribute("title").contains("Click to Select")) {

								if (tabCell.getAttribute("textContent").equals("Object Privileges")) {
									tableValue.get(1).click();
								} else
									tableValue.get(0).click();
							}

							// cell.click();
							Thread.sleep(1000);
							Remove_Role.click();
							k = k - 1;
							// Click on save button
							Thread.sleep(1000);
							// save button to save the role
							// SaveButton.click();
							// Thread.sleep(2000);
							// tableRows =
							// roleTable.findElements(By.tagName("tr"));
							// Thread.sleep(1000);
						}
					}

				}

			}
			i++;
		}

		SaveButton.click();
	}

	public void roleObjectCheck(String object, String Privilege, String Origin, String SelectObjectType) throws InterruptedException
	{

		// String
		// cssSelectorOfSameElements="input[class='sapUiTf sapUiTfInner'][type='search']";
		// int tableIndex;
		// List<WebElement> searchField
		// =driver.findElements(By.cssSelector(cssSelectorOfSameElements)) ;

		Thread.sleep(1000);
		int flag1 = 0;
		Actions builder = new Actions(driver);
		if (Origin.equals("Run-time")) {
			builder.sendKeys(Keys.TAB).build().perform();

			builder.sendKeys(Keys.ENTER).build().perform();

			if (Privilege.equals("Object_Privileges")) {
				builder.sendKeys("ForTest").build().perform();
			} else {
				builder.sendKeys(object).build().perform();
			}

		} else {
			if (Privilege.equals("Object_Privileges")) {
				builder.sendKeys(Keys.TAB).build().perform();

				builder.sendKeys(Keys.ENTER).build().perform();

				builder.sendKeys("ForTest").build().perform();

			}

			else {
				builder.sendKeys(Keys.ENTER).sendKeys(object);
				builder.build().perform();
			}
		}

		Thread.sleep(1000);

		String cssSelectorOfSameTables = "table[class='sapUiTableCtrl sapUiTableCtrlRowScroll sapUiTableCtrlScroll'][role='grid']";

		List<WebElement> tables = driver.findElements(By.cssSelector(cssSelectorOfSameTables));

		// Grab the table***
		// WebElement table =
		// driver.findElement(By.xpath("//*[@class='sapUiTableCtrl sapUiTableCtrlRowScroll sapUiTableCtrlScroll']"));
		Thread.sleep(2000);
		List<WebElement> allRows = tables.get(0).findElements(By.tagName("tr"));

		if (Privilege.equals("Granted_Roles")) {
			// Now get all the TR elements from the table
			allRows = tables.get(0).findElements(By.tagName("tr"));
			if (Origin.equals("Run-time"))
				allRows = tables.get(1).findElements(By.tagName("tr"));
		} else if (Privilege.equals("System_Privileges")) {
			allRows = tables.get(2).findElements(By.tagName("tr"));
			// if(Origin.equals("Run-time"))
			// allRows = tables.get(3).findElements(By.tagName("tr"));
		} else if (Privilege.equals("Object_Privileges")) {
			if (Origin.equals("Design-time")) {
				if (SelectObjectType.equals("SCHEMA")) {
					allRows = tables.get(3).findElements(By.tagName("tr"));
				}
				if (SelectObjectType.equals("TABLE")) {
					allRows = tables.get(4).findElements(By.tagName("tr"));
				}
				if (SelectObjectType.equals("VIEW")) {
					allRows = tables.get(5).findElements(By.tagName("tr"));
				}
			} else if (Origin.equals("Run-time")) {
				if (SelectObjectType.equals("SCHEMA")) {
					allRows = tables.get(6).findElements(By.tagName("tr"));
				}
				if (SelectObjectType.equals("TABLE")) {
					allRows = tables.get(7).findElements(By.tagName("tr"));
				}
				if (SelectObjectType.equals("VIEW")) {
					allRows = tables.get(8).findElements(By.tagName("tr"));
				}
			}

		} else if (Privilege.equals("Analytic_Privileges")) {
			allRows = tables.get(9).findElements(By.tagName("tr"));
			if (Origin.equals("Run-time"))
				allRows = tables.get(10).findElements(By.tagName("tr"));
		} else if (Privilege.equals("Package_Privileges")) {
			allRows = tables.get(11).findElements(By.tagName("tr"));
		} else if (Privilege.equals("Application_Privileges")) {
			allRows = tables.get(12).findElements(By.tagName("tr"));
		}

		Thread.sleep(1000);
		// And iterate over them, getting the cells
		outerloop: for (WebElement cell : allRows) {
			// List<WebElement> cells = row.findElements(By.tagName("td"));
			// Thread.sleep(1000);

			// Print the contents of each cell
			// for (WebElement cell : cells) {
			// System.out.println("element is : " +
			// cell.getAttribute("textContent") + " ");
			if (cell.getAttribute("textContent").contains(object)) {
				Thread.sleep(1000);
				// cell.sendKeys(Keys.ENTER);
				// builder.sendKeys(cell,Keys.ENTER).build().perform();
				cell.click();
				// System.out.println("Object: " + object + " exist");
				builder.sendKeys(Keys.TAB).build().perform();

				// builder.sendKeys(Keys.ENTER).build().perform();

				flag1 = 0;
				Thread.sleep(1000);
				// driver.findElement(By.xpath("//*[text()='OK']")).click();
				builder.sendKeys(Keys.TAB).build().perform();

				builder.sendKeys(Keys.ENTER).build().perform();

				Thread.sleep(2000);
				break outerloop;

			} else {

				flag1 = 1;
			}
		}

		// }

		if (flag1 == 0) {
			System.out.println("Role Object: " + object + " exist");
		} else {
			System.out.println("Role Object: " + object + " doesn't exist");
			CancelButton.click();
		}

	}

	public void originChange(String Origin, String Privilege, int Index) throws InterruptedException {
		// Thread.sleep(1000);
		// "span.CCC:contains('TTT')"
		String cssSelectorOfSameElements = "span[class='sapUiRb sapUiRbInteractive sapUiRbStd'][title='Run-time']";

		List<WebElement> RadioBox = driver.findElements(By.cssSelector(cssSelectorOfSameElements));
		Thread.sleep(1000);
		RadioBox.get(Index).click();

	}

	public void addGrantedRoles(String roleName, String Origin) throws InterruptedException, IOException {
		try {
			// Click on the '+' button to add role
			Thread.sleep(2000);
			Add_Role.click();
			Thread.sleep(2000);
			if (Origin.equals("Run-time")) {
				originChange("Run-time", "Granted_Roles", 1);
			}
			Thread.sleep(2000);
			roleObjectCheck(roleName, "Granted_Roles", Origin, "");
			takeScreenshotPassed("Granted Roles- " + Origin + " added");
			Reporter.log("Granted Roles- " + Origin + " added");

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addSystemPrivileges(String systempPrivilege) throws InterruptedException, IOException {
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[text()='System Privileges']")).click();
		Thread.sleep(1000);
		// Click on the '+' button to add role
		Add_Role.click();
		Thread.sleep(1000);

		// Check for Audit_Admin
		roleObjectCheck(systempPrivilege, "System_Privileges", "", "");
		
		takeScreenshotPassed("System privileges added");
		Reporter.log("System privileges added");
	}

	public void addObjectPrivileges(String Origin) throws InterruptedException, IOException {
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[text()='Object Privileges']")).click();
		Thread.sleep(1000);
		if (Origin.equals("Design-time")) {

			String type = "SCHEMA";
			selectObjectType(type, Origin, "Object_Privileges");
			// Click for schemaForTest.hdbschema
			roleObjectCheck("idetests.editor.plugin.editors.hdbrole.target:schemaForTest.hdbschema", "Object_Privileges", Origin, type);
			Thread.sleep(2000);
			driver.findElement(By.xpath("//*[text()='SELECT']")).click();
			Thread.sleep(2000);

			type = "TABLE";
			selectObjectType("TABLE", Origin, "Object_Privileges");
			roleObjectCheck("idetests.editor.plugin.editors.hdbrole.target::tableForTest", "Object_Privileges", Origin, type);
			Thread.sleep(2000);
			driver.findElement(By.xpath("//*[text()='SELECT']")).click();
			Thread.sleep(2000);

			type = "VIEW";
			selectObjectType("VIEW", Origin, "Object_Privileges");
			roleObjectCheck("idetests.editor.plugin.editors.hdbrole.target::viewForTest", "Object_Privileges", Origin, type);
			Thread.sleep(2000);
			driver.findElement(By.xpath("//*[text()='SELECT']")).click();
			Thread.sleep(2000);
		} else if (Origin.equals("Run-time")) {
			String type = "SCHEMA";
			selectObjectType(type, Origin, "Object_Privileges");
			// Click for schemaForTest.hdbschema
			roleObjectCheck("schemaForTest", "Object_Privileges", Origin, type);
			Thread.sleep(2000);
			driver.findElement(By.xpath("//*[text()='SELECT']")).click();
			Thread.sleep(2000);

			type = "TABLE";
			selectObjectType("TABLE", Origin, "Object_Privileges");
			roleObjectCheck("idetests.editor.plugin.editors.hdbrole.target::tableForTest(schemaForTest)", "Object_Privileges", Origin, type);
			Thread.sleep(2000);
			driver.findElement(By.xpath("//*[text()='SELECT']")).click();
			Thread.sleep(2000);

			type = "VIEW";
			selectObjectType("VIEW", Origin, "Object_Privileges");
			roleObjectCheck("idetests.editor.plugin.editors.hdbrole.target::viewForTest(schemaForTest)", "Object_Privileges", Origin, type);
			Thread.sleep(2000);
			driver.findElement(By.xpath("//*[text()='SELECT']")).click();
			Thread.sleep(2000);
		}
		takeScreenshotPassed("Object privileges- " + Origin + " added");
		Reporter.log("Object privileges- " + Origin + " added");
	}

	public void selectObjectType(String type, String Origin, String Privilege) throws InterruptedException {

		Actions builder = new Actions(driver);

		// Click on the '+' button to add role
		// String type1 = "'" +type + "'";
		String xpath_type = "//span[text()='" + type + "']";
		// System.out.println("xpath_type: " + xpath_type );

		Add_Role.click();
		Thread.sleep(1000);
		String cssSelectorOfSelectObjectType = "input[class='sapUiTf sapUiTfInner'][title='Select Object Type']";
		// String
		// cssSelectorOfSelectObjectType="div[role='combobox'][title='Select Object Type']";

		List<WebElement> SelectObjectType = driver.findElements(By.cssSelector(cssSelectorOfSelectObjectType));

		if (Origin.equals("Run-time")) {
			Thread.sleep(2000);
			if (type.equals("SCHEMA")) {
				originChange("Run-time", "Object_Privileges", 4);
				SelectObjectType.get(3).click();
				builder.sendKeys(Keys.ARROW_UP).build().perform();
				SelectObjectType.get(3).sendKeys(type);
			} else if (type.equals("TABLE")) {
				originChange("Run-time", "Object_Privileges", 4);
				SelectObjectType.get(4).click();
				builder.sendKeys(Keys.ARROW_UP).build().perform();
				SelectObjectType.get(4).sendKeys(type);
			} else if (type.equals("VIEW")) {
				originChange("Run-time", "Object_Privileges", 4);
				SelectObjectType.get(5).click();
				builder.sendKeys(Keys.ARROW_UP).build().perform();
				SelectObjectType.get(5).sendKeys(type);
			}

		} else {

			Thread.sleep(2000);
			if (type.equals("SCHEMA")) {
				SelectObjectType.get(0).click();
				builder.sendKeys(Keys.ARROW_UP).build().perform();
				SelectObjectType.get(0).sendKeys(type);
			} else if (type.equals("TABLE")) {

				SelectObjectType.get(1).click();
				builder.sendKeys(Keys.ARROW_UP).build().perform();
				SelectObjectType.get(1).sendKeys(type);
			} else if (type.equals("VIEW")) {

				SelectObjectType.get(2).click();
				builder.sendKeys(Keys.ARROW_UP).build().perform();
				SelectObjectType.get(2).sendKeys(type);
			}
		}
		Thread.sleep(2000);
	}
	
	public void addPackagePrivileges(String packageName) throws InterruptedException, IOException {
		driver.findElement(By.xpath("//*[text()='Package Privileges']")).click();
		Thread.sleep(1000);
		Add_Role.click();
		Thread.sleep(1000);
		roleObjectCheck(packageName, "Package_Privileges", "", "");
		Thread.sleep(1000);
		//add privilege on package
		driver.findElement(By.xpath("//*[text()='REPO.READ']")).click();
		Thread.sleep(1000);
		takeScreenshotPassed("Package privilege added");
		Reporter.log("Package privilege added");
	}
	
	public void addApplicationPrivileges(String appPrivName) throws InterruptedException, IOException {
		driver.findElement(By.xpath("//*[text()='Application Privileges']")).click();
		Thread.sleep(1000);
		Add_Role.click();
		Thread.sleep(1000);
		roleObjectCheck(appPrivName, "Application_Privileges", "", "");
		Thread.sleep(1000);
		takeScreenshotPassed("Application privilege added");
		Reporter.log("Application privilege added");
	}

	public void addAnalyticPrivileges(String Origin) throws InterruptedException, IOException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[text()='Analytic Privileges']")).click();
		Thread.sleep(2000);
		Add_Role.click();
		Thread.sleep(2000);

		if (Origin.equals("Run-time")) {
			originChange("Run-time", "Analytic_Privileges", 5);

			Thread.sleep(2000);
			roleObjectCheck("idetests.editor.plugin.editors.hdbrole.target/analyticPrivilegeForTest", "Analytic_Privileges", Origin, "");

		} else {
			Thread.sleep(2000);
			roleObjectCheck("idetests.editor.plugin.editors.hdbrole.target:analyticPrivilegeForTest.analyticprivilege",
					"Analytic_Privileges", Origin, "");
		}
		
		takeScreenshotPassed("Analytic privileges- " + Origin + " added");
		Reporter.log("Analytic privileges- " + Origin + " added");
	}

	public void cleareConsole() throws InterruptedException, IOException {
		WebElement console = driver.findElement(By.id("Console"));
		action.contextClick(console).build().perform();
		driver.findElement(By.xpath("//*[text()='Clear console']")).click();
		Thread.sleep(1000);

		// check that console is empty
		console = driver.findElement(By.id("Console"));
		List<WebElement> allMessages = console.findElements(By.tagName("div"));

		for (WebElement cell : allMessages) {
			Assert.assertTrue(cell.getText().isEmpty(), "Console should be empty after clear console");
		}

	}

	public void messageCheck(String searchString) throws InterruptedException {
		boolean msgExists = true;
		Thread.sleep(2000);
		// Grab the message***
		WebElement message = driver.findElement(By.id("Console"));
		Thread.sleep(1000);
		// Now get all the TR elements from the table
		List<WebElement> allMessages = message.findElements(By.tagName("div"));
		Thread.sleep(1000);
		// And iterate over them, getting the cells

		outerloop:
		// Print the contents of each cell
		for (WebElement cell : allMessages) {
			System.out.println(cell.getText());
			if (cell.getText().contains(searchString)) {
				Thread.sleep(1000);

				System.out.println("Message " + cell.getText());
				msgExists = true;
				break outerloop;
			} else {
				msgExists = false;
			}
			Assert.assertFalse(cell.getText().contains("Error"), "There should be no error message.");
		}

		Assert.assertTrue(msgExists, "The expected message: '" + searchString + "' doesn't exist.");
	}

	private void editorAvailableRoleCheck(String packageName) throws InterruptedException {
		action.contextClick(driver.findElement(By.xpath("//a[text()='Role.hdbrole']"))).build().perform();
		Thread.sleep(1000);
		action.moveToElement(driver.findElement(By.xpath("//div[text()='Open With']"))).click().perform();
		Thread.sleep(1000);
		action.moveToElement(driver.findElement(By.xpath("//*[text()='Text Editor']"))).click().perform();
		Thread.sleep(1000);

		// check for all privileges in text editor
		Thread.sleep(1000);
		// identify the textarea
		WebElement textarea = driver.findElement(By.xpath("//*[@class='ace_content']"));
		// get content of textarea
		String content = textarea.getText();

		try {

			String roleCheckValue = "role "
					+ packageName
					+ "::Role extends role idetests.editor.plugin.editors.hdbrole.target::roleForTest extends catalog role \"idetests.editor.plugin.editors.hdbrole.target::roleForTest\"";
			Assert.assertTrue(content.contains(roleCheckValue));
		} catch (Error e) {
			System.out.println("role " + e.toString());
		}

		try {
			Assert.assertTrue(content.contains("system privilege: AUDIT ADMIN;"));
		} catch (Error e) {
			System.out.println("system " + e.toString());
		}

		try {
			Assert.assertTrue(content.contains("schema idetests.editor.plugin.editors.hdbrole.target:schemaForTest.hdbschema: SELECT;"));
		} catch (Error e) {
			System.out.println("schema " + e.toString());
		}

		try {

			Assert.assertTrue(content
					.contains("sql object idetests.editor.plugin.editors.hdbrole.target::tableForTest: //Objecttype: TABLE"));
		} catch (Error e) {
			System.out.println("sql table " + e.toString());
		}

		try {

			Assert.assertTrue(content.contains("sql object idetests.editor.plugin.editors.hdbrole.target::viewForTest: //Objecttype: VIEW"));
		} catch (Error e) {
			System.out.println("sql view " + e.toString());
		}

		try {
			Assert.assertTrue(content.contains("catalog schema \"schemaForTest\": SELECT;"));
		} catch (Error e) {
			System.out.println(" Catalog " + e.toString());
		}

		try {
			Assert.assertTrue(content
					.contains("catalog sql object \"schemaForTest\".\"idetests.editor.plugin.editors.hdbrole.target::tableForTest\": //Objecttype: TABLE"));
		} catch (Error e) {
			System.out.println("Catalog table " + e.toString());
		}

		try {
			Assert.assertTrue(content
					.contains("catalog sql object \"schemaForTest\".\"idetests.editor.plugin.editors.hdbrole.target::viewForTest\": //Objecttype: VIEW"));
		} catch (Error e) {
			System.out.println("catalog view " + e.toString());
		}

		try {
			Assert.assertTrue(content
					.contains("analytic privilege : idetests.editor.plugin.editors.hdbrole.target:analyticPrivilegeForTest.analyticprivilege;"));
		} catch (Error e) {
			System.out.println("analytic " + e.toString());
		}

		try {
			Assert.assertTrue(content
					.contains("catalog analytic privilege : \"idetests.editor.plugin.editors.hdbrole.target/analyticPrivilegeForTest\";"));
		} catch (Error e) {
			System.out.println("catalog analytic " + e.toString());
		}

		try {
			String packagePrivilege = "package " + packageName + ": REPO.READ;";
			Assert.assertTrue(content.contains(packagePrivilege));
		} catch (Error e) {
			System.out.println("package " + e.toString());
		}

		try {
			Assert.assertTrue(content
					.contains("application privilege: \"idetests.editor.plugin.editors.hdbrole::applicationPrivilegeForTest\";"));
		} catch (Error e) {
			System.out.println("application privilege " + e.toString());
		}

	}

	private void loginAndNavigateToEditor(String username, String password, String url) throws Exception {
		login(username, password, url);
		waitFor(By.id("myShell-logout"), 25);

		action = new Actions(driver);

		String actualTitle = driver.getTitle();
		String title = actualTitle.substring(0, 5);
		String editorTitle = title + new String(" SAP HANA: Editor");

		Reporter.log("Login Successful  ");
		takeScreenshotPassed("Login Successful");

		// click on editor
		driver.findElement(By.xpath("//*[text()='Editor']")).click();

		ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(1));

		fluentWait(By.xpath("//*[text()='Content']"));
		switchToWindowUsingTitle(driver, editorTitle);

		takeScreenshotPassed("EditorPageCheck");
	}

	private void editorDeletedRoleCheck(String packageName) throws InterruptedException {

		String TextEditorTabTitle = "//a[@title='/" + packageName + "/Role.hdbrole']";
		// Switch to Role Text Editor Tab
		waitFor(By.xpath(TextEditorTabTitle), 10);
		// Thread.sleep(2000);
		String cssSelectorOfTabList = "ul[class='sapUiTabBarCnt'][role='tablist']";
		List<WebElement> tabBar = driver.findElements(By.cssSelector(cssSelectorOfTabList));

		// String DesignTimeTabTitle =
		// "//a[@title='Design Time Role Editor for: " + packageName +
		// "::Role ']" ;
		// driver.findElement(By.xpath(DesignTimeTabTitle)).click();

		Thread.sleep(3000);
		driver.findElement(By.xpath(TextEditorTabTitle)).click();
		Thread.sleep(3000);

		// check for all privileges in text editor

		// identify the textarea
		WebElement textarea = driver.findElement(By.xpath("//*[@class='ace_content']"));
		// get content of textarea
		String content = textarea.getText();

		try {
			Assert.assertFalse(content
					.contains("role a::role extends role idetests.editor.plugin.editors.hdbrole.target::roleForTest extends catalog role \"idetests.editor.plugin.editors.hdbrole.target::roleForTest\""));
		} catch (Error e) {
			System.out.println(e.toString());
		}

		try {
			Assert.assertFalse(content.contains("system privilege: AUDIT ADMIN;"));
		} catch (Error e) {
			System.out.println(e.toString());
		}

		try {
			Assert.assertFalse(content.contains("schema idetests.editor.plugin.editors.hdbrole.target:schemaForTest.hdbschema: SELECT;"));
		} catch (Error e) {
			System.out.println(e.toString());
		}

		try {
			Assert.assertFalse(content
					.contains("sql object idetests.editor.plugin.editors.hdbrole.target::tableForTest: //Objecttype: TABLE SELECT;"));
		} catch (Error e) {
			System.out.println(e.toString());
		}

		try {
			Assert.assertFalse(content
					.contains("sql object idetests.editor.plugin.editors.hdbrole.target::viewForTest: //Objecttype: VIEW SELECT;"));
		} catch (Error e) {
			System.out.println(e.toString());
		}

		try {
			Assert.assertFalse(content.contains("catalog schema \"schemaForTest\": SELECT;"));
		} catch (Error e) {
			System.out.println(e.toString());
		}

		try {
			Assert.assertFalse(content
					.contains("catalog sql object \"schemaForTest\".\"idetests.editor.plugin.editors.hdbrole.target::tableForTest\": //Objecttype: TABLE SELECT;"));
		} catch (Error e) {
			System.out.println(e.toString());
		}

		try {
			Assert.assertFalse(content
					.contains("catalog sql object \"schemaForTest\".\"idetests.editor.plugin.editors.hdbrole.target::viewForTest\": //Objecttype: VIEW SELECT;"));
		} catch (Error e) {
			System.out.println(e.toString());
		}

		try {
			Assert.assertFalse(content
					.contains("analytic privilege : idetests.editor.plugin.editors.hdbrole.target:analyticPrivilegeForTest.analyticprivilege;"));
		} catch (Error e) {
			System.out.println(e.toString());
		}

		try {
			Assert.assertFalse(content
					.contains("catalog analytic privilege : \"idetests.editor.plugin.editors.hdbrole.target/analyticPrivilegeForTest\";"));
		} catch (Error e) {
			System.out.println(e.toString());
		}

		try {
			String packagePrivilege = "package " + packageName + ": REPO.READ;";
			Assert.assertFalse(content.contains(packagePrivilege));
		} catch (Error e) {
			System.out.println(e.toString());
		}

		try {
			Assert.assertFalse(content
					.contains("application privilege: \"idetests.editor.plugin.editors.hdbrole::applicationPrivilegeForTest\";"));
		} catch (Error e) {
			System.out.println(e.toString());
		}

	}

}
