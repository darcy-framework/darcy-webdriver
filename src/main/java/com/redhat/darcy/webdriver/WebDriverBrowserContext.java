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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.redhat.darcy.ui.FindsByChained;
import com.redhat.darcy.ui.FindsById;
import com.redhat.darcy.ui.ViewContext;
import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.web.BrowserContext;

public class WebDriverBrowserContext extends BrowserContext implements FindsById, FindsByChained {

    public WebDriverBrowserContext(WebDriverBrowserManager manager) {
        super(manager);
    }

    @Override
    public <T extends Element> T findElement(Class<T> type, com.redhat.darcy.ui.By locator) {
        return locator.find(type, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findById(Class<T> type, String id) {
        WebElement source = By.id(id).findElement(getDriver());
        return (T) WebDriverElementFactoryMap.get((Class<? extends Element>)type, source);
    }

    @Override
    public ViewContext findContext(com.redhat.darcy.ui.By locator) {
        return ((WebDriverBrowserManager) manager).findContext(locator);
    }
    
    private WebDriver getDriver() {
        return ((WebDriverBrowserManager) manager).getDriver(this);
    }

    @Override
    public <T> T findByChained(Class<T> type, com.redhat.darcy.ui.By... bys) {
        // TODO Implement this: it is necessary for NestedViewContext to work
        // Also need to make sure if one of the elements is a frame, that searching within that
        // element switches to the frame in the driver
        return null;
    }
}
