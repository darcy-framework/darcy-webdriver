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

import static org.openqa.selenium.WebDriver.TargetLocator;

import com.redhat.darcy.ui.api.elements.Findable;
import com.redhat.darcy.webdriver.internal.WebDriverTarget;

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
import java.util.stream.Collectors;

public class TargetedWebElement implements WebElement, FindsByClassName,
        FindsByCssSelector, FindsById, FindsByLinkText, FindsByName, FindsByTagName, FindsByXPath,
        Findable {
    private final TargetLocator locator;
    private final WebDriverTarget target;
    private final WebElement element;

    public TargetedWebElement(TargetLocator locator, WebDriverTarget target,
            WebElement element) {
        this.locator = locator;
        this.target = target;
        this.element = element;
    }

    @Override
    public boolean isPresent() {
        return ((Findable) element()).isPresent();
    }

    @Override
    public void click() {
        element().click();
    }

    @Override
    public void submit() {
        element().submit();
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        element().sendKeys(keysToSend);
    }

    @Override
    public void clear() {
        element().clear();
    }

    @Override
    public String getTagName() {
        return element().getTagName();
    }

    @Override
    public String getAttribute(String name) {
        return element().getAttribute(name);
    }

    @Override
    public boolean isSelected() {
        return element().isSelected();
    }

    @Override
    public boolean isEnabled() {
        return element().isEnabled();
    }

    @Override
    public String getText() {
        return element().getText();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return targetedWebElements(element().findElements(by));
    }

    @Override
    public WebElement findElement(By by) {
        return targetedWebElement(element().findElement(by));
    }

    @Override
    public boolean isDisplayed() {
        return element().isDisplayed();
    }

    @Override
    public Point getLocation() {
        return element().getLocation();
    }

    @Override
    public Dimension getSize() {
        return element().getSize();
    }

    @Override
    public String getCssValue(String propertyName) {
        return element().getCssValue(propertyName);
    }

    @Override
    public WebElement findElementByClassName(String using) {
        return targetedWebElement(((FindsByClassName) element()).findElementByClassName(using));
    }

    @Override
    public List<WebElement> findElementsByClassName(String using) {
        return targetedWebElements(((FindsByClassName) element()).findElementsByClassName(using));
    }

    @Override
    public WebElement findElementByCssSelector(String using) {
        return targetedWebElement(((FindsByCssSelector) element()).findElementByCssSelector(using));
    }

    @Override
    public List<WebElement> findElementsByCssSelector(String using) {
        return targetedWebElements(((FindsByCssSelector) element()).findElementsByCssSelector(using));
    }

    @Override
    public WebElement findElementById(String using) {
        return targetedWebElement(((FindsById) element()).findElementById(using));
    }

    @Override
    public List<WebElement> findElementsById(String using) {
        return targetedWebElements(((FindsById) element()).findElementsById(using));
    }

    @Override
    public WebElement findElementByLinkText(String using) {
        return targetedWebElement(((FindsByLinkText) element()).findElementByLinkText(using));
    }

    @Override
    public List<WebElement> findElementsByLinkText(String using) {
        return targetedWebElements(((FindsByLinkText) element()).findElementsByLinkText(using));
    }

    @Override
    public WebElement findElementByPartialLinkText(String using) {
        return targetedWebElement(((FindsByLinkText) element()).findElementByPartialLinkText(using));
    }

    @Override
    public List<WebElement> findElementsByPartialLinkText(String using) {
        return targetedWebElements(((FindsByLinkText) element()).findElementsByPartialLinkText(using));
    }

    @Override
    public WebElement findElementByName(String using) {
        return targetedWebElement(((FindsByName) element()).findElementByName(using));
    }

    @Override
    public List<WebElement> findElementsByName(String using) {
        return targetedWebElements(((FindsByName) element()).findElementsByName(using));
    }

    @Override
    public WebElement findElementByTagName(String using) {
        return targetedWebElement(((FindsByTagName) element()).findElementByTagName(using));
    }

    @Override
    public List<WebElement> findElementsByTagName(String using) {
        return targetedWebElements(((FindsByTagName) element()).findElementsByTagName(using));
    }

    @Override
    public WebElement findElementByXPath(String using) {
        return targetedWebElement(((FindsByXPath) element()).findElementByXPath(using));
    }

    @Override
    public List<WebElement> findElementsByXPath(String using) {
        return targetedWebElements(((FindsByXPath) element()).findElementsByXPath(using));
    }

    private WebElement element() {
        target.switchTo(locator);
        return element;
    }

    private WebElement targetedWebElement(WebElement element) {
        return new TargetedWebElement(locator, target, element);
    }

    private List<WebElement> targetedWebElements(List<WebElement> elements) {
        return elements
                .stream()
                .map(e -> new TargetedWebElement(locator, target, e))
                .collect(Collectors.toList());
    }
}
