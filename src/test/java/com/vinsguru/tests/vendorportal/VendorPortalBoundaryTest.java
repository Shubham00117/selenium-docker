package com.vinsguru.tests.vendorportal;

import com.vinsguru.pages.vendorportal.LoginPage;
import com.vinsguru.tests.AbstractTest;
import com.vinsguru.util.ConfigReader;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class VendorPortalBoundaryTest extends AbstractTest {

    private LoginPage loginPage;
    private String baseUrl;

    @BeforeClass
    public void setPageObjects(){
        this.loginPage = new LoginPage(driver);
        this.baseUrl = ConfigReader.get("vendor.app.url");
    }

    @DataProvider(name = "boundaryData")
    public Object[][] boundaryData() {
        com.vinsguru.tests.vendorportal.model.LoginData[] data = com.vinsguru.util.JsonUtil.getTestData("test-data/vendor-portal/boundary-login.json", com.vinsguru.tests.vendorportal.model.LoginData[].class);
        Object[][] result = new Object[data.length][2];
        for (int i = 0; i < data.length; i++) {
            result[i][0] = data[i].username();
            result[i][1] = data[i].password();
        }
        return result;
    }

    @Test(dataProvider = "boundaryData", groups = {"regression", "edge"})
    public void test_vendorPortal_login_boundaryInputs(String username, String password) {
        loginPage.goTo(baseUrl);
        AssertJUnit.assertTrue(loginPage.isAt());
        loginPage.login(username, password);
        AssertJUnit.assertTrue(loginPage.isAt());
    }
}
