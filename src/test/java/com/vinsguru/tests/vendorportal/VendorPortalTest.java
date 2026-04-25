package com.vinsguru.tests.vendorportal;

import com.vinsguru.pages.vendorportal.DashboardPage;
import com.vinsguru.pages.vendorportal.LoginPage;
import com.vinsguru.tests.AbstractTest;
import com.vinsguru.tests.vendorportal.model.VendorPortalTestData;
import com.vinsguru.util.ConfigReader;
import com.vinsguru.util.JsonUtil;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class VendorPortalTest extends AbstractTest {

    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private VendorPortalTestData testData;
    private String baseUrl;

    @BeforeClass
    @Parameters("testDataPath")
    public void setPageObjects(String testDataPath){

        this.loginPage = new LoginPage(driver);
        this.dashboardPage = new DashboardPage(driver);

        this.testData = JsonUtil.getTestData(testDataPath, VendorPortalTestData.class);

        // 🔥 Read URL from config.properties
        this.baseUrl = ConfigReader.get("vendor.app.url");
    }

    @Test
    public void loginTest(){

        loginPage.goTo(baseUrl);
        AssertJUnit.assertTrue(loginPage.isAt());

        loginPage.login(testData.username(), testData.password());
    }

    @Test(dependsOnMethods = "loginTest")
    public void dashboardTest(){

        AssertJUnit.assertTrue(dashboardPage.isAt());

        AssertJUnit.assertEquals(dashboardPage.getMonthlyEarning(), testData.monthlyEarning());
        AssertJUnit.assertEquals(dashboardPage.getAnnualEarning(), testData.annualEarning());
        AssertJUnit.assertEquals(dashboardPage.getProfitMargin(), testData.profitMargin());
        AssertJUnit.assertEquals(dashboardPage.getAvailableInventory(), testData.availableInventory());

        dashboardPage.searchOrderHistoryBy(testData.searchKeyword());
        AssertJUnit.assertEquals(dashboardPage.getSearchResultsCount(), testData.searchResultsCount());
    }

    @Test(dependsOnMethods = "dashboardTest")
    public void logoutTest(){
        dashboardPage.logout();
        AssertJUnit.assertTrue(loginPage.isAt());
    }
}