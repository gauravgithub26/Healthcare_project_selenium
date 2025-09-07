package com.healthcare;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import org.testng.annotations.*;

import java.time.Duration;

public class health extends base {
    private WebDriverWait explicitWait;
    private Actions userActions;

    @BeforeClass
    public void setupBrowser() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        explicitWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        userActions = new Actions(driver);

        driver.get("https://www.westfloridaahec.org/");
        handleCookieBanner();
    }

    private void handleCookieBanner() {
        try {
            WebElement cookieDismissBtn = explicitWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.cc-btn.cc-dismiss")));
            cookieDismissBtn.click();
            System.out.println("Cookie consent dismissed successfully.");
        } catch (TimeoutException e) {
            System.out.println("No cookie banner displayed.");
        }
    }

    @Test(priority = 1)
    public void validateSearchFunctionality() throws InterruptedException {
        WebElement searchInput = explicitWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//form//label/input")));
        searchInput.sendKeys("AEHC");

        WebElement searchBtn = driver.findElement(By.xpath("//form//input[@type='submit' or @type='button']"));
        searchBtn.click();

        Thread.sleep(1500);
        returnToMainMenu(By.xpath("//*[@id='menu-item-264']/a"));
    }

    @Test(priority = 2)
    public void verifyProgramsNavigation() throws InterruptedException {
        System.out.println("Starting validation for Programs and Tobacco-related links...");

        String[][] menuPaths = {
                {"//*[@id='menu-item-344']/a", "//*[@id='post-340']//div[1]//a/h2"},
                {"", "//*[@id='post-340']//div[2]//a/h2"},
                {"", "//*[@id='post-340']//div[3]//a/h2"},
                {"//*[@id='menu-item-280']/a", ""},
                {"//*[@id='menu-item-534']/a", ""},
                {"//*[@id='menu-item-1572']/a", ""}
        };

        for (String[] path : menuPaths) {
            moveCursorTo(By.xpath("//*[@id='menu-item-264']/a"));
            if (!path[0].isEmpty()) {
                clickOnElement(By.xpath(path[0]));
            }
            if (!path[1].isEmpty()) {
                clickOnElement(By.xpath(path[1]));
            }
            returnToMainMenu(By.xpath("//*[@id='menu-item-264']/a"));
        }

        System.out.println("Programs and sub-sections navigation completed.");
    }

    @Test(priority = 3)
    public void exploreServiceLinks() throws InterruptedException {
        String[][] serviceSections = {
                {"//*[@id='menu-item-331']/a", "//*[@id='menu-item-440']/a"},
                {"//*[@id='menu-item-331']/a", "//*[@id='menu-item-330']/a"},
                {"", "//*[@id='menu-item-209']/a/span"}
        };

        for (String[] section : serviceSections) {
            if (!section[0].isEmpty()) {
                moveCursorTo(By.xpath(section[0]));
            }
            clickOnElement(By.xpath(section[1]));
            Thread.sleep(1000);
            if (!section[0].isEmpty()) {
                returnToMainMenu(By.xpath(section[0]));
            }
        }
    }

    @Test(priority = 4)
    public void testUserRegistration() throws InterruptedException {
        moveCursorTo(By.xpath("//li[a/span[text()='My Account']]/a"));
        clickOnElement(By.xpath("//li[a/span[text()='My Account']]/div/a"));

        fillInput(By.id("reg_username"), "TestUser");
        fillInput(By.id("reg_email"), "testuser@example.com");
        fillInput(By.id("reg_password"), "StrongP@ssw0rd");

        System.out.println("Registration form filled. CAPTCHA must be completed manually.");
        Thread.sleep(2000);
    }

    @AfterClass
    public void teardownBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ----------------------- Utility Methods -----------------------

    private void moveCursorTo(By locator) {
        WebElement element = explicitWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        userActions.moveToElement(element).pause(Duration.ofMillis(300)).perform();
    }

    private void clickOnElement(By locator) {
        WebElement element = explicitWait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    private void fillInput(By locator, String inputText) {
        WebElement field = explicitWait.until(ExpectedConditions.elementToBeClickable(locator));
        field.clear();
        field.sendKeys(inputText);
    }

    private void returnToMainMenu(By locatorToReappear) {
        driver.navigate().back();
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(locatorToReappear));
    }
}
