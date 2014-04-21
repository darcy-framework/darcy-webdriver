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

import com.redhat.darcy.ui.ElementContext;
import com.redhat.darcy.ui.FindsByChained;
import com.redhat.darcy.ui.FindsById;
import com.redhat.darcy.ui.Locator;
import com.redhat.darcy.ui.ViewContext;
import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.web.BrowserContext;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WebDriverBrowserContext extends BrowserContext implements FindsById, FindsByChained {
    private final WebDriverBrowserManager manager;
    private final ElementFactoryMap elements;
    
    WebDriverBrowserContext(WebDriverBrowserManager manager, ElementFactoryMap elements) {
        super(manager);
        
        this.manager = manager;
        this.elements = elements;
    }

    @Override
    public ViewContext findContext(Locator locator) {
        return manager.findContext(locator);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findById(Class<T> type, String id) {
        WebElement source = getDriver().findElement(By.id(id));
        return (T) elements.getElement((Class<Element>) type, source);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllById(Class<T> type, String id) {
        List<WebElement> sources = getDriver().findElements(By.id(id));
        List<T> impls = new ArrayList<>(sources.size());
        
        for (WebElement source : sources) {
            impls.add((T) elements.getElement((Class<Element>) type, source));
        }
        
        return impls;
    }
    
    @Override
    public <T> List<T> findAllByChained(Class<T> type, Locator... locators) {
        if (locators.length == 0) {
            return new ArrayList<T>(0);
        }
        
        List<T> elements = null;
        List<T> subElements = new LinkedList<>();
        
        for (Locator locator : locators) {
            if (elements == null) {
                elements = locator.findAll(type, this);
            } else {
                for (T element : elements) {
                    subElements.addAll(locator.findAll(type, (ElementContext) element));
                }
                
                elements = subElements;
                subElements.clear();
            }
        }
        
        return elements;
    }
    
    private WebDriver getDriver() {
        return manager.getDriver(this);
    }
}
