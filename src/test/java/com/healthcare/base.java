package com.healthcare;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utility.driverfactory;

public class base {
    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = driverfactory.initDriver();
        driver.get("https://westfloridaahec.org/"); // Replace with your actual healthcare app URL
    }

    @AfterMethod
    public void tearDown() {
        driverfactory.quitDriver();
    }
}
