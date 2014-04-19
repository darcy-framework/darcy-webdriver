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

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

import com.redhat.darcy.ui.ElementContext;
import com.redhat.darcy.ui.FindsById;
import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.webdriver.WebDriverElementFactoryMap;

public class WebDriverElement implements Element, WrapsElement, ElementContext, FindsById {
    protected final WebElement me;
    
    public WebDriverElement(WebElement source) {
        this.me = source;
    }
    
    @Override
    public boolean isDisplayed() {
        return me.isDisplayed();
    }

    @Override
    public WebElement getWrappedElement() {
        return me;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllById(Class<T> type, String id) {
        List<WebElement> sources = By.id(id).findElements(me);
        List<T> impls = new ArrayList<>(sources.size());
        
        for (WebElement source : sources) {
            impls.add((T) WebDriverElementFactoryMap.get((Class<? extends Element>)type, source));
        }
        
        return impls;
    }
}
