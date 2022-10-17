package com.axonivy.market.test;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Named;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;

import com.axonivy.ivy.webtest.IvyWebTest;
import com.axonivy.ivy.webtest.engine.EngineUrl;

import ch.ivyteam.ivy.environment.IvyTest;
import ch.ivyteam.ivy.security.IUser;

/**
 * This Stateful Datatable Demo Web test will:
 * 1_ generate the test data in the business repo.
 * 2_ login into the system with "Tester" user.
 * 3_ start "showDatatableRepo" process
 * 4_ verify that the table of "Business Objects" is displayed.
 * 5_ add a New "Business Object" into the business repo.
 * 
 */
@IvyTest
@IvyWebTest(headless = true)
@TestMethodOrder(OrderAnnotation.class)
public class StatefulDatatableDemoWebIT {

	@Test
	@Order(1)
	public void createTestData() {
		// valid links can be copied from the start page of the internal web-browser
		open(EngineUrl.createProcessUrl("stateful-datatable-demo/183AC82DD4753247/createTestData.ivp"));
	}

	@Test
	@Order(2)
	public void showDatatableRepo(@Named("tester") IUser tester) {
		assertThat(tester.getDisplayName()).isEqualTo("Tester");
		
		open(EngineUrl.base() + "default-workflow/faces/loginTable.xhtml");

		$(By.xpath("//tr[@data-rk='Tester']")).$(By.tagName("td")).shouldBe(visible).click();
		
		// valid links can be copied from the start page of the internal web-browser
		open(EngineUrl.createProcessUrl("stateful-datatable-demo/183AC82DD4753247/showDatatableRepo.ivp"));
		// verify that the registration was successful.
		$(By.id("form:businessObjectTable")).shouldBe(visible, text("Business Objects"));
	}

	@Test
	@Order(3)
	public void addNewBusinessObject() {
		// valid links can be copied from the start page of the internal web-browser
		open(EngineUrl.createProcessUrl("stateful-datatable-demo/183AC82DD4753247/showDatatableRepo.ivp"));

		$(By.id("form:businessObjectTable:addBusinessObject")).shouldBe(enabled).click();
		// fill new customer form
		$(By.id("addBusinessObjectDialogForm:productName")).sendKeys("New test product");
		
		$(By.id("addBusinessObjectDialogForm:status")).shouldBe(enabled).click();
		$(By.id("addBusinessObjectDialogForm:status_1")).shouldBe(enabled).click();
		
		$(By.id("addBusinessObjectDialogForm:validThrough_input")).shouldBe(enabled).click();
		$(By.id("addBusinessObjectDialogForm:validThrough_panel")).$(By.linkText("24")).shouldBe(enabled).click();
		
		$(By.id("addBusinessObjectDialogForm:orderDate_input")).shouldBe(enabled).click();
		$(By.id("addBusinessObjectDialogForm:orderDate_panel")).$(By.linkText("25")).shouldBe(enabled).click();
		
		$(By.id("addBusinessObjectDialogForm:deliveryDate_input")).shouldBe(enabled).click();
		$(By.id("addBusinessObjectDialogForm:deliveryDate_panel")).$(By.linkText("26")).shouldBe(enabled).click();
		
		$(By.id("addBusinessObjectDialogForm:quantity_input")).sendKeys("10");
		
		$(By.id("addBusinessObjectDialogForm:quality")).shouldBe(enabled).click();
		$(By.id("addBusinessObjectDialogForm:quality_1")).shouldBe(enabled).click();
		
		$(By.id("addBusinessObjectDialogForm:onSale")).shouldBe(enabled).click();
		
		$(By.id("addBusinessObjectDialogForm:addBusinessObject")).shouldBe(enabled).click();
		
		// verify that the registration was successful.
		$(By.id("form:businessObjectTable")).shouldBe(visible, text("Business Objects"));
	}

}