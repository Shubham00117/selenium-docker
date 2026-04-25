package com.vinsguru.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.vinsguru.util.ConfigReader;
import com.vinsguru.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

public final class ExtentReportManager {

    private static final Logger log = LoggerFactory.getLogger(ExtentReportManager.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();
    private static ExtentReports extentReports;

    private ExtentReportManager() {
    }

    public static synchronized void initReport(String suiteName) {
        if (extentReports != null) {
            return;
        }

        try {
            DateTimeUtil.setDefaultTimeZone();
            Path reportDir = Path.of("test-output", "extent-reports");
            Files.createDirectories(reportDir);

            String reportName = "ExtentReport_" + DateTimeUtil.now(FORMATTER) + ".html";
            Path reportPath = reportDir.resolve(reportName);

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath.toString());
            sparkReporter.config().setDocumentTitle("Selenium Docker Automation Report");
            sparkReporter.config().setReportName(suiteName);

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("Project", "selenium-docker");
            extentReports.setSystemInfo("Suite", suiteName);
            extentReports.setSystemInfo("Execution", ConfigReader.get("execution"));
            extentReports.setSystemInfo("Browser", ConfigReader.get("browser"));
            extentReports.setSystemInfo("Grid URL", ConfigReader.get("grid.url"));
            extentReports.setSystemInfo("Timezone", DateTimeUtil.zoneId());
            extentReports.setSystemInfo("OS", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java", System.getProperty("java.version"));

            log.info("Extent report initialized: {}", reportPath.toAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Unable to initialize Extent report", e);
        }
    }

    public static void createTest(ITestResult result) {
        String testName = result.getTestContext().getName() + " - " + result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        ExtentTest extentTest = getExtentReports().createTest(testName, description == null ? "" : description);
        extentTest.assignCategory(result.getTestContext().getSuite().getName(), result.getTestContext().getName());
        TEST.set(extentTest);
    }

    public static void pass(String message) {
        getCurrentTest().pass(message);
    }

    public static void fail(String message, String screenshotBase64) {
        if (screenshotBase64 != null && !screenshotBase64.isBlank()) {
            getCurrentTest().fail(message, MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
        } else {
            getCurrentTest().fail(message);
        }
    }

    public static void skip(String message) {
        getCurrentTest().skip(message);
    }

    public static synchronized void flushReport() {
        if (extentReports != null) {
            extentReports.flush();
            log.info("Extent report flushed");
        }
        TEST.remove();
    }

    private static ExtentReports getExtentReports() {
        if (extentReports == null) {
            initReport("TestNG Suite");
        }
        return extentReports;
    }

    private static ExtentTest getCurrentTest() {
        ExtentTest extentTest = TEST.get();
        if (extentTest == null) {
            throw new RuntimeException("ExtentTest is not initialized for current thread");
        }
        return extentTest;
    }
}
