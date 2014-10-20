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

import com.redhat.darcy.ui.DarcyException;
import com.redhat.darcy.ui.api.ParentContext;
import com.redhat.darcy.web.api.Alert;
import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.web.api.Frame;
import com.redhat.darcy.webdriver.ElementConstructorMap;
import com.redhat.darcy.webdriver.WebDriverAlert;
import com.redhat.darcy.webdriver.WebDriverBrowser;
import com.redhat.darcy.webdriver.WebDriverParentContext;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ParentContext} for {@link TargetedWebDriver}s that instantiates other 
 * {@link com.redhat.darcy.webdriver.WebDriverBrowser}s with {@link TargetedWebDriver}s assigned to
 * them that point to the found driver.
 */
// TODO: Consider tracking reference of a frame's parent browser in order to forward certain methods
public class TargetedWebDriverParentContext implements WebDriverParentContext {
    private final TargetedWebDriver driver;
    private final ElementConstructorMap elementMap;

    /**
     * @param driver The targeted driver for which we are finding contexts aside or under.
     * Specifically, frames will be found within the target of this driver.
     */
    public TargetedWebDriverParentContext(TargetedWebDriver driver,
            ElementConstructorMap elementMap) {
        this.driver = driver;
        this.elementMap = elementMap;
    }

    @Override
    public Alert alert() {
        return new WebDriverAlert(driver.switchTo().alert());
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
        
        TargetedWebDriver targetedDriver = (TargetedWebDriver) target.switchTo(driver.switchTo());
        Browser newBrowser = new WebDriverBrowser(targetedDriver,
                new TargetedWebDriverParentContext(targetedDriver, elementMap),
                new DefaultWebDriverElementContext(targetedDriver, elementMap));
        
        found.add((T) newBrowser);
        
        return found;
    }

    @Override
    public <T> T findById(Class<T> type, String id) {
        return findAllById(type, id).get(0);
    }
}
