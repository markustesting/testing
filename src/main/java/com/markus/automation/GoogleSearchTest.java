package com.markus.automation; // Make sure this matches your package name

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;

// Import for waits (Highly Recommended for real tests)
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration; // For Duration in WebDriverWait

public class GoogleSearchTest {

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Selenium_Drivers\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        try {
            System.out.println("Navigating to Google...");
            driver.get("https://www.google.com");
            System.out.println("Initial Page Title: " + driver.getTitle()); // Corrected variable name for clarity

            WebElement searchBox = driver.findElement(By.name("q"));
            System.out.println("Found search box.");

            String searchQuery = "Selenium Java automation tutorial";
            searchBox.sendKeys(searchQuery);
            System.out.println("Typed: '" + searchQuery + "' into search box.");

            searchBox.sendKeys(Keys.ENTER);
            System.out.println("Pressed Enter.");

            // --- IMPORTANT: Replace Thread.sleep with WebDriverWait for reliability ---
            // Create a WebDriverWait instance with a timeout (e.g., 10 seconds)
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));

            // Wait until the title of the page *contains* our search query
            wait.until(ExpectedConditions.titleContains(searchQuery));
            System.out.println("Page title updated, search results loaded.");

            // Get the actual new page title AFTER the wait
            String actualNewPageTitle = driver.getTitle(); // Retrieve the title again after the page has loaded
            System.out.println("Actual New Page Title: " + actualNewPageTitle);

            // Now, perform the assertion using the actual page title
            if (actualNewPageTitle.contains(searchQuery)) {
                System.out.println("Test Passed: Search results page title contains the query.");
            } else {
                System.out.println("Test Failed: Search results page title does NOT contain the query.");
                System.out.println("Expected to contain: '" + searchQuery + "'");
                System.out.println("Actual title was: '" + actualNewPageTitle + "'");
            }

        } catch (Exception e) {
            System.err.println("An error occurred during automation: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                System.out.println("Closing browser...");
                driver.quit();
                System.out.println("Browser closed.");
            }
        }
    }
}