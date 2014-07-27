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

import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.util.LazyList;
import com.redhat.darcy.webdriver.ElementConstructorMap;
import com.redhat.darcy.webdriver.elements.WebDriverElement;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Wraps a {@link TargetedWebDriver} and creates {@link WebDriverElement}s who's source 
 * {@link WebElement}s and {@link WebDriver}s are "targeted" versions of each.
 * 
 * @see TargetedWebDriver
 * @see TargetedWebDriver#createTargetedWebElement(WebElement)
 */
public class TargetedElementFactory implements ElementFactory {
    private final TargetedWebDriver driver;
    private final ElementConstructorMap elementMap;
    
    public TargetedElementFactory(TargetedWebDriver driver, ElementConstructorMap elementMap) {
        this.driver = driver;
        this.elementMap = elementMap;
    }
    
    @Override
    public <T extends Element> T newElement(Class<T> type, WebElement source) {
        return elementMap.get(type).newElement(source, this);
    }
    
}
