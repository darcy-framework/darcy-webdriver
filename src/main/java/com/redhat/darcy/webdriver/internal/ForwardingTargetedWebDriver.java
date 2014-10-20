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

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.SessionNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ForwardingTargetedWebDriver implements TargetedWebDriver, FindsByClassName,
        FindsByCssSelector, FindsById, FindsByLinkText, FindsByName, FindsByTagName, FindsByXPath,
        TakesScreenshot, JavascriptExecutor, WrapsDriver {
    private final TargetLocator locator;
    private final WebDriverTarget target;

    /**
     * @param locator Should not be a {@link com.redhat.darcy.webdriver.internal.TargetedTargetLocator}.
     * @param target The target this driver will always refer to.
     */
    public ForwardingTargetedWebDriver(TargetLocator locator, WebDriverTarget target) {
        this.locator = locator;
        this.target = target;
    }

    @Override
    public WebDriverTarget getWebDriverTarget() {
        return target;
    }

    @Override
    public boolean isPresent() {
        try {
            driver().getTitle();
            return true;
        } catch (NotFoundException | SessionNotFoundException e) {
            return false;
        }
    }

    @Override
    public void get(String url) {
        driver().get(url);
    }

    @Override
    public String getCurrentUrl() {
        return driver().getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return driver().getTitle();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return targetedWebElements(driver().findElements(by));
    }

    @Override
    public WebElement findElement(By by) {
        return targetedWebElement(driver().findElement(by));
    }

    @Override
    public String getPageSource() {
        return driver().getPageSource();
    }

    @Override
    public void close() {
        driver().close();
    }

    @Override
    public void quit() {
        driver().quit();
    }

    @Override
    public Set<String> getWindowHandles() {
        return driver().getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return driver().getWindowHandle();
    }

    @Override
    public TargetedTargetLocator switchTo() {
        return new TargetedTargetLocator(locator, target);
    }

    // TODO: Navigation object wont be targeted
    @Override
    public Navigation navigate() {
        return driver().navigate();
    }

    // TODO: Options object wont be targeted
    @Override
    public Options manage() {
        return driver().manage();
    }

    @Override
    public WebElement findElementByClassName(String using) {
        return targetedWebElement(((FindsByClassName) driver()).findElementByClassName(using));
    }

    @Override
    public List<WebElement> findElementsByClassName(String using) {
        return targetedWebElements(((FindsByClassName) driver()).findElementsByClassName(using));
    }

    @Override
    public WebElement findElementByCssSelector(String using) {
        return targetedWebElement(((FindsByCssSelector) driver()).findElementByCssSelector(using));
    }

    @Override
    public List<WebElement> findElementsByCssSelector(String using) {
        return targetedWebElements(((FindsByCssSelector) driver()).findElementsByCssSelector(using));
    }

    @Override
    public WebElement findElementById(String using) {
        return targetedWebElement(((FindsById) driver()).findElementById(using));
    }

    @Override
    public List<WebElement> findElementsById(String using) {
        return targetedWebElements(((FindsById) driver()).findElementsById(using));
    }

    @Override
    public WebElement findElementByLinkText(String using) {
        return targetedWebElement(((FindsByLinkText) driver()).findElementByLinkText(using));
    }

    @Override
    public List<WebElement> findElementsByLinkText(String using) {
        return targetedWebElements(((FindsByLinkText) driver()).findElementsByLinkText(using));
    }

    @Override
    public WebElement findElementByPartialLinkText(String using) {
        return targetedWebElement(((FindsByLinkText) driver()).findElementByPartialLinkText(using));
    }

    @Override
    public List<WebElement> findElementsByPartialLinkText(String using) {
        return targetedWebElements(((FindsByLinkText) driver()).findElementsByPartialLinkText
                (using));
    }

    @Override
    public WebElement findElementByName(String using) {
        return targetedWebElement(((FindsByName) driver()).findElementByName(using));
    }

    @Override
    public List<WebElement> findElementsByName(String using) {
        return targetedWebElements(((FindsByName) driver()).findElementsByName(using));
    }

    @Override
    public WebElement findElementByTagName(String using) {
        return targetedWebElement(((FindsByTagName) driver()).findElementByTagName(using));
    }

    @Override
    public List<WebElement> findElementsByTagName(String using) {
        return targetedWebElements(((FindsByTagName) driver()).findElementsByTagName(using));
    }

    @Override
    public WebElement findElementByXPath(String using) {
        return targetedWebElement(((FindsByXPath) driver()).findElementByXPath(using));
    }

    @Override
    public List<WebElement> findElementsByXPath(String using) {
        return targetedWebElements(((FindsByXPath) driver()).findElementsByXPath(using));
    }

    // TODO: Protect WebElements returned from executeScript?
    @Override
    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) driver()).executeScript(script, args);
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return ((JavascriptExecutor) driver()).executeAsyncScript(script, args);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return ((TakesScreenshot) driver()).getScreenshotAs(target);
    }

    @Override
    public WebDriver getWrappedDriver() {
        return driver();
    }

    /**
     * @return The switched-to {@link org.openqa.selenium.WebDriver} instance which we can forward
     * calls to.
     */
    private WebDriver driver() {
        return target.switchTo(locator);
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
