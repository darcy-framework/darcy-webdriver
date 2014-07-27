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

package com.redhat.darcy.webdriver.internal.webdriver;

import com.redhat.darcy.ui.api.elements.Findable;
import com.redhat.darcy.util.Caching;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.Point;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class RefindingWebElement implements WebElement, Caching, Findable, FindsByClassName,
        FindsByCssSelector, FindsById, FindsByLinkText, FindsByName, FindsByTagName, FindsByXPath {
    private final By by;
    private final SearchContext context;

    private WebElement cachedWebElement;

    public RefindingWebElement(By by, SearchContext context) {
        this.by = by;
        this.context = context;
    }

    @Override
    public void invalidateCache() {
        cachedWebElement = null;
    }

    private WebElement element() {
        if (cachedWebElement == null) {
            cachedWebElement = by.findElement(context);
        }

        return cachedWebElement;
    }

    /**
     * Attempts some action on the element; if the element is stale it will be refound.
     */
    private void perform(Consumer<WebElement> action) {
        try {
            action.accept(element());
        } catch (StaleElementReferenceException e) {
            invalidateCache();
            action.accept(element());
        }
    }

    /**
     * Attempts some action on the element that returns a result; if the element is stale it will be
     * refound.
     */
    private <T> T performAndGet(Function<WebElement, T> action) {
        try {
            return action.apply(element());
        } catch (StaleElementReferenceException e) {
            invalidateCache();
            return action.apply(element());
        }
    }

    @Override
    public boolean isPresent() {
        invalidateCache();

        try {
            element();
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public void click() {
        perform(WebElement::click);
    }

    @Override
    public void submit() {
        perform(WebElement::submit);
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        perform(e -> e.sendKeys(keysToSend));
    }

    @Override
    public void clear() {
        perform(WebElement::clear);
    }

    @Override
    public String getTagName() {
        return performAndGet(WebElement::getTagName);
    }

    @Override
    public String getAttribute(String name) {
        return performAndGet(e -> e.getAttribute(name));
    }

    @Override
    public boolean isSelected() {
        return performAndGet(WebElement::isSelected);
    }

    @Override
    public boolean isEnabled() {
        return performAndGet(WebElement::isEnabled);
    }

    @Override
    public String getText() {
        return performAndGet(WebElement::getText);
    }

    @Override
    public List<WebElement> findElements(By by) {
        return by.findElements(this);
    }

    @Override
    public WebElement findElement(By by) {
        return by.findElement(this);
    }

    @Override
    public boolean isDisplayed() {
        return performAndGet(WebElement::isDisplayed);
    }

    @Override
    public Point getLocation() {
        return performAndGet(WebElement::getLocation);
    }

    @Override
    public Dimension getSize() {
        return performAndGet(WebElement::getSize);
    }

    @Override
    public String getCssValue(String propertyName) {
        return performAndGet(e -> e.getCssValue(propertyName));
    }

    @Override
    public WebElement findElementByClassName(String using) {
        return performAndGet(e -> new RefindingWebElement(By.className(using), e));
    }

    @Override
    public List<WebElement> findElementsByClassName(String using) {
        List<WebElement> _elements = performAndGet(
                e -> ((FindsByClassName) e).findElementsByClassName(using));
        return refindingWebElements(_elements);
    }

    @Override
    public WebElement findElementByCssSelector(String using) {
        return performAndGet(e -> new RefindingWebElement(By.cssSelector(using), e));
    }

    @Override
    public List<WebElement> findElementsByCssSelector(String using) {
        List<WebElement> _elements = performAndGet(
                e -> ((FindsByCssSelector) e).findElementsByCssSelector(using));
        return refindingWebElements(_elements);
    }

    @Override
    public WebElement findElementById(String using) {
        return performAndGet(e -> new RefindingWebElement(By.id(using), e));
    }

    @Override
    public List<WebElement> findElementsById(String using) {
        List<WebElement> _elements = performAndGet(
                e -> ((FindsById) e).findElementsById(using));
        return refindingWebElements(_elements);
    }

    @Override
    public WebElement findElementByLinkText(String using) {
        return performAndGet(e -> new RefindingWebElement(By.linkText(using), e));
    }

    @Override
    public List<WebElement> findElementsByLinkText(String using) {
        List<WebElement> _elements = performAndGet(
                e -> ((FindsByLinkText) e).findElementsByLinkText(using));
        return refindingWebElements(_elements);
    }

    @Override
    public WebElement findElementByPartialLinkText(String using) {
        return performAndGet(e -> new RefindingWebElement(By.partialLinkText(using), e));
    }

    @Override
    public List<WebElement> findElementsByPartialLinkText(String using) {
        List<WebElement> _elements = performAndGet(
                e -> ((FindsByLinkText) e).findElementsByPartialLinkText(using));
        return refindingWebElements(_elements);
    }

    @Override
    public WebElement findElementByName(String using) {
        return performAndGet(e -> new RefindingWebElement(By.name(using), e));
    }

    @Override
    public List<WebElement> findElementsByName(String using) {
        List<WebElement> _elements = performAndGet(
                e -> ((FindsByName) e).findElementsByName(using));
        return refindingWebElements(_elements);
    }

    @Override
    public WebElement findElementByTagName(String using) {
        return performAndGet(e -> new RefindingWebElement(By.tagName(using), e));
    }

    @Override
    public List<WebElement> findElementsByTagName(String using) {
        List<WebElement> _elements = performAndGet(
                e -> ((FindsByTagName) e).findElementsByTagName(using));
        return refindingWebElements(_elements);
    }

    @Override
    public WebElement findElementByXPath(String using) {
        return performAndGet(e -> new RefindingWebElement(By.xpath(using), e));
    }

    @Override
    public List<WebElement> findElementsByXPath(String using) {
        List<WebElement> _elements = performAndGet(
                e -> ((FindsByXPath) e).findElementsByXPath(using));
        return refindingWebElements(_elements);
    }

    // TODO: How to implement?
    private List<WebElement> refindingWebElements(List<WebElement> elements) {
        return elements;
    }
}
