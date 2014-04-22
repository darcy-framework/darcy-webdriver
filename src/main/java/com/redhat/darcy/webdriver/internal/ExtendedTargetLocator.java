package com.redhat.darcy.webdriver.internal;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;

public interface ExtendedTargetLocator extends TargetLocator {
    WebDriver nested(WebDriverTarget parent, WebDriverTarget child);
}
