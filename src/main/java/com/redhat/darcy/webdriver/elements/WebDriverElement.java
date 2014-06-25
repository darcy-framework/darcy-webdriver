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

package com.redhat.darcy.webdriver.elements;

import com.redhat.darcy.ui.ElementContext;
import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.util.Caching;
import com.redhat.darcy.webdriver.internal.DefaultWebDriverElementContext;
import com.redhat.darcy.webdriver.internal.ElementFactory;

import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

import java.util.function.Supplier;

public class WebDriverElement implements Element, Caching, WrapsElement {
    private final Supplier<WebElement> source;
    private final ElementFactory elementFactory;

    private WebElement cachedElement;

    public WebDriverElement(Supplier<WebElement> source, ElementFactory elementFactory) {
        this.source = source;
        this.elementFactory = elementFactory;
    }

    @Override
    public boolean isDisplayed() {
        try {
            return getWrappedElement().isDisplayed();
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean isPresent() {
        try {
            invalidateCache();
            getWrappedElement();
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public void invalidateCache() {
        cachedElement = null;
    }

    @Override
    public WebElement getWrappedElement() {
        if (cachedElement == null) {
            cachedElement = source.get();
        }

        return cachedElement;
    }

    public ElementContext getElementContext() {
        // Make sure to look up the element each time so that it matches the cache
        return new DefaultWebDriverElementContext(getWrappedElement(), elementFactory);
    }
}
