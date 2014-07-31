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
import com.redhat.synq.ThrowableUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class ThreadedWebElement extends Threaded implements WebElement, FindsByClassName,
        FindsByCssSelector, FindsById, FindsByLinkText, FindsByName, FindsByTagName, FindsByXPath,
        Findable {
    private final WebElement element;

    public ThreadedWebElement(WebElement element, ExecutorService executor) {
        super(executor);

        this.element = element;
    }

    @Override
    public boolean isPresent() {
        return submitAndGet(((Findable) element)::isPresent);
    }

    @Override
    public void click() {
        submitAndWait(element::click);
    }

    @Override
    public void submit() {
        submitAndWait(element::submit);
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        submitAndWait(() -> element.sendKeys(keysToSend));
    }

    @Override
    public void clear() {
        submitAndWait(element::clear);
    }

    @Override
    public String getTagName() {
        return submitAndGet(element::getTagName);
    }

    @Override
    public String getAttribute(String name) {
        return submitAndGet(() -> element.getAttribute(name));
    }

    @Override
    public boolean isSelected() {
        return submitAndGet(element::isSelected);
    }

    @Override
    public boolean isEnabled() {
        return submitAndGet(element::isEnabled);
    }

    @Override
    public String getText() {
        return submitAndGet(element::getText);
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
        return submitAndGet(element::isDisplayed);
    }

    @Override
    public Point getLocation() {
        return submitAndGet(element::getLocation);
    }

    @Override
    public Dimension getSize() {
        return submitAndGet(element::getSize);
    }

    @Override
    public String getCssValue(String propertyName) {
        return submitAndGet(() -> element.getCssValue(propertyName));
    }

    @Override
    public WebElement findElementByClassName(String using) {
        WebElement _element = submitAndGet(
                () -> ((FindsByClassName) element).findElementByClassName(using));
        return new ThreadedWebElement(_element, executor);
    }

    @Override
    public List<WebElement> findElementsByClassName(String using) {
        List<WebElement> _elements = submitAndGet(
                () -> ((FindsByClassName) element).findElementsByClassName(using));
        return threadedWebElements(_elements);
    }

    @Override
    public WebElement findElementByCssSelector(String using) {
        WebElement _element = submitAndGet(
                () -> (FindsByCssSelector) element).findElementByCssSelector(using);
        return new ThreadedWebElement(_element, executor);
    }

    @Override
    public List<WebElement> findElementsByCssSelector(String using) {
        List<WebElement> _elements = submitAndGet(
                () -> ((FindsByCssSelector) element).findElementsByCssSelector(using));
        return threadedWebElements(_elements);
    }

    @Override
    public WebElement findElementById(String using) {
        WebElement _element = submitAndGet(
                () -> ((FindsById) element).findElementById(using));
        return new ThreadedWebElement(_element, executor);
    }

    @Override
    public List<WebElement> findElementsById(String using) {
        List<WebElement> _elements = submitAndGet(
                () -> ((FindsById) element).findElementsById(using));
        return threadedWebElements(_elements);
    }

    @Override
    public WebElement findElementByLinkText(String using) {
        WebElement _element = submitAndGet(
                () -> ((FindsByLinkText) element).findElementByLinkText(using));
        return new ThreadedWebElement(_element, executor);
    }

    @Override
    public List<WebElement> findElementsByLinkText(String using) {
        List<WebElement> _elements = submitAndGet(
                () -> ((FindsByLinkText) element).findElementsByLinkText(using));
        return threadedWebElements(_elements);
    }

    @Override
    public WebElement findElementByPartialLinkText(String using) {
        WebElement _element = submitAndGet(
                () -> ((FindsByLinkText) element).findElementByPartialLinkText(using));
        return new ThreadedWebElement(_element, executor);
    }

    @Override
    public List<WebElement> findElementsByPartialLinkText(String using) {
        List<WebElement> _elements = submitAndGet(
                () -> ((FindsByLinkText) element).findElementsByPartialLinkText(using));
        return threadedWebElements(_elements);
    }

    @Override
    public WebElement findElementByName(String using) {
        WebElement _element = submitAndGet(
                () -> ((FindsByName) element).findElementByName(using));
        return new ThreadedWebElement(_element, executor);
    }

    @Override
    public List<WebElement> findElementsByName(String using) {
        List<WebElement> _elements = submitAndGet(
                () -> ((FindsByName) element).findElementsByName(using));
        return threadedWebElements(_elements);
    }

    @Override
    public WebElement findElementByTagName(String using) {
        WebElement _element = submitAndGet(
                () -> ((FindsByTagName) element).findElementByTagName(using));
        return new ThreadedWebElement(_element, executor);
    }

    @Override
    public List<WebElement> findElementsByTagName(String using) {
        List<WebElement> _elements = submitAndGet(
                () -> ((FindsByTagName) element).findElementsByTagName(using));
        return threadedWebElements(_elements);
    }

    @Override
    public WebElement findElementByXPath(String using) {
        WebElement _element = submitAndGet(
                () -> ((FindsByXPath) element).findElementByXPath(using));
        return new ThreadedWebElement(_element, executor);
    }

    @Override
    public List<WebElement> findElementsByXPath(String using) {
        List<WebElement> _elements = submitAndGet(
                () -> ((FindsByXPath) element).findElementsByXPath(using));
        return threadedWebElements(_elements);
    }

    /**
     * Replaces all elements in a list with threaded counterparts.
     */
    private List<WebElement> threadedWebElements(List<WebElement> elements) {
        return elements
                .stream()
                .map(e -> new ThreadedWebElement(e, executor))
                .collect(Collectors.toList());
    }
}
