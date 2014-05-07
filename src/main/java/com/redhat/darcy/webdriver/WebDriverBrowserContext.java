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

import com.redhat.darcy.ui.Locator;
import com.redhat.darcy.ui.ViewContext;
import com.redhat.darcy.web.BrowserManager;
import com.redhat.darcy.web.ManagedBrowserContext;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;

/**
 * The main gateway between darcy code and WebDriver. This class takes the Browser API and forwards
 * the calls to the WebDriver operating behind the scenes. However, it is not entirely that
 * straightforward. In WebDriver, a single driver may refer to any number of open windows -- those
 * opened from the original browser window started with that driver. In darcy-web, however, a
 * Browser object must only correspond with one window, consistently. So, a {@link BrowserManager}
 * is used to silently switch between windows if different Browser objects are used that are owned
 * by the same driver. 
 */
public class WebDriverBrowserContext extends ManagedBrowserContext implements WrapsDriver,
        WebDriverElementContext {
    private final WebDriverBrowserManager manager;
    private final ElementFinder finder;
    
    WebDriverBrowserContext(WebDriverBrowserManager manager, ElementFinder finder) {
        super(manager);
        
        this.manager = manager;
        this.finder = finder;
    }
    
    @Override
    public ViewContext findContext(Locator locator) {
        return manager.findContext(locator);
    }

    @Override
    public ElementFinder finder() {
        return finder;
    }

    @Override
    public SearchContext searchContext() {
        return getDriver();
    }
    
    /**
     * Returns the wrapped WebDriver instance. Use judiciously. The original Browser instance may
     * still be used safely, but will incur a performance penalty at the loss of strict control
     * over the underlying WebDriver. If you open a new window by result of working with the driver
     * directly, you may get a Browser from that by using 
     * {@link com.redhat.darcy.ui.ParentContext#findContext(Locator)}, which the Browser implements.
     */
    @Override
    public WebDriver getWrappedDriver() {
        manager.flagWebDriverExposed();
        return getDriver();
    }
    
    /**
     * Browser objects represent a single window, but a driver can represent many. So, this method
     * not only returns the driver shrouded in the BrowserManager, but makes sure to switch to the
     * window relevant to this Browser instance before returning it.
     * 
     * @return
     */
    private WebDriver getDriver() {
        return manager.getDriver(this);
    }
}
