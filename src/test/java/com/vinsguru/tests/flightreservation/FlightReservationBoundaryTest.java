package com.vinsguru.tests.flightreservation;

import com.vinsguru.pages.flightreservation.RegistrationPage;
import com.vinsguru.tests.AbstractTest;
import com.vinsguru.util.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class FlightReservationBoundaryTest extends AbstractTest {

    private String baseUrl;

    @BeforeClass
    public void setParameters(){
        this.baseUrl = ConfigReader.get("flight.app.url");
    }

    @DataProvider(name = "boundaryData")
    public Object[][] boundaryData() {
        return new Object[][]{
            // Exactly 1 char
            {"A", "B", "c@d.com", "e", "F", "G", "H"},
            // Extremely long
            {"A".repeat(100), "B".repeat(100), "C".repeat(50) + "@d.com", "e".repeat(50), "F".repeat(100), "G".repeat(100), "H".repeat(20)},
            // Mix of spaces and characters
            {" First ", " Last ", " email@test.com ", " pass ", " Street ", " City ", " Zip "}
        };
    }

    @Test(dataProvider = "boundaryData", groups = {"regression", "edge"})
    public void test_flightReservation_registration_boundaryInputs(
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
}
