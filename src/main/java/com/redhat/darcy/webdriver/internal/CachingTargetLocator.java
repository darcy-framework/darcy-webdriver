package com.redhat.darcy.webdriver.internal;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;

/**
 * An implementation of {@link org.openqa.selenium.WebDriver.TargetLocator} that caches the current
 * target, so that attempting to switch to the same target multiple times in a row will make no
 * call to the driver.
 */
public class CachingTargetLocator implements TargetLocator {
    private WebDriverTarget currentTarget;
    private WebDriver driver;
    
    public CachingTargetLocator(WebDriverTarget currentTarget, WebDriver driver) {
        this.currentTarget = currentTarget;
        this.driver = driver;
    }
    
    public WebDriver frame(WebDriverTarget parent, int index) {
        return switchTo(WebDriverTargets.frame(parent, index));
    }
    
    public WebDriver frame(WebDriverTarget parent, String nameOrId) {
        return switchTo(WebDriverTargets.frame(parent, nameOrId));
    }
    
    public WebDriver frame(WebDriverTarget parent, WebElement frameElement) {
        return switchTo(WebDriverTargets.frame(parent, frameElement));
    }
    
    @Override
    public WebDriver frame(int index) {
        return switchTo(WebDriverTargets.frame(currentTarget, index));
    }
    
    @Override
    public WebDriver frame(String nameOrId) {
        return switchTo(WebDriverTargets.frame(currentTarget, nameOrId));
    }
    
    @Override
    public WebDriver frame(WebElement frameElement) {
        return switchTo(WebDriverTargets.frame(currentTarget, frameElement));
    }
    
    @Override
    public WebDriver window(String nameOrHandle) {
        return switchTo(WebDriverTargets.window(nameOrHandle));
    }
    
    @Override
    public WebDriver defaultContent() {
        // Can probably cache this target when we have a class for it
        currentTarget = null;
        return driver.switchTo().defaultContent();
    }
    
    @Override
    public WebElement activeElement() {
        // Subsequent calls to active element could return different elements, so make sure we
        // don't cache this
        currentTarget = null;
        return driver.switchTo().activeElement();
    }
    
    @Override
    public Alert alert() {
        // Don't cache this
        currentTarget = null;
        return driver.switchTo().alert();
    }
    
    private WebDriver switchTo(WebDriverTarget newTarget) {
        if (!newTarget.equals(currentTarget)) {
            System.out.print("Switching to new target... " + newTarget.toString() + "\n");
            newTarget.switchTo(driver.switchTo());
            currentTarget = newTarget;
        }
        
        return driver;
    }
}
