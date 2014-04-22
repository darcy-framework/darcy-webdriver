package com.redhat.darcy.webdriver.internal;

public interface TargetedDriverFactory {
    TargetedWebDriver getTargetedDriver(WebDriverTarget target);
}
