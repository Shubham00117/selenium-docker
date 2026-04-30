package com.vinsguru.tests.vendorportal;

import com.vinsguru.pages.vendorportal.DashboardPage;
import com.vinsguru.pages.vendorportal.LoginPage;
import com.vinsguru.tests.AbstractTest;
import com.vinsguru.util.ConfigReader;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class VendorPortalMetricsTest extends AbstractTest {

    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private String baseUrl;

    @BeforeClass
    public void setPageObjects(){
        this.loginPage = new LoginPage(driver);
        this.dashboardPage = new DashboardPage(driver);
        this.baseUrl = ConfigReader.get("vendor.app.url");
    }

    @Test(groups = {"regression"})
    public void test_vendorPortal_metrics_monthlyEarning_isVisible() {
        loginPage.goTo(baseUrl);
        com.vinsguru.tests.vendorportal.model.VendorPortalTestData testData = com.vinsguru.util.JsonUtil.getTestData("test-data/vendor-portal/sam.json", com.vinsguru.tests.vendorportal.model.VendorPortalTestData.class);
        loginPage.login(testData.username(), testData.password());
        AssertJUnit.assertTrue(dashboardPage.isAt());
        AssertJUnit.assertNotNull(dashboardPage.getMonthlyEarning());
        AssertJUnit.assertFalse(dashboardPage.getMonthlyEarning().isEmpty());
    }

    @Test(dependsOnMethods = "test_vendorPortal_metrics_monthlyEarning_isVisible", groups = {"regression"})
    public void test_vendorPortal_metrics_annualEarning_isVisible() {
        AssertJUnit.assertNotNull(dashboardPage.getAnnualEarning());
        AssertJUnit.assertFalse(dashboardPage.getAnnualEarning().isEmpty());
    }

    @Test(dependsOnMethods = "test_vendorPortal_metrics_annualEarning_isVisible", groups = {"regression"})
    public void test_vendorPortal_metrics_profitMargin_isVisible() {
        AssertJUnit.assertNotNull(dashboardPage.getProfitMargin());
        AssertJUnit.assertFalse(dashboardPage.getProfitMargin().isEmpty());
    }

    @Test(dependsOnMethods = "test_vendorPortal_metrics_profitMargin_isVisible", groups = {"regression"})
    public void test_vendorPortal_metrics_availableInventory_isVisible() {
        AssertJUnit.assertNotNull(dashboardPage.getAvailableInventory());
        AssertJUnit.assertFalse(dashboardPage.getAvailableInventory().isEmpty());
    }
    
    @Test(dependsOnMethods = "test_vendorPortal_metrics_availableInventory_isVisible", groups = {"regression"})
    public void test_vendorPortal_metrics_logout() {
        dashboardPage.logout();
        AssertJUnit.assertTrue(loginPage.isAt());
    }
}
