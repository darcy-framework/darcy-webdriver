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

import com.redhat.darcy.web.Browser;
import com.redhat.darcy.web.BrowserFactory;
import com.redhat.darcy.webdriver.elements.WebDriverElement;
import com.redhat.darcy.webdriver.internal.CachingTargetedWebDriverFactory;
import com.redhat.darcy.webdriver.internal.DefaultTargetedElementFactoryFactory;
import com.redhat.darcy.webdriver.internal.DefaultWebDriverElementContext;
import com.redhat.darcy.webdriver.internal.TargetedDriverFactory;
import com.redhat.darcy.webdriver.internal.TargetedElementFactory;
import com.redhat.darcy.webdriver.internal.TargetedElementFactoryFactory;
import com.redhat.darcy.webdriver.internal.TargetedWebDriver;
import com.redhat.darcy.webdriver.internal.TargetedWebDriverParentContext;
import com.redhat.darcy.webdriver.internal.WebDriverTarget;
import com.redhat.darcy.webdriver.internal.WebDriverTargets;

import org.openqa.selenium.WebDriver;

public abstract class WebDriverBrowserFactory<T extends WebDriverBrowserFactory<T>> implements
        BrowserFactory {
    /**
     * Registers an element implementation to use for a given element type. A valid Element
     * implementation must implement that element type, extend WebDriverElement, and accept its
     * source WebElement, parent WebDriver, and its ElementContext in its constructor. Its
     * ElementContext will only find elements within that source WebElement.
     * 
     * @see ElementConstructor
     * @see com.redhat.darcy.ui.ElementContext
     * @see com.redhat.darcy.webdriver.elements.WebDriverElement
     * @param type
     * @param constructor
     * @return
     */
    public abstract <E extends WebDriverElement> T withElementImplementation(Class<? super E> type,
            ElementConstructor<E> constructor);
    
    /**
     * Boiler plate code to take a freshly minted driver, an {@link ElementConstructorMap}, and 
     * spit out a Browser.
     * 
     * @param driver
     * @param constructorMap
     * @return
     */
    static Browser makeBrowserContext(WebDriver driver, ElementConstructorMap constructorMap) {
        WebDriverTarget target = WebDriverTargets.window(driver.getWindowHandle());
        
        TargetedDriverFactory targetedWdFactory = new CachingTargetedWebDriverFactory(driver,
                target);
        TargetedWebDriver targetedDriver = targetedWdFactory.getTargetedDriver(target);
        
        TargetedElementFactoryFactory elementFactoryFactory = new DefaultTargetedElementFactoryFactory(
                constructorMap);
        TargetedElementFactory elementFactory = elementFactoryFactory
                .newTargetedElementFactory(targetedDriver);
        
        return new WebDriverBrowserContext(targetedDriver, 
                new TargetedWebDriverParentContext(targetedDriver, targetedWdFactory, 
                        elementFactoryFactory),
                new DefaultWebDriverElementContext(targetedDriver, elementFactory));
    }
}
