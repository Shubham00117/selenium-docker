package com.vinsguru.pages.flightreservation;

import com.vinsguru.pages.AbstractPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class FlightsSearchPage extends AbstractPage {

    @FindBy(id = "passengers")
    private WebElement passengerSelect;

    @FindBy(id = "search-flights")
    private WebElement searchFlightsButton;

    public FlightsSearchPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isAt() {
        // wait for dropdown
        this.wait.until(ExpectedConditions.visibilityOf(passengerSelect));

        // wait for button readiness
        this.wait.until(ExpectedConditions.elementToBeClickable(searchFlightsButton));

        return passengerSelect.isDisplayed();
    }

    public void selectPassengers(String noOfPassengers) {

        Select passengers = new Select(this.passengerSelect);
        passengers.selectByValue(noOfPassengers);

        // wait for UI update after selection
        this.wait.until(ExpectedConditions.elementToBeClickable(searchFlightsButton));
    }

    public void searchFlights() {

        // wait until clickable
        this.wait.until(ExpectedConditions.elementToBeClickable(searchFlightsButton));

        // scroll into view (very important for Mac + Chrome)
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", searchFlightsButton);

        try {
            // normal click
            searchFlightsButton.click();
        } catch (Exception e) {

            System.out.println("Normal click failed → using JS click fallback");

            // fallback JS click (handles overlay issues)
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", searchFlightsButton);
        }
    }
}