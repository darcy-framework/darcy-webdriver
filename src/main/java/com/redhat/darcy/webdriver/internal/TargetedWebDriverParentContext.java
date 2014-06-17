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

package com.redhat.darcy.webdriver.internal;

import com.redhat.darcy.DarcyException;
import com.redhat.darcy.ui.ParentContext;
import com.redhat.darcy.web.Browser;
import com.redhat.darcy.web.Frame;
import com.redhat.darcy.webdriver.WebDriverBrowserContext;
import com.redhat.darcy.webdriver.WebDriverParentContext;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ParentContext} for {@link TargetedWebDriver}s that instantiates other 
 * {@link WebDriverBrowserContext}s with {@link TargetedWebDriver}s assigned to them that point to
 * the found driver.
 */
// TODO: Consider tracking reference of a frame's parent browser in order to forward certain methods
public class TargetedWebDriverParentContext implements WebDriverParentContext {
    private final TargetedWebDriver driver;
    private final TargetedWebDriverFactory targetedWdFactory;
    private final TargetedElementConverterFactory elementFactoryFactory;

    /**
     *
     * @param driver The targeted driver for which this context is a parent of. (Frames will be
     *               found within this target).
     * @param targetedWdFactory
     * @param elementFactoryFactory
     */
    public TargetedWebDriverParentContext(TargetedWebDriver driver, 
            TargetedWebDriverFactory targetedWdFactory,
            TargetedElementConverterFactory elementFactoryFactory) {
        this.driver = driver;
        this.targetedWdFactory = targetedWdFactory;
        this.elementFactoryFactory = elementFactoryFactory;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllById(Class<T> type, String nameOrId) {
        List<T> found = new ArrayList<>(1);
        WebDriverTarget target;
        
        if (Browser.class.isAssignableFrom(type)) {
            target = WebDriverTargets.window(nameOrId);
        } else if (Frame.class.isAssignableFrom(type)) {
            target = WebDriverTargets.frame(driver.getWebDriverTarget(), nameOrId);
        } else {
            // TODO: check for more generic types
            throw new DarcyException("Cannot find Contexts of type: " + type);
        }
        
        TargetedWebDriver targetedDriver = targetedWdFactory.getTargetedWebDriver(target);
        Browser newBrowser = new WebDriverBrowserContext(targetedDriver,
                new TargetedWebDriverParentContext(targetedDriver, targetedWdFactory,
                        elementFactoryFactory),
                new DefaultWebDriverElementContext(targetedDriver, 
                        elementFactoryFactory.newTargetedElementConverter(targetedDriver)));
        
        found.add((T) newBrowser);
        
        return found;
    }
}
