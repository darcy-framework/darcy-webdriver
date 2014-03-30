/*
 Copyright 2014 Red Hat, Inc. and/or its affiliates.

 This file is part of darcy-webdriver.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.redhat.darcy.webdriver;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.redhat.darcy.ui.By;
import com.redhat.darcy.ui.FindsByName;
import com.redhat.darcy.ui.Locator;
import com.redhat.darcy.ui.ParentContext;
import com.redhat.darcy.ui.ViewContext;
import com.redhat.darcy.web.Browser;
import com.redhat.darcy.web.BrowserManager;

public class WebDriverBrowserManager implements BrowserManager, ParentContext, FindsByName {
    private WebDriver driver;
    private Map<Browser, String> handles = new HashMap<>();
    
    public static WebDriverBrowserContext newManagedBrowser(WebDriver driver) {
        return new WebDriverBrowserManager(driver).getBrowserForHandle(driver.getWindowHandle());
    }
    
    private WebDriverBrowserManager(WebDriver driver) {
        this.driver = driver;
        
        if (driver.getWindowHandles().size() > 1) {
            throw new IllegalStateException("Don't initialize a new WebDriverBrowserManager with a"
                    + " driver that already has multiple windows open.");
        }
    }
    
    @Override
    public void open(String url, Browser me) {
        switchTo(me);
        
        driver.get(url);
    }

    @Override
    public String getCurrentUrl(Browser me) {
        switchTo(me);
        
        return driver.getCurrentUrl();
    }

    @Override
    public void close(Browser me) {
        switchTo(me);
        
        driver.close();
    }

    @Override
    public ViewContext findContext(Locator locator) {
        return locator.find(WebDriverBrowserContext.class, this);
    }

    // TODO: Catch NoSuchWindowException and throw some higher level exception
    // Do that in findContext I think (in BrowserContext probably)
    @Override
    public <T> T findByName(Class<T> type, String name) {
        driver.switchTo().window(name);
        
        return (T) getBrowserForHandle(driver.getWindowHandle());
    }
    
    public WebDriverBrowserContext getBrowserForHandle(String handle) {
        WebDriverBrowserContext browser = new WebDriverBrowserContext(this);
        
        handles.put(browser, handle);
        
        return browser;
    }
    
    public WebDriver getDriver(Browser me) {
        switchTo(me);
        
        return driver;
    }
    
    private void switchTo(Browser browser) {
        String handle = handles.get(browser);
        
        if (handle == null) {
            throw new IllegalStateException();
        }
        
        if (!handle.equals(driver.getWindowHandle())) {
            driver.switchTo().window(handle);
        }
    }
}
