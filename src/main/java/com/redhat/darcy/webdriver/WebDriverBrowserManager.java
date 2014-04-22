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

import com.redhat.darcy.ui.FindsByName;
import com.redhat.darcy.ui.Locator;
import com.redhat.darcy.ui.ParentContext;
import com.redhat.darcy.ui.ViewContext;
import com.redhat.darcy.web.Browser;
import com.redhat.darcy.web.BrowserManager;

import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class WebDriverBrowserManager implements BrowserManager, ParentContext, FindsByName {
    private final WebDriver driver;
    private final Map<WebDriverBrowserContext, String> handles = new HashMap<>();
    private final ElementFinder finder;
    
    /**
     * A cache of the window handle the driver is currently working with. This must be updated 
     * whenever the driver's focus in changed.
     * @see #updateHandle()
     * @see #updateHandle(String)
     */
    private String currentHandle;
    private boolean driverExposed = false;
    
    public WebDriverBrowserManager(WebDriver driver, ElementFactoryMap elements) {
        if (driver.getWindowHandles().size() > 1) {
            throw new IllegalStateException("Don't initialize a new WebDriverBrowserManager with a"
                    + " driver that already has multiple windows open.");
        }
        
        this.driver = driver;
        this.finder = new ElementFinder(elements);
        
        updateHandle();
    }
    
    /**
     * Returns a WebDriverBrowserContext corresponding to the currently "focused" window. If the
     * currently focused window does not yet have a browser context, this will instantiate one for
     * it.
     * <P>
     * It is expected that {@link WebDriverBrowserManager#currentHandle} is accurate when this 
     * method is called.
     * @return
     */
    public WebDriverBrowserContext getBrowser() {
        // First check if we already know about this window
        for (Entry<WebDriverBrowserContext, String> entry : handles.entrySet()) {
            if (entry.getValue().equals(currentHandle)) {
                return entry.getKey();
            }
        }
        
        WebDriverBrowserContext browser = new WebDriverBrowserContext(this, finder);
        
        handles.put(browser, currentHandle);
        
        return browser;
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
        updateHandle();
        
        return (T) getBrowser();
    }

    @Override
    public <T> List<T> findAllByName(Class<T> type, String name) {
        Set<String> handles = driver.getWindowHandles();
        List<T> contexts = new LinkedList<>();
        
        for (String handle : handles) {
            try {
                driver.switchTo().window(handle);
                updateHandle(handle);
                
                if (driver.getTitle().equals(name)) {
                    contexts.add((T) getBrowser());
                }
            } catch (NoSuchWindowException e) {
                // This shouldn't happen because we just got the available window handles from the
                // driver. But just in case, we can just ignore it. Perhaps it was closed by 
                // something.
            }
        }
        
        return contexts;
    }
    
    public WebDriver getDriver(WebDriverBrowserContext me) {
        switchTo(me);
        
        return driver;
    }
    
    /**
     * If the underlying driver is exposed, then the user can switchTo a new window unbeknownst to
     * our cached currentHandle, invalidating it. This flag let's us know so we may force updating 
     * of the current handle before acting with a Browser.
     */
    public void flagWebDriverExposed() {
        driverExposed = true;
    }
    
    /**
     * Focus the driver on the browser object we want to work with. Checks {@link #currentHandle} to
     * see if a switch is actually required.
     * @param browser
     */
    private void switchTo(Browser browser) {
        String handle = handles.get(browser);
        
        if (handle == null) {
            throw new IllegalStateException();
        }
        
        if (driverExposed || !handle.equals(currentHandle)) {
            driver.switchTo().window(handle);
            updateHandle(handle);
        }
    }
    
    private String updateHandle(String handle) {
        currentHandle = handle;
        
        return currentHandle;
    }
    
    /**
     * Updates the current handle by asking the driver what the current window handle is. Returns 
     * it. Prefer {@link #updateHandle(String)} if you already know the handle you're switching to.
     * @return
     */
    private String updateHandle() {
        return updateHandle(driver.getWindowHandle());
    }
}
