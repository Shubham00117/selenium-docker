package com.vinsguru.tests.flightreservation;

import com.vinsguru.pages.flightreservation.RegistrationPage;
import com.vinsguru.tests.AbstractTest;
import com.vinsguru.util.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class FlightReservationFormsTest extends AbstractTest {

    private String baseUrl;

    @BeforeClass
    public void setParameters(){
        this.baseUrl = ConfigReader.get("flight.app.url");
    }

    @Test(groups = {"regression", "negative"})
    public void test_flightReservation_form_emptyFirstName_shouldFail() {
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.goTo(baseUrl);
        registrationPage.enterUserDetails("", "Last");
        registrationPage.enterUserCredentials("test@test.com", "pass");
        registrationPage.enterAddress("Street", "City", "12345");
        registrationPage.register();
        Assert.assertTrue(registrationPage.isAt());
    }

    @Test(groups = {"regression", "negative"})
    public void test_flightReservation_form_emptyLastName_shouldFail() {
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.goTo(baseUrl);
        registrationPage.enterUserDetails("First", "");
        registrationPage.enterUserCredentials("test@test.com", "pass");
        registrationPage.enterAddress("Street", "City", "12345");
        registrationPage.register();
        Assert.assertTrue(registrationPage.isAt());
    }

    @Test(groups = {"regression", "negative"})
    public void test_flightReservation_form_emptyEmail_shouldFail() {
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.goTo(baseUrl);
        registrationPage.enterUserDetails("First", "Last");
        registrationPage.enterUserCredentials("", "pass");
        registrationPage.enterAddress("Street", "City", "12345");
        registrationPage.register();
        Assert.assertTrue(registrationPage.isAt());
    }

    @Test(groups = {"regression", "negative"})
    public void test_flightReservation_form_emptyPassword_shouldFail() {
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.goTo(baseUrl);
        registrationPage.enterUserDetails("First", "Last");
        registrationPage.enterUserCredentials("test@test.com", "");
        registrationPage.enterAddress("Street", "City", "12345");
        registrationPage.register();
        Assert.assertTrue(registrationPage.isAt());
    }

    @Test(groups = {"regression", "negative"})
    public void test_flightReservation_form_emptyStreet_shouldFail() {
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.goTo(baseUrl);
        registrationPage.enterUserDetails("First", "Last");
        registrationPage.enterUserCredentials("test@test.com", "pass");
        registrationPage.enterAddress("", "City", "12345");
        registrationPage.register();
        Assert.assertTrue(registrationPage.isAt());
    }

    @Test(groups = {"regression", "negative"})
    public void test_flightReservation_form_emptyCity_shouldFail() {
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.goTo(baseUrl);
        registrationPage.enterUserDetails("First", "Last");
        registrationPage.enterUserCredentials("test@test.com", "pass");
        registrationPage.enterAddress("Street", "", "12345");
        registrationPage.register();
        Assert.assertTrue(registrationPage.isAt());
    }

    @Test(groups = {"regression", "negative"})
    public void test_flightReservation_form_emptyZip_shouldFail() {
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.goTo(baseUrl);
        registrationPage.enterUserDetails("First", "Last");
        registrationPage.enterUserCredentials("test@test.com", "pass");
        registrationPage.enterAddress("Street", "City", "");
        registrationPage.register();
        Assert.assertTrue(registrationPage.isAt());
    }
}
