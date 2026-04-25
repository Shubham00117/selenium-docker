package com.vinsguru.pages.flightreservation;

import com.vinsguru.pages.AbstractPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class FlightsSelectionPage extends AbstractPage {

    @FindBy(name = "departure-flight")
    private List<WebElement> departureFlightsOptions;

    @FindBy(name = "arrival-flight")
    private List<WebElement> arrivalFlightsOptions;

    @FindBy(id = "confirm-flights")
    private WebElement confirmFlightsButton;

    public FlightsSelectionPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isAt() {
        // wait for confirm button
        this.wait.until(ExpectedConditions.visibilityOf(confirmFlightsButton));
        this.wait.until(ExpectedConditions.elementToBeClickable(confirmFlightsButton));
        return confirmFlightsButton.isDisplayed();
    }

    public void selectFlights(){

        // wait for flight options to be visible
        this.wait.until(ExpectedConditions.visibilityOfAllElements(departureFlightsOptions));
        this.wait.until(ExpectedConditions.visibilityOfAllElements(arrivalFlightsOptions));

        int size = Math.min(departureFlightsOptions.size(), arrivalFlightsOptions.size());

        if(size == 0){
            throw new RuntimeException("No flights available for selection");
        }

        // 🔥 stable selection (avoid randomness)
        int index = 0;

        WebElement departure = departureFlightsOptions.get(index);
        WebElement arrival = arrivalFlightsOptions.get(index);

        safeClick(departure);
        safeClick(arrival);
    }

    public void confirmFlights(){
        safeClick(confirmFlightsButton);
    }

    // 🔥 reusable safe click method
    private void safeClick(WebElement element){

        this.wait.until(ExpectedConditions.elementToBeClickable(element));

        // scroll into view
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

        try {
            element.click();
        } catch (Exception e) {
            System.out.println("Normal click failed → using JS click fallback");

            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", element);
        }
    }
}