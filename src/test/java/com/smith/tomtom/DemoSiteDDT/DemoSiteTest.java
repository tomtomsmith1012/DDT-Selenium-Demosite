package com.smith.tomtom.DemoSiteDDT;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class DemoSiteTest {
	
	WebDriver driver;
	ExtentReports report;
	ExtentTest test;
	
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", Constant.CHROMEDRIVER_PATH);
		System.setProperty("webdriver.gecko.driver", Constant.FIREFOXDRIVER_PATH);
		report = new ExtentReports(Constant.REPORT_PATH, true);
		ExcelUtils.setFile(Constant.DATA_PATH + Constant.DATA_FILE, 0);
	}
	
	@After
	public void closeDown() {
		report.flush();
	}
	
	@Test
	public void testLogin() throws IOException, InterruptedException {
		int testNumber = 1;
		int fails = 1;
		int rows = ExcelUtils.getSheetRows();
		
		for(int i = 0; i < rows; i++) {
			test = report.startTest("Database Credential Log-In. Test " + testNumber + ":");
			testNumber +=1;
			String browser = ExcelUtils.getCellData(i, 2);
			
			if(browser.equals("Chrome")) {
			driver = new ChromeDriver();
			}else if (browser.equals("Firefox")) {
			driver = new FirefoxDriver();
			}
			test.log(LogStatus.INFO, "Selecting correct browser. Using: " + browser + ".");
			
			String username = ExcelUtils.getCellData(i, 0);
			String password = ExcelUtils.getCellData(i, 1);
			test.log(LogStatus.INFO, "Getting username and password from database. Username: " + username + ", Password: " + password + ".");
			
			try {
			driver.get(Constant.HOME_URL);
			LandingPage landing = PageFactory.initElements(driver, LandingPage.class);
			landing.clickLink();
			test.log(LogStatus.INFO, "Navigating to add user page.");
			
			AddUserPage addUser = PageFactory.initElements(driver, AddUserPage.class);
			addUser.signUp(username, password);
			test.log(LogStatus.INFO, "Create user with given username and password.");
			
			LogInPage logUserIn = PageFactory.initElements(driver, LogInPage.class);
			logUserIn.logIn(username, password);
			test.log(LogStatus.INFO, "Attempting to log in with username " + username + " and password " + password + ".");
			
			if(driver.findElement(By.xpath(Constant.ELEMENT_CHECK)).getAttribute("innerHTML").equals("**Successful Login**")) {
				test.log(LogStatus.PASS, "Successfully logged in with username " + username + " and password " + password + ".");
				ExcelUtils.setCellData("Success", i, 4);
			}else {
				test.log(LogStatus.FAIL, "Failed to log in with username " + username + " and password " + password + ".");
				ExcelUtils.setCellData("Fail", i, 4);
				fails++;
			}
		
			report.endTest(test);
			driver.quit();
			
			}
			catch(UnhandledAlertException e) {
				test.log(LogStatus.FAIL, "Failed to log in with username " + username + " and password " + password + ". Username or password too short. ");
				ExcelUtils.setCellData("Fail", i, 4);
				report.endTest(test);
				driver.quit();
				fails++;
			}

		}
		
		assertEquals("At least one test failed.", 0, fails);
	}

}
