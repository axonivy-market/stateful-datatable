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
 * 4_ verify that the table of "Products" is displayed.
 * 5_ add a New "Product" into the business repo.
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
		$(By.id("form:productTable")).shouldBe(visible, text("List of products"));
	}

	@Test
	@Order(3)
	public void addNewProduct() {
		// valid links can be copied from the start page of the internal web-browser
		open(EngineUrl.createProcessUrl("stateful-datatable-demo/183AC82DD4753247/showDatatableRepo.ivp"));

		$(By.id("form:productTable:addProduct")).shouldBe(enabled).click();
		// fill new customer form
		$(By.id("addProductDialogForm:productName")).sendKeys("New test product");
		
		$(By.id("addProductDialogForm:status")).shouldBe(enabled).click();
		$(By.id("addProductDialogForm:status_1")).shouldBe(enabled).click();
		
		$(By.id("addProductDialogForm:validThrough_input")).shouldBe(enabled).click();
		$(By.id("addProductDialogForm:validThrough_panel")).$(By.linkText("24")).shouldBe(enabled).click();
		
		$(By.id("addProductDialogForm:orderDate_input")).shouldBe(enabled).click();
		$(By.id("addProductDialogForm:orderDate_panel")).$(By.linkText("25")).shouldBe(enabled).click();
		
		$(By.id("addProductDialogForm:deliveryDate_input")).shouldBe(enabled).click();
		$(By.id("addProductDialogForm:deliveryDate_panel")).$(By.linkText("26")).shouldBe(enabled).click();
		
		$(By.id("addProductDialogForm:quantity_input")).sendKeys("10");
		
		$(By.id("addProductDialogForm:quality")).shouldBe(enabled).click();
		$(By.id("addProductDialogForm:quality_1")).shouldBe(enabled).click();
		
		$(By.id("addProductDialogForm:onSale")).shouldBe(enabled).click();
		
		$(By.id("addProductDialogForm:addProduct")).shouldBe(enabled).click();
		
		// verify that the registration was successful.
		$(By.id("form:productTable")).shouldBe(visible, text("List of products"));
	}

}