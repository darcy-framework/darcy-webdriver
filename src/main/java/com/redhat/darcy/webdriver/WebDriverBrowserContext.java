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
import com.redhat.darcy.ui.FindsByLinkText;
import com.redhat.darcy.ui.FindsByName;
import com.redhat.darcy.ui.FindsByNested;
import com.redhat.darcy.ui.FindsByXPath;
import com.redhat.darcy.ui.Locator;
import com.redhat.darcy.ui.ViewContext;
import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.web.FindsByCssSelector;
import com.redhat.darcy.web.FindsByHtmlTag;
import com.redhat.darcy.web.ManagedBrowserContext;

import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The main gateway between darcy code and WebDriver. This class takes the Browser API and forwards
 * the calls to the WebDriver operating behind the scenes. However, it is not entirely that
 * straightforward. In WebDriver, a single driver may refer to any number of open windows -- those
 * opened from the original browser window started with that driver. In darcy-web, however, a
 * Browser object must only correspond with one window, consistently. So, a {@link BrowserManager}
 * is used to silently switch between windows if different Browser objects are used that are owned
 * by the same driver. 
 */
public class WebDriverBrowserContext extends ManagedBrowserContext implements WrapsDriver,
        FindsById, FindsByName, FindsByLinkText, FindsByXPath, FindsByCssSelector,
        FindsByHtmlTag, FindsByChained, FindsByNested {
    private final WebDriverBrowserManager manager;
    private final ElementFinder finder;
    
    WebDriverBrowserContext(WebDriverBrowserManager manager, ElementFinder finder) {
        super(manager);
        
        this.manager = manager;
        this.finder = finder;
    }
    
    @Override
    public ViewContext findContext(Locator locator) {
        return manager.findContext(locator);
    }
    
    /**
     * Returns the wrapped WebDriver instance. Use judiciously. The original Browser instance may
     * still be used safely, but will incur a performance penalty at the loss of strict control
     * over the underlying WebDriver. If you open a new window by result of working with the driver
     * directly, you may get a Browser from that by using 
     * {@link com.redhat.darcy.ui.ParentContext#findContext(Locator)}, which the Browser implements.
     */
    @Override
    public WebDriver getWrappedDriver() {
        manager.flagWebDriverExposed();
        return getDriver();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T findById(Class<T> type, String id) {
        return (T) finder.findElement((Class<Element>) type, By.id(id), getDriver());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllById(Class<T> type, String id) {
        return (List<T>) finder.findElements((Class<Element>) type, By.id(id), getDriver());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllByName(Class<T> type, String name) {
        return (List<T>) finder.findElements((Class<Element>) type, By.name(name), getDriver());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T findByLinkText(Class<T> type, String linkText) {
        return (T) finder.findElement((Class<Element>) type, By.linkText(linkText), getDriver());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllByLinkText(Class<T> type, String linkText) {
        return (List<T>) finder.findElements((Class<Element>) type, By.linkText(linkText),
                getDriver());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T findByXPath(Class<T> type, String xpath) {
        return (T) finder.findElement((Class<Element>) type, By.xpath(xpath), getDriver());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllByXPath(Class<T> type, String xpath) {
        return (List<T>) finder.findElements((Class<Element>) type, By.xpath(xpath), getDriver());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllByHtmlTag(Class<T> type, String tag) {
        return (List<T>) finder.findElements((Class<Element>) type, By.tagName(tag), getDriver());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllByCssSelector(Class<T> type, String css) {
        return (List<T>) finder.findElements((Class<Element>) type, By.cssSelector(css), getDriver());
    }
    
    // TODO: If one of the elements along the way is a frame, switch to it
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

    @Override
    public <T> List<T> findAllByNested(Class<T> type, Element parent, Locator child) {
        return child.findAll(type, (ElementContext) parent);
    }
    
    /**
     * Browser objects represent a single window, but a driver can represent many. So, this method
     * not only returns the driver shrouded in the BrowserManager, but makes sure to switch to the
     * window relevant to this Browser instance before returning it.
     * 
     * @return
     */
    private WebDriver getDriver() {
        return manager.getDriver(this);
    }
}
