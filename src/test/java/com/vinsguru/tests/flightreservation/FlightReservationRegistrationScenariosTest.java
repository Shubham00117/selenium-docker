package com.vinsguru.tests.flightreservation;

import com.vinsguru.pages.flightreservation.RegistrationPage;
import com.vinsguru.tests.AbstractTest;
import com.vinsguru.util.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class FlightReservationRegistrationScenariosTest extends AbstractTest {

    private String baseUrl;

    @BeforeClass
    public void setParameters(){
        this.baseUrl = ConfigReader.get("flight.app.url");
    }

    @DataProvider(name = "invalidRegistrationData")
    public Object[][] invalidRegistrationData() {
        return new Object[][]{
            {"", "Last", "test@test.com", "pass", "Street", "City", "12345"}, // missing first name
            {"First", "", "test@test.com", "pass", "Street", "City", "12345"}, // missing last name
            {"First", "Last", "", "pass", "Street", "City", "12345"}, // missing email
            {"First", "Last", "test@test.com", "", "Street", "City", "12345"}, // missing password
            {"First", "Last", "test@test.com", "pass", "", "City", "12345"}, // missing street
            {"First", "Last", "test@test.com", "pass", "Street", "", "12345"}, // missing city
            {"First", "Last", "test@test.com", "pass", "Street", "City", ""}, // missing zip
            {"", "", "", "", "", "", ""} // all empty
        };
    }

    @Test(dataProvider = "invalidRegistrationData", groups = {"regression", "negative"})
    public void test_flightReservation_registration_missingFields_shouldRemainOnPage(
            String firstName, String lastName, String email, String password,
            String street, String city, String zip) {
        
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.goTo(baseUrl);
        Assert.assertTrue(registrationPage.isAt());

        registrationPage.enterUserDetails(firstName, lastName);
        registrationPage.enterUserCredentials(email, password);
        registrationPage.enterAddress(street, city, zip);
        registrationPage.register();

        Assert.assertTrue(registrationPage.isAt());
    }

    @Test(groups = {"regression", "edge"})
    public void test_flightReservation_registration_specialCharacters_shouldFail() {
        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.goTo(baseUrl);
        
        registrationPage.enterUserDetails("!@#$", "%^&*");
        registrationPage.enterUserCredentials("invalidemail", "pass");
        registrationPage.enterAddress("~`", "{}|", ":\"<>?");
        registrationPage.register();
        
        Assert.assertTrue(registrationPage.isAt()); 
    }
}
