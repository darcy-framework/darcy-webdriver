package com.redhat.darcy.webdriver.internal;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebDriver.TargetLocator;

public interface WebDriverTarget {
    WebDriver switchTo(TargetLocator targetLocator);
}
