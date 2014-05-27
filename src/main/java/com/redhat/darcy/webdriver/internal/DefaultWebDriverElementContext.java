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
import com.redhat.darcy.ui.Locator;
import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.webdriver.WebDriverElementContext;
import com.redhat.darcy.webdriver.elements.WebDriverElement;
import com.redhat.darcy.webdriver.locators.ByPartialVisibleText;
import com.redhat.darcy.webdriver.locators.ByVisibleText;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DefaultWebDriverElementContext implements WebDriverElementContext {
    private final SearchContext searchContext;
    private final ElementFactory elementFactory;
    
    public DefaultWebDriverElementContext(SearchContext searchContext, 
            ElementFactory elementFactory) {
        this.searchContext = searchContext;
        this.elementFactory = elementFactory;
    }
    
    @Override
    public <T> List<T> findAllById(Class<T> type, String id) {
        return findElements(type, By.id(id));
    }
    
    @Override
    public <T> List<T> findAllByName(Class<T> type, String name) {
        return findElements(type, By.name(name));
    }
    
    @Override
    public <T> List<T> findAllByLinkText(Class<T> type, String linkText) {
        return findElements(type, By.linkText(linkText));
    }
    
    @Override
    public <T> List<T> findAllByTextContent(Class<T> type, String textContent) {
        return findElements(type, new ByVisibleText(textContent));
    }
    
    @Override
    public <T> List<T> findAllByPartialTextContent(Class<T> type, String partialTextContent) {
        return findElements(type, new ByPartialVisibleText(partialTextContent));
    }
    
    @Override
    public <T> List<T> findAllByXPath(Class<T> type, String xpath) {
        return findElements(type, By.xpath(xpath));
    }
    
    @Override
    public <T> List<T> findAllByCssSelector(Class<T> type, String css) {
        return findElements(type, By.cssSelector(css));
    }
    
    @Override
    public <T> List<T> findAllByHtmlTag(Class<T> type, String tag) {
        return findElements(type, By.tagName(tag));
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
                    subElements.addAll(
                            locator.findAll(
                                    type,
                                    new DefaultWebDriverElementContext(
                                            ((WebDriverElement) element).getWrappedElement(), 
                                            elementFactory)));
                }
                
                elements.clear();
                elements.addAll(subElements);
                subElements.clear();
            }
        }
        
        return elements;
    }
    
    @Override
    public <T> List<T> findAllByNested(Class<T> type, Element parent, Locator child) {
        if (!(parent instanceof WebDriverElement)) {
            throw new DarcyException("Cannot cast element to WebDriverElement. Is the parent "
                    + "from another automation library? " + parent.toString());
        }
        
        return child.findAll(type, new DefaultWebDriverElementContext(
                ((WebDriverElement) parent).getWrappedElement(), elementFactory));
    }
    
    @SuppressWarnings("unchecked")
    private <T> T findElement(Class<T> type, By by) {
        if (!Element.class.isAssignableFrom(type)) {
            throw new DarcyException("An ElementContext can only locate Element types: " 
                    + type.toString());
        }
        
        return (T) elementFactory.newElement(
                (Class<Element>) type, 
                by.findElement(searchContext));
    }
  
    @SuppressWarnings("unchecked")
    private <T> List<T> findElements(Class<T> type, By by) {
        if (!Element.class.isAssignableFrom(type)) {
            throw new DarcyException("An ElementContext can only locate Element types: " 
                    + type.toString());
        }
        
        return (List<T>) elementFactory.newElementList(
                (Class<Element>) type, 
                by.findElements(searchContext));
    }
}
