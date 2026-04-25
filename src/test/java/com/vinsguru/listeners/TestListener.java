package com.vinsguru.listeners;

import com.vinsguru.reports.ExtentReportManager;
import com.vinsguru.tests.AbstractTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestListener implements ITestListener {

    private static final Logger log = LoggerFactory.getLogger(TestListener.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss_SSS");
    private static final String SCREENSHOTS_FOLDER = "screenshots";

    @Override
    public void onStart(ITestContext context) {
        ExtentReportManager.initReport(context.getSuite().getName());
        log.info("Suite started: {}", context.getSuite().getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("Starting test: {}", getQualifiedTestName(result));
        ExtentReportManager.createTest(result);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("Test passed: {}", getQualifiedTestName(result));
        ExtentReportManager.pass("Test passed: " + getQualifiedTestName(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Test failed: {}", getQualifiedTestName(result), result.getThrowable());
        WebDriver driver = getDriver(result);

        if (driver == null) {
            log.warn("Skipping screenshot because WebDriver is not available for {}", getQualifiedTestName(result));
            Reporter.log("Screenshot skipped: WebDriver was not available.", true);
            ExtentReportManager.fail(getFailureMessage(result) + " | Screenshot skipped: WebDriver was not available.", "");
            return;
        }

        Screenshot screenshot = captureScreenshot(driver, result);
        if (screenshot.path() == null || screenshot.base64().isBlank()) {
            Reporter.setCurrentTestResult(result);
            Reporter.log("Screenshot capture failed. Check logs for details.", true);
            Reporter.setCurrentTestResult(null);
            ExtentReportManager.fail(getFailureMessage(result) + " | Screenshot capture failed.", "");
            return;
        }

        String screenshotPath = screenshot.path().toAbsolutePath().toString();
        Reporter.setCurrentTestResult(result);
        Reporter.log("Failure screenshot: " + screenshotPath, true);
        Reporter.log(
                "<br><a href='" + SCREENSHOTS_FOLDER + "/" + screenshot.path().getFileName() + "' target='_blank'>"
                        + "<img src='data:image/png;base64," + screenshot.base64()
                        + "' height='350' width='600' style='border:1px solid #ddd'/></a><br>",
                false
        );
        Reporter.setCurrentTestResult(null);
        ExtentReportManager.fail(getFailureMessage(result), screenshot.base64());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("Test skipped: {}", getQualifiedTestName(result), result.getThrowable());
        ExtentReportManager.skip(getSkipMessage(result));
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info(
                "Suite finished: {} | Passed: {} | Failed: {} | Skipped: {}",
                context.getSuite().getName(),
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size()
        );
        ExtentReportManager.flushReport();
    }

    private WebDriver getDriver(ITestResult result) {
        Object testInstance = result.getInstance();
        if (testInstance instanceof AbstractTest abstractTest) {
            return abstractTest.getDriver();
        }
        return null;
    }

    private Screenshot captureScreenshot(WebDriver driver, ITestResult result) {
        try {
            Path screenshotDir = getScreenshotDirectory(result);
            Files.createDirectories(screenshotDir);

            String fileName = getSafeFileName(result) + "_" + LocalDateTime.now().format(FORMATTER) + ".png";
            Path destination = screenshotDir.resolve(fileName);

            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(source.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

            String base64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            log.info("Failure screenshot saved: {}", destination.toAbsolutePath());

            return new Screenshot(destination, base64);
        } catch (IOException | RuntimeException e) {
            log.error("Unable to capture failure screenshot for {}", getQualifiedTestName(result), e);
            return new Screenshot(null, "");
        }
    }

    private String getQualifiedTestName(ITestResult result) {
        return result.getTestContext().getName() + "." + result.getMethod().getMethodName();
    }

    private String getSafeFileName(ITestResult result) {
        return getQualifiedTestName(result).replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private String getFailureMessage(ITestResult result) {
        Throwable throwable = result.getThrowable();
        return throwable == null
                ? "Test failed: " + getQualifiedTestName(result)
                : "Test failed: " + getQualifiedTestName(result) + " | " + throwable.getMessage();
    }

    private String getSkipMessage(ITestResult result) {
        Throwable throwable = result.getThrowable();
        return throwable == null
                ? "Test skipped: " + getQualifiedTestName(result)
                : "Test skipped: " + getQualifiedTestName(result) + " | " + throwable.getMessage();
    }

    private Path getScreenshotDirectory(ITestResult result) {
        String outputDirectory = result.getTestContext().getOutputDirectory();
        Path contextOutputPath = Path.of(outputDirectory);
        Path suiteOutputPath = contextOutputPath.getParent();

        if (suiteOutputPath != null && suiteOutputPath.getFileName() != null) {
            return suiteOutputPath.resolve(SCREENSHOTS_FOLDER);
        }

        return Path.of("test-output", SCREENSHOTS_FOLDER);
    }

    private record Screenshot(Path path, String base64) {
    }
}
