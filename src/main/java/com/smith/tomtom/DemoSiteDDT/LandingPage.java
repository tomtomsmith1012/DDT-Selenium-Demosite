package com.smith.tomtom.DemoSiteDDT;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LandingPage {

	@FindBy(xpath = "/html/body/div/center/table/tbody/tr[2]/td/div/center/table/tbody/tr/td[2]/p/small/a[3]")
	private WebElement addUserLink;
	
	public void clickLink() {
		addUserLink.click();
	}
}
