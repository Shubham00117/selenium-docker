package com.vinsguru.tests.flightreservation;

import com.vinsguru.pages.flightreservation.FlightsSearchPage;
import com.vinsguru.pages.flightreservation.RegistrationPage;
import com.vinsguru.pages.flightreservation.RegistrationConfirmationPage;
import com.vinsguru.tests.AbstractTest;
import com.vinsguru.util.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class FlightReservationSearchScenariosTest extends AbstractTest {

    private String baseUrl;
    private RegistrationPage registrationPage;
    private RegistrationConfirmationPage registrationConfirmationPage;
    private FlightsSearchPage flightsSearchPage;

    @BeforeClass
    public void setParameters(){
        this.baseUrl = ConfigReader.get("flight.app.url");
        this.registrationPage = new RegistrationPage(driver);
        this.registrationConfirmationPage = new RegistrationConfirmationPage(driver);
        this.flightsSearchPage = new FlightsSearchPage(driver);
    }

    @DataProvider(name = "passengerCountData")
    public Object[][] passengerCountData() {
        return new Object[][]{
            {"1"}, {"2"}, {"3"}, {"4"}, {"5"}, {"10"}, {"99"}
        };
    }

    @Test(dataProvider = "passengerCountData", groups = {"regression"})
    public void test_flightReservation_searchFlights_multiplePassengerCounts(String count) {
        registrationPage.goTo(baseUrl);
        Assert.assertTrue(registrationPage.isAt());

        registrationPage.enterUserDetails("First", "Last");
        registrationPage.enterUserCredentials("test@test.com", "pass");
        registrationPage.enterAddress("Street", "City", "12345");
        registrationPage.register();

        Assert.assertTrue(registrationConfirmationPage.isAt());
        registrationConfirmationPage.goToFlightsSearch();

        Assert.assertTrue(flightsSearchPage.isAt());
        flightsSearchPage.selectPassengers(count);
        flightsSearchPage.searchFlights();

        // The flow navigates away or stays, checking if it doesn't crash
        // Cannot assert FlightsSelectionPage here without changing state, keeping it focused on Search
    }
}
