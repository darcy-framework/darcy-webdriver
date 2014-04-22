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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.internal.WrapsElement;

public class WebDriverElement implements Element, WrapsElement, WrapsDriver {
    private final WebElement source;
    private final WebDriver parent;
    private final ElementContext elementContext;
    
    public WebDriverElement(WebElement source, WebDriver parent, ElementContext elementContext) {
        this.source = source;
        this.parent = parent;
        this.elementContext = elementContext;
    }
    
    @Override
    public boolean isDisplayed() {
        return getWrappedElement().isDisplayed();
    }

    @Override
    public WebElement getWrappedElement() {
        return source;
    }

    @Override
    public WebDriver getWrappedDriver() {
        return parent;
    }
    
    public ElementContext getElementContext() {
        return elementContext;
    }
}
