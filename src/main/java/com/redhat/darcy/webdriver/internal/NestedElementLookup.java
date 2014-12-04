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

import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.webdriver.ElementConstructorMap;
import com.redhat.darcy.webdriver.WebDriverElementContext;
import com.redhat.darcy.webdriver.elements.WebDriverElement;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

public class NestedElementLookup implements ElementLookup {
    private final WebDriverElement parent;
    private final Locator child;

    private static final ElementConstructorMap elMap = ElementConstructorMap.webDriverElementOnly();

    public NestedElementLookup(WebDriverElement parent, Locator child) {
        this.parent = parent;
        this.child = child;
    }

    @Override
    public WebElement lookup() {
        try {
            return actualLookup();
        } catch (StaleElementReferenceException e) {
            parent.invalidateCache();

            return actualLookup();
        }
    }

    @Override
    public String toString() {
        return "An element found nested within another element.\n" +
                "The parent element is a " + parent + ".\n" +
                "The locator to find the child element is " + child;
    }

    private WebElement actualLookup() {
        WebDriverElement element = child.find(WebDriverElement.class, getNestedContext());
        return element.getWrappedElement();
    }

    private WebDriverElementContext getNestedContext() {
        return new DefaultWebDriverElementContext(parent.getWrappedElement(), elMap);
    }
}
