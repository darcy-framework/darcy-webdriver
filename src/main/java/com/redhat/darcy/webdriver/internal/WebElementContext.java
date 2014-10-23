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
import com.redhat.darcy.webdriver.WebDriverElementContext;
import com.redhat.darcy.webdriver.locators.ByPartialVisibleText;
import com.redhat.darcy.webdriver.locators.ByValue;
import com.redhat.darcy.webdriver.locators.ByVisibleText;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * An {@link com.redhat.darcy.webdriver.WebDriverElementContext} that can only find
 * {@link org.openqa.selenium.WebElement}s. This is so darcy
 * {@link com.redhat.darcy.ui.api.Locator}s can be used to find WebElements, which is necessary when
 * an element is found by chaining locators or nesting under another element.
 * <p>
 * <strong>This class only finds WebElements by design. Trying to find other types will throw a
 * class cast exception.</strong>
 *
 * @see #findAllByChained(Class, com.redhat.darcy.ui.api.Locator...)
 * @see #findAllByNested(Class, com.redhat.darcy.ui.api.elements.Element, com.redhat.darcy.ui.api.Locator)
 * @see #findByNested(Class, com.redhat.darcy.ui.api.elements.Element, com.redhat.darcy.ui.api.Locator)
 */
public class WebElementContext implements WebDriverElementContext {
    private final SearchContext sc;

    public WebElementContext(SearchContext sc) {
        this.sc = sc;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllByChained(Class<T> type, Locator... locators) {
        List<WebElement> elements = null;
        List<WebElement> subElements = new LinkedList<>();

        for (Locator locator : locators) {
            if (elements == null) {
                elements = locator.findAll(WebElement.class, this);
            } else {
                for (WebElement element : elements) {
                    subElements.addAll(
                            locator.findAll(WebElement.class, new WebElementContext(element)));
                }

                elements.clear();
                elements.addAll(subElements);
                subElements.clear();
            }
        }

        return (List<T>) elements;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findByChained(Class<T> type, Locator... locators) {
        List<WebElement> elements = findAllByChained(WebElement.class, locators);

        if (elements.isEmpty()) {
            throw new NoSuchElementException("No elements found. SearchContext: '" + sc
                    + "', Locators: " + Arrays.toString(locators));
        }

        return (T) elements.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllByCss(Class<T> type, String css) {
        return (List<T>) By.cssSelector(css).findElements(sc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findByCss(Class<T> type, String css) {
        return (T) By.cssSelector(css).findElement(sc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllByHtmlTag(Class<T> type, String tag) {
        return (List<T>) By.tagName(tag).findElements(sc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findByHtmlTag(Class<T> type, String tag) {
        return (T) By.tagName(tag).findElement(sc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllById(Class<T> type, String id) {
        return (List<T>) By.id(id).findElements(sc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findById(Class<T> type, String id) {
        return (T) By.id(id).findElement(sc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllByLinkText(Class<T> type, String linkText) {
        return (List<T>) By.linkText(linkText).findElements(sc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findByLinkText(Class<T> type, String linkText) {
        return (T) By.linkText(linkText).findElement(sc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllByName(Class<T> type, String name) {
        return (List<T>) By.name(name).findElements(sc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findByName(Class<T> type, String name) {
        return (T) By.name(name).findElement(sc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllByValue(Class<T> type, String value) {
        return (List<T>) new ByValue(value).findElements(sc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findByValue(Class<T> type, String value) {
        return (T) new ByValue(value).findElement(sc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllByNested(Class<T> type, Element parent, Locator child) {
        if (!(parent instanceof WrapsElement)) {
            throw new DarcyException("Parent element does not wrap a WebElement. Is this "
                    + "element from another automation library?");
        }

        return (List<T>) child.findAll(WebElement.class,
                new WebElementContext(((WrapsElement) parent).getWrappedElement()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findByNested(Class<T> type, Element parent, Locator child) {
        if (!(parent instanceof WrapsElement)) {
            throw new DarcyException("Parent element does not wrap a WebElement. Is this "
                    + "element from another automation library?");
        }

        return (T) child.find(WebElement.class,
                new WebElementContext(((WrapsElement) parent).getWrappedElement()));
    }

    @Override
    public WebDriverElementContext withRootLocator(Locator root) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public WebDriverElementContext withRootElement(Element root) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllByPartialTextContent(Class<T> type, String partialTextContent) {
        return (List<T>) new ByPartialVisibleText(partialTextContent).findElements(sc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findByPartialTextContent(Class<T> type, String partialTextContent) {
        return (T) new ByPartialVisibleText(partialTextContent).findElement(sc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllByTextContent(Class<T> type, String textContent) {
        return (List<T>) new ByVisibleText(textContent).findElements(sc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findByTextContent(Class<T> type, String textContent) {
        return (T) new ByVisibleText(textContent).findElement(sc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllByXPath(Class<T> type, String xpath) {
        return (List<T>) By.xpath(xpath).findElements(sc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findByXPath(Class<T> type, String xpath) {
        return (T) By.xpath(xpath).findElement(sc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllByClassName(Class<T> type, String className) {
        return (List<T>) By.className(className).findElements(sc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findByClassName(Class<T> type, String className) {
        return (T) By.className(className).findElement(sc);
    }
}
