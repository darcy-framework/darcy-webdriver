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
import com.redhat.darcy.ui.api.Context;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.Transition;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.internal.SimpleTransition;
import com.redhat.darcy.util.LazyList;
import com.redhat.darcy.webdriver.ElementConstructorMap;
import com.redhat.darcy.webdriver.WebDriverElementContext;
import com.redhat.darcy.webdriver.elements.WebDriverElement;
import com.redhat.darcy.webdriver.locators.ByPartialVisibleText;
import com.redhat.darcy.webdriver.locators.ByVisibleText;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultWebDriverElementContext implements WebDriverElementContext {
    private final ElementConstructorMap elementMap;
    private final Transition transition;
    private final SearchContext sc;

    public DefaultWebDriverElementContext(SearchContext context, ElementConstructorMap elementMap) {
        this.elementMap = elementMap;
        this.transition = new SimpleTransition(this);
        this.sc = context;
    }

    public DefaultWebDriverElementContext(SearchContext context, ElementConstructorMap elementMap,
            Transition transition) {
        this.elementMap = elementMap;
        this.transition = transition;
        this.sc = context;
    }

    @Override
    public Transition transition() {
        return transition;
    }

    @Override
    public <T> List<T> findAllById(Class<T> type, String id) {
        return newElementList(type, () -> By.id(id).findElements(sc));
    }

    @Override
    public <T> T findById(Class<T> type, String id) {
        return newElement(type, () -> By.id(id).findElement(sc));
    }

    @Override
    public <T> List<T> findAllByName(Class<T> type, String name) {
        return newElementList(type, () -> By.name(name).findElements(sc));
    }

    @Override
    public <T> T findByName(Class<T> type, String name) {
        return newElement(type, () -> By.name(name).findElement(sc));
    }

    @Override
    public <T> List<T> findAllByLinkText(Class<T> type, String linkText) {
        return newElementList(type, () -> By.linkText(linkText).findElements(sc));
    }

    @Override
    public <T> T findByLinkText(Class<T> type, String linkText) {
        return newElement(type, () -> By.linkText(linkText).findElement(sc));
    }

    @Override
    public <T> List<T> findAllByTextContent(Class<T> type, String textContent) {
        return newElementList(type, () -> new ByVisibleText(textContent).findElements(sc));
    }

    @Override
    public <T> T findByTextContent(Class<T> type, String textContent) {
        return newElement(type, () -> new ByVisibleText(textContent).findElement(sc));
    }

    @Override
    public <T> List<T> findAllByPartialTextContent(Class<T> type, String partialTextContent) {
        return newElementList(type,
                () -> new ByPartialVisibleText(partialTextContent).findElements(sc));
    }

    @Override
    public <T> T findByPartialTextContent(Class<T> type, String partialTextContent) {
        return newElement(type,
                () -> new ByPartialVisibleText(partialTextContent).findElement(sc));
    }

    @Override
    public <T> List<T> findAllByXPath(Class<T> type, String xpath) {
        return newElementList(type, () -> By.xpath(xpath).findElements(sc));
    }

    @Override
    public <T> T findByXPath(Class<T> type, String xpath) {
        return newElement(type, () -> By.xpath(xpath).findElement(sc));
    }

    @Override
    public <T> List<T> findAllByCss(Class<T> type, String css) {
        return newElementList(type, () -> By.cssSelector(css).findElements(sc));
    }

    @Override
    public <T> T findByCss(Class<T> type, String css) {
        return newElement(type, () -> By.cssSelector(css).findElement(sc));
    }

    @Override
    public <T> List<T> findAllByHtmlTag(Class<T> type, String tag) {
        return newElementList(type, () -> By.tagName(tag).findElements(sc));
    }

    @Override
    public <T> T findByHtmlTag(Class<T> type, String tag) {
        return newElement(type, () -> By.tagName(tag).findElement(sc));
    }

    @Override
    public <T> List<T> findAllByChained(Class<T> type, Locator... locators) {
        return new LazyList<>(() -> {
            List<T> elements = null;
            List<T> subElements = new ArrayList<>();

            for (Locator locator : locators) {
                if (elements == null) {
                    elements = locator.findAll(type, this);
                } else {
                    for (T element : elements) {
                        Context subCtx = new DefaultWebDriverElementContext(
                                ((WebDriverElement) element).getWrappedElement(), elementMap);
                        subElements.addAll(locator.findAll(type, subCtx));
                    }

                    elements.clear();
                    elements.addAll(subElements);
                    subElements.clear();
                }
            }

            return elements;
        });
    }

    @Override
    public <T> T findByChained(Class<T> type, Locator... locators) {
        return newElement(type, () -> {
            List<T> elements = findAllByChained(type, locators);

            if (elements.isEmpty()) {
                throw new NoSuchElementException("Cannot locate a element by chaining locators: " +
                        Arrays.toString(locators));
            }

            return ((WrapsElement) elements.get(0)).getWrappedElement();
        });
    }

    @Override
    public <T> List<T> findAllByNested(Class<T> type, Element parent, Locator child) {
        return newElementList(type, () -> {
            if (!(parent instanceof WrapsElement)) {
                throw new DarcyException("Parent element does not wrap a WebElement. Can only find "
                        + "by nested for fundamental UI element types found by darcy-webdriver.");
            }
            Context subCtx = new DefaultWebDriverElementContext(
                    ((WrapsElement) parent).getWrappedElement(), elementMap);

            return child.findAll(type, subCtx)
                    .stream()
                    .map(e -> ((WrapsElement) e).getWrappedElement())
                    .collect(Collectors.toList());
        });
    }

    @Override
    public <T> T findByNested(Class<T> type, Element parent, Locator child) {
        return newElement(type, () -> {
            if (!(parent instanceof WrapsElement)) {
                throw new DarcyException("Parent element does not wrap a WebElement. Can only find "
                        + "by nested for fundamental UI element types found by darcy-webdriver.");
            }
            Context subCtx = new DefaultWebDriverElementContext(
                    ((WrapsElement) parent).getWrappedElement(), elementMap);

            return ((WrapsElement) child.find(type, subCtx)).getWrappedElement();
        });
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

        return (T) elementMap.get((Class<Element>) type).newElement(source, elementMap);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> newElementList(Class<T> type, Supplier<List<WebElement>> source) {
        if (!Element.class.isAssignableFrom(type)) {
            throw new DarcyException("An ElementContext can only locate Element types: "
                    + type.toString());
        }

        return new LazyList<>(() -> source.get()
                .stream()
                .map(s -> newElement(type, () -> s))
                .collect(Collectors.toList()));
    }
}
