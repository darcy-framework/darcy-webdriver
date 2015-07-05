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
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.web.api.Alert;
import com.redhat.darcy.web.api.Frame;
import com.redhat.darcy.webdriver.ElementConstructorMap;
import com.redhat.darcy.webdriver.WebDriverAlert;
import com.redhat.darcy.webdriver.WebDriverBrowser;
import com.redhat.darcy.webdriver.WebDriverParentContext;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * {@link ParentContext} for {@link TargetedWebDriver}s that instantiates other 
 * {@link com.redhat.darcy.webdriver.WebDriverBrowser}s with {@link TargetedWebDriver}s assigned to
 * them that point to the found driver.
 */
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


    @Override
    public <T> List<T> findAllById(Class<T> type, String id) {
        return findAllByNameOrId(type, id);
    }

    @Override
    public <T> T findById(Class<T> type, String id) {
        return findByNameOrId(type, id);
    }

    @Override
    public <T> List<T> findAllByName(Class<T> type, String name) {
        return findAllByNameOrId(type, name);
    }

    @Override
    public <T> T findByName(Class<T> type, String name) {
        return findByNameOrId(type, name);
    }

    @Override
    public <T> List<T> findAllByView(Class<T> type, View view) {
        return Collections.singletonList(findByView(type, view));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findByView(Class<T> type, View view) {
        if (!type.isAssignableFrom(WebDriverBrowser.class)) {
            throw new DarcyException("Cannot find contexts of type: " + type);
        }

        if (Frame.class.equals(type)) {
            throw new DarcyException("Cannot find Frames by view. Unable to iterate through all "
                    + "available frames.");
        }

        return (T) newBrowser(WebDriverTargets.withViewLoaded(view, this));
    }

    public <T> List<T> findAllByNameOrId(Class<T> type, String nameOrId) {
        return Collections.singletonList(findByNameOrId(type, nameOrId));
    }

    @SuppressWarnings("unchecked")
    public <T> T findByNameOrId(Class<T> type, String nameOrId) {
        if (!type.isAssignableFrom(WebDriverBrowser.class)) {
            throw new DarcyException("Cannot find contexts of type: " + type);
        }

        WebDriverTarget target = Frame.class.equals(type)
                ? WebDriverTargets.frame(driver.getWebDriverTarget(), nameOrId)
                : WebDriverTargets.window(nameOrId);

        return (T) newBrowser(target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TargetedWebDriverParentContext that = (TargetedWebDriverParentContext) o;
        return Objects.equals(driver, that.driver) &&
                Objects.equals(elementMap, that.elementMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driver, elementMap);
    }

    @Override
    public String toString() {
        return "TargetedWebDriverParentContext{" +
                "driver=" + driver +
                ", elementMap=" + elementMap +
                '}';
    }

    private WebDriverBrowser newBrowser(WebDriverTarget target) {
        TargetedWebDriver targetedDriver = (TargetedWebDriver) target.switchTo(driver.switchTo());

        return new WebDriverBrowser(targetedDriver,
            new TargetedWebDriverParentContext(targetedDriver, elementMap),
            new DefaultWebDriverElementContext(targetedDriver, elementMap));
    }
}
