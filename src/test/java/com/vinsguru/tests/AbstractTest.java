package com.vinsguru.tests;

import com.google.common.util.concurrent.Uninterruptibles;
import com.vinsguru.util.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.net.URL;
import java.time.Duration;

public abstract class AbstractTest {

    protected WebDriver driver;

    @BeforeTest
    public void setDriver() throws Exception {

        String browser = ConfigReader.get("browser");
        String execution = ConfigReader.get("execution");
        String gridUrl = ConfigReader.get("grid.url");

        System.out.println("=================================");
        System.out.println("Execution: " + execution);
        System.out.println("Browser: " + browser);
        System.out.println("=================================");

        if ("grid".equalsIgnoreCase(execution)) {

            URL url = new URL(gridUrl);

            switch (browser.toLowerCase()) {

                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setCapability("se:cdpEnabled", false);
                    this.driver = new RemoteWebDriver(url, chromeOptions);
                    break;

                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    this.driver = new RemoteWebDriver(url, firefoxOptions);
                    break;

                default:
                    throw new RuntimeException("❌ Invalid browser: " + browser);
            }

        } else if ("local".equalsIgnoreCase(execution)) {

            switch (browser.toLowerCase()) {

                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    this.driver = new ChromeDriver();
                    break;

                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    this.driver = new FirefoxDriver();
                    break;

                default:
                    throw new RuntimeException("❌ Invalid browser: " + browser);
            }

        } else {
            throw new RuntimeException("❌ Invalid execution type: " + execution);
        }

        // ✅ Stability
        this.driver.manage().window().maximize();
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterTest
    public void quitDriver() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    public WebDriver getDriver() {
        return this.driver;
    }
//    @AfterMethod
//    public void sleep() {
//    	Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(10));
//    }
}
