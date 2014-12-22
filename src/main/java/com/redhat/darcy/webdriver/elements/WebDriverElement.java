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

import com.redhat.darcy.ui.ElementNotDisplayedException;
import com.redhat.darcy.ui.FindableNotPresentException;
import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.util.Caching;
import com.redhat.darcy.web.api.elements.HtmlElement;
import com.redhat.darcy.webdriver.internal.ElementLookup;

import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WebDriverElement implements Element, Caching, HtmlElement, WrapsElement {
    /**
     * Subclasses should not really be using this directly.
     * @see #attempt(java.util.function.Consumer)
     * @see #attemptAndGet(java.util.function.Function)
     */
    protected final ElementLookup source;
    private final ElementContext context;

    private WebElement cached;

    /**
     * @param source Provides a means to lookup the backing {@link org.openqa.selenium.WebElement}
     * for this WebDriverElement.
     * @param context The context in which this element was found. Using this context will not nest
     * underneath this element (by you may do so by using {@code By.nested(this, ...);}.
     */
    public WebDriverElement(ElementLookup source, ElementContext context) {
        this.source = source;
        this.context = context;
    }

    @Override
    public boolean isDisplayed() {
        try {
            return attemptAndGet(WebElement::isDisplayed);
        } catch (FindableNotPresentException e) {
            return false;
        }
    }

    @Override
    public boolean isPresent() {
        try {
            getWrappedElement();
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public void click() {
        attempt(WebElement::click);
    }

    @Override
    public String getTagName() {
        return attemptAndGet(WebElement::getTagName);
    }

    @Override
    public String getCssValue(String property) {
        return attemptAndGet(e -> e.getCssValue(property));
    }

    @Override
    public List<String> getClasses() {
        String classList = getAttribute("class");

        if (classList == null || classList.isEmpty()) {
            return Collections.emptyList();
        }

        return Arrays.stream(classList.split(" "))
                .collect(Collectors.toList());
    }

    @Override
    public String getAttribute(String attribute) {
        return attemptAndGet(e -> e.getAttribute(attribute));
    }

    /**
     * Looks up the underlying source {@link org.openqa.selenium.WebElement}. If the element is no
     * longer present, a {@link org.openqa.selenium.NoSuchElementException} may be thrown.
     */
    @Override
    public WebElement getWrappedElement() {
        return source.lookup();
    }

    @Override
    public void invalidateCache() {
        cached = null;
    }

    @Override
    public String toString() {
        return "A WebDriverElement backed by, " + source;
    }

    /**
     * Retrieve the ElementContext that this element was found in. Intended to allow element types
     * to find other elements within the same context, most likely by nesting within themselves. For
     * example, {@code return By.nested(this, By.htmlTag("option")).findAll(SelectOption.class,
     * getContext());}.
     */
    public ElementContext getContext() {
        return context;
    }

    /**
     * Expected way for sub element types to interact with their corresponding WebElement. This
     * attempts the desired action, and will throw appropriate exceptions should the element not be
     * able to be interacted with, for whatever reason. If the WebElement is stale when the action
     * is attempted, the cached WebElement will be cleared and looked up again, which may lookup a
     * fresh reference to the equivalent element.
     *
     * @param action A function that wraps the action to be performed. Accepts the source WebElement
     * as its only parameter and returns nothing.
     */
    protected void attempt(Consumer<WebElement> action) {
        try {
            action.accept(webElement());
        } catch (StaleElementReferenceException e) {
            invalidateCache();

            try {
                action.accept(webElement());
            } catch (ElementNotVisibleException e1) {
                throw new ElementNotDisplayedException(this, e1);
            }
        } catch (ElementNotVisibleException e) {
            throw new ElementNotDisplayedException(this, e);
        }
    }

    /**
     * Expected way for sub element types to interact with their corresponding WebElement. This
     * attempts the desired action, and will throw appropriate exceptions should the element not be
     * able to be interacted with, for whatever reason. If the WebElement is stale when the action
     * is attempted, the cached WebElement will be cleared and looked up again, which may lookup a
     * fresh reference to the equivalent element.
     *
     * @param action A function that wraps the action to be performed. Accepts the source WebElement
     * as its only parameter and returns the result of this action.
     * @param <T> Return type of the action.
     * @return Whatever the action returns.
     */
    protected <T> T attemptAndGet(Function<WebElement, T> action) {
        try {
            return action.apply(webElement());
        } catch (StaleElementReferenceException e) {
            invalidateCache();

            try {
                return action.apply(webElement());
            } catch (ElementNotVisibleException e1) {
                throw new ElementNotDisplayedException(this, e1);
            }
        } catch (ElementNotVisibleException e) {
            throw new ElementNotDisplayedException(this, e);
        }
    }

    private WebElement webElement() {
        if (cached == null) {
            try {
                cached = getWrappedElement();
            } catch (NotFoundException e) {
                throw new FindableNotPresentException(this, e);
            }
        }

        return cached;
    }
}
