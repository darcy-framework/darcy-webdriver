package com.redhat.darcy.webdriver.internal;

import com.redhat.darcy.web.Alert;

import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

public class WebDriverAlert implements Alert {
    private final WebDriver driver;
    private org.openqa.selenium.Alert cachedAlert;
    
    public WebDriverAlert(WebDriver driver) {
        this.driver = driver;
    }
    
    @Override
    public boolean isPresent() {
        try {
            cachedAlert = null;
            alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }
    
    @Override
    public void accept() {
        alert().accept();
    }
    
    @Override
    public void dismiss() {
        alert().dismiss();
    }
    
    @Override
    public void sendKeys(String keysToSend) {
        alert().sendKeys(keysToSend);
    }
    
    @Override
    public String readText() {
        return alert().getText();
    }
    
    private org.openqa.selenium.Alert alert() {
        if (cachedAlert == null) {
            cachedAlert = driver.switchTo().alert();
        }
        
        // TODO: What happens if alert gets cached, user switches to something else, then cached
        // alert is used?
        return cachedAlert;
    }
}
