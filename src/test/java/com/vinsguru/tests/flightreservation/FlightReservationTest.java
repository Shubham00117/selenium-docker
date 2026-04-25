package com.vinsguru.tests.flightreservation;

import com.vinsguru.pages.flightreservation.*;
import com.vinsguru.tests.AbstractTest;
import com.vinsguru.tests.flightreservation.model.FlightReservationTestData;
import com.vinsguru.util.ConfigReader;
import com.vinsguru.util.JsonUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class FlightReservationTest extends AbstractTest {

    private FlightReservationTestData testData;
    private String baseUrl;

    @BeforeClass
    @Parameters("testDataPath")
    public void setParameters(String testDataPath){
        this.testData = JsonUtil.getTestData(testDataPath, FlightReservationTestData.class);

        // 🔥 Read URL from config.properties
        this.baseUrl = ConfigReader.get("flight.app.url");
    }

    @Test
    public void userRegistrationTest(){
        RegistrationPage registrationPage = new RegistrationPage(driver);

        registrationPage.goTo(baseUrl);
        Assert.assertTrue(registrationPage.isAt());

        registrationPage.enterUserDetails(testData.firstName(), testData.lastName());
        registrationPage.enterUserCredentials(testData.email(), testData.password());
        registrationPage.enterAddress(testData.street(), testData.city(), testData.zip());
        registrationPage.register();
    }

    @Test(dependsOnMethods = "userRegistrationTest")
    public void registrationConfirmationTest(){
        RegistrationConfirmationPage page = new RegistrationConfirmationPage(driver);

        Assert.assertTrue(page.isAt());
        Assert.assertEquals(page.getFirstName(), testData.firstName());

        page.goToFlightsSearch();
    }

    @Test(dependsOnMethods = "registrationConfirmationTest")
    public void flightsSearchTest(){
        FlightsSearchPage page = new FlightsSearchPage(driver);

        Assert.assertTrue(page.isAt());
        page.selectPassengers(testData.passengersCount());
        page.searchFlights();
    }

    @Test(dependsOnMethods = "flightsSearchTest")
    public void flightsSelectionTest(){
        FlightsSelectionPage page = new FlightsSelectionPage(driver);

        Assert.assertTrue(page.isAt());
        page.selectFlights();
        page.confirmFlights();
    }

    @Test(dependsOnMethods = "flightsSelectionTest")
    public void flightReservationConfirmationTest(){
        FlightConfirmationPage page = new FlightConfirmationPage(driver);

        Assert.assertTrue(page.isAt());
        Assert.assertEquals(page.getPrice(), testData.expectedPrice());
    }
}