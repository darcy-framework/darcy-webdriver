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
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.util.LazyList;
import com.redhat.darcy.webdriver.ElementConstructorMap;
import com.redhat.darcy.webdriver.WebDriverElementContext;
import com.redhat.darcy.webdriver.elements.WebDriverElement;
import com.redhat.darcy.webdriver.locators.ByChained;
import com.redhat.darcy.webdriver.locators.ByPartialVisibleText;
import com.redhat.darcy.webdriver.locators.ByValue;
import com.redhat.darcy.webdriver.locators.ByVisibleText;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultWebDriverElementContext implements WebDriverElementContext {
    private final ElementConstructorMap elementMap;
    private final SearchContext sc;

    public DefaultWebDriverElementContext(SearchContext context, ElementConstructorMap elementMap) {
        this.elementMap = elementMap;
        this.sc = context;
    }

    @Override
    public <T> List<T> findAllById(Class<T> type, String id) {
        return newElementList(type, new WebElementListLookup(By.id(id), sc));
    }

    @Override
    public <T> T findById(Class<T> type, String id) {
        return newElement(type, new WebElementLookup(By.id(id), sc));
    }

    @Override
    public <T> List<T> findAllByName(Class<T> type, String name) {
        return newElementList(type, new WebElementListLookup(By.name(name), sc));
    }

    @Override
    public <T> T findByName(Class<T> type, String name) {
        return newElement(type, new WebElementLookup(By.name(name), sc));
    }

    @Override
    public <T> List<T> findAllByLinkText(Class<T> type, String linkText) {
        return newElementList(type, new WebElementListLookup(By.linkText(linkText), sc));
    }

    @Override
    public <T> T findByLinkText(Class<T> type, String linkText) {
        return newElement(type, new WebElementLookup(By.linkText(linkText), sc));
    }

    @Override
    public <T> List<T> findAllByTextContent(Class<T> type, String textContent) {
        return newElementList(type, new WebElementListLookup(new ByVisibleText(textContent), sc));
    }

    @Override
    public <T> T findByTextContent(Class<T> type, String textContent) {
        return newElement(type, new WebElementLookup(new ByVisibleText(textContent), sc));
    }

    @Override
    public <T> List<T> findAllByPartialTextContent(Class<T> type, String partialTextContent) {
        By by = new ByPartialVisibleText(partialTextContent);
        ElementListLookup listLookup = new WebElementListLookup(by, sc);
        return newElementList(type, listLookup);
    }

    @Override
    public <T> T findByPartialTextContent(Class<T> type, String partialTextContent) {
        By by = new ByPartialVisibleText(partialTextContent);
        return newElement(type, new WebElementLookup(by, sc));
    }

    @Override
    public <T> List<T> findAllByXPath(Class<T> type, String xpath) {
        return newElementList(type, new WebElementListLookup(By.xpath(xpath), sc));
    }

    @Override
    public <T> T findByXPath(Class<T> type, String xpath) {
        return newElement(type, new WebElementLookup(By.xpath(xpath), sc));
    }

    @Override
    public <T> List<T> findAllByCss(Class<T> type, String css) {
        return newElementList(type, new WebElementListLookup(By.cssSelector(css), sc));
    }

    @Override
    public <T> T findByCss(Class<T> type, String css) {
        return newElement(type, new WebElementLookup(By.cssSelector(css), sc));
    }

    @Override
    public <T> List<T> findAllByHtmlTag(Class<T> type, String tag) {
        return newElementList(type, new WebElementListLookup(By.tagName(tag), sc));
    }

    @Override
    public <T> T findByHtmlTag(Class<T> type, String tag) {
        return newElement(type, new WebElementLookup(By.tagName(tag), sc));
    }

    @Override
    public <T> List<T> findAllByClassName(Class<T> type, String className) {
        return newElementList(type, new WebElementListLookup(By.className(className), sc));
    }

    @Override
    public <T> T findByClassName(Class<T> type, String className) {
        return newElement(type, new WebElementLookup(By.className(className), sc));
    }

    @Override
    public <T> List<T> findAllByChained(Class<T> type, Locator... locators) {
        return newElementList(type, new WebElementListLookup(new ByChained(locators), sc));
    }

    @Override
    public <T> T findByChained(Class<T> type, Locator... locators) {
        return newElement(type, new WebElementLookup(new ByChained(locators), sc));
    }

    @Override
    public <T> List<T> findAllByNested(Class<T> type, Element parent, Locator child) {
        if (!(parent instanceof WebDriverElement)) {
            throw new DarcyException("Parent element is not a WebDriverElement. Can only find " +
                    "by nested for fundamental UI element types found by darcy-webdriver.");
        }

        return newElementList(type, new NestedElementListLookup((WebDriverElement) parent, child));
    }

    @Override
    public <T> T findByNested(Class<T> type, Element parent, Locator child) {
        if (!(parent instanceof WebDriverElement)) {
            throw new DarcyException("Parent element is not a WebDriverElement. Can only find " +
                    "by nested for fundamental UI element types found by darcy-webdriver.");
        }

        return newElement(type, new NestedElementLookup((WebDriverElement) parent, child));
    }

    @Override
    public <T> List<T> findAllByValue(Class<T> type, String value) {
        return newElementList(type, new WebElementListLookup(new ByValue(value), sc));
    }

    @Override
    public <T> T findByValue(Class<T> type, String value) {
        return newElement(type, new WebElementLookup(new ByValue(value), sc));
    }

    @Override
    public WebDriverElementContext withRootLocator(Locator root) {
        return new ChainedWebDriverElementContext(root, this);
    }

    @Override
    public WebDriverElementContext withRootElement(Element root) {
        // Reuse the original transition to prevent the view transitioned to from having a context
        // also nested under some root element
        return new NestedWebDriverElementContext(root, this);
    }

    @SuppressWarnings("unchecked")
    private <T> T newElement(Class<T> type, ElementLookup source) {
        if (!Element.class.isAssignableFrom(type)) {
            throw new DarcyException("An ElementContext can only locate Element types: "
                    + type.toString());
        }

        return (T) elementMap.get((Class<Element>) type).newElement(source, this);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> newElementList(Class<T> type, ElementListLookup source) {
        if (!Element.class.isAssignableFrom(type)) {
            throw new DarcyException("An ElementContext can only locate Element types: "
                    + type.toString());
        }

        return new LazyList<>(() -> source.lookup()
                .stream()
                .map(e -> newElement(type, new ListElementLookup(source, e)))
                .collect(Collectors.toList()));
    }
}
