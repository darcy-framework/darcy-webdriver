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

import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.web.api.BrowserFactory;
import com.redhat.darcy.webdriver.elements.WebDriverElement;
import com.redhat.darcy.webdriver.internal.CachingTargetLocator;
import com.redhat.darcy.webdriver.internal.DefaultWebDriverElementContext;
import com.redhat.darcy.webdriver.internal.ForwardingTargetedWebDriver;
import com.redhat.darcy.webdriver.internal.TargetedWebDriver;
import com.redhat.darcy.webdriver.internal.TargetedWebDriverParentContext;
import com.redhat.darcy.webdriver.internal.WebDriverTarget;
import com.redhat.darcy.webdriver.internal.WebDriverTargets;

import org.openqa.selenium.WebDriver;

public abstract class WebDriverBrowserFactory<T extends WebDriverBrowserFactory<T>> implements
        BrowserFactory {
    /**
     * Registers an element implementation to use for a given element type. A valid Element
     * implementation must implement that element type, extend WebDriverElement, and be creatable
     * by a method accepting a source WebElement and ElementConstructorMap (obviously this will
     * generally be the element's own constructor).
     *
     * @see ElementConstructor
     * @see com.redhat.darcy.ui.api.ElementContext
     * @see com.redhat.darcy.webdriver.elements.WebDriverElement
     */
    public abstract <E extends WebDriverElement> T withElementImplementation(Class<? super E> type,
            ElementConstructor<E> constructor);

    /**
     * Boiler plate code to take a freshly minted driver, an {@link ElementConstructorMap}, and
     * spit out a Browser.
     */
    protected static Browser makeBrowser(WebDriver driver, ElementConstructorMap elementMap) {
        WebDriverTarget target = WebDriverTargets.window(driver.getWindowHandle());

        CachingTargetLocator cachingLocator = new CachingTargetLocator(target, driver);
        TargetedWebDriver targetedDriver = new ForwardingTargetedWebDriver(cachingLocator, target);

        return new WebDriverBrowser(targetedDriver,
                new TargetedWebDriverParentContext(targetedDriver, elementMap),
                new DefaultWebDriverElementContext(targetedDriver, elementMap));
    }
}
