package com.vinsguru.tests.flightreservation;

import com.vinsguru.tests.AbstractTest;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

public class FlightReservationPerformanceTest extends AbstractTest {

    @Test
    public void searchResponseTimeTest() {
        // Dummy test for validating that flight search returns within SLA
        AssertJUnit.assertTrue(true);
    }

    @Test
    public void pageLoadTimeTest() {
        // Dummy test for checking the initial page load time 
        AssertJUnit.assertTrue(true);
    }
}
