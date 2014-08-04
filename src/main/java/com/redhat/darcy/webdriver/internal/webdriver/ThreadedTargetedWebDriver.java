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

import com.redhat.darcy.webdriver.internal.TargetedWebDriver;
import com.redhat.darcy.webdriver.internal.WebDriverTarget;
import com.redhat.synq.ThrowableUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * Implements all common WebDriver interfaces, forwarding to some underlying WebDriver
 * implementation. All commands however are sent through an executor service so access to the driver
 * (and WebElements) can be restricted to one thread.
 * <p>
 * All objects returned as results of these methods that may interact with the browser or driver,
 * are also threaded and share the same executor in a similar pattern.
 * <p>
 * This class implements {@link org.openqa.selenium.internal.WrapsDriver}, which may be used to
 * retrieve the underlying original driver that is not protected against multiple threads.
 *
 * @see com.redhat.darcy.webdriver.internal.webdriver.ThreadedWebElement
 * @see com.redhat.darcy.webdriver.internal.webdriver.ThreadedOptions
 * @see com.redhat.darcy.webdriver.internal.webdriver.ThreadedTargetLocator
 * @see com.redhat.darcy.webdriver.internal.webdriver.ThreadedNavigation
 */
public class ThreadedTargetedWebDriver extends Threaded implements TargetedWebDriver,
        FindsByClassName, FindsByCssSelector, FindsById, FindsByLinkText, FindsByName,
        FindsByTagName, FindsByXPath, TakesScreenshot, JavascriptExecutor, WrapsDriver {
    private final ForwardingTargetedWebDriver driver;

    /**
     * Creates a new ThreadedWebDriver for the given driver and executor service. Note that because
     * browsers expect one command at a time (like a real user), WebDrivers are not thread safe,
     * and the executor service should therefore be a
     * {@link java.util.concurrent.Executors#newSingleThreadExecutor() SingleThreadExecutor}.
     */
    public ThreadedTargetedWebDriver(ForwardingTargetedWebDriver driver, ExecutorService executor) {
        super(executor);

        this.driver = Objects.requireNonNull(driver, "driver");
    }

    @Override
    public WebDriverTarget getWebDriverTarget() {
        // This is thread safe
        return driver.getWebDriverTarget();
    }

    @Override
    public boolean isPresent() {
        return submitAndGet(driver::isPresent);
    }

    @Override
    public void get(String url) {
        submitAndWait(() -> driver.get(url));
    }

    @Override
    public String getCurrentUrl() {
        return submitAndGet(driver::getCurrentUrl);
    }

    @Override
    public String getTitle() {
        return submitAndGet(driver::getTitle);
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
    public String getPageSource() {
        return submitAndGet(driver::getPageSource);
    }

    @Override
    public void close() {
        submitAndWait(driver::close);
    }

    @Override
    public void quit() {
        submitAndWait(driver::quit);
    }

    @Override
    public Set<String> getWindowHandles() {
        return submitAndGet(driver::getWindowHandles);
    }

    @Override
    public String getWindowHandle() {
        return submitAndGet(driver::getWindowHandle);
    }

    @Override
    public TargetLocator switchTo() {
        return new ThreadedTargetLocator(submitAndGet(driver::switchTo), executor);
    }

    @Override
    public Navigation navigate() {
        return new ThreadedNavigation(submitAndGet(driver::navigate), executor);
    }

    @Override
    public Options manage() {
        return new ThreadedOptions(submitAndGet(driver::manage), executor);
    }

    @Override
    public WebElement findElementByClassName(String using) {
        WebElement element = submitAndGet(
                () -> ((FindsByClassName) driver).findElementByClassName(using));
        return new ThreadedWebElement(element, executor);
    }

    @Override
    public List<WebElement> findElementsByClassName(String using) {
        List<WebElement> elements = submitAndGet(
                () -> ((FindsByClassName) driver).findElementsByClassName(using));
        return threadedWebElements(elements);
    }

    @Override
    public WebElement findElementByCssSelector(String using) {
        WebElement element = submitAndGet(
                () -> (FindsByCssSelector) driver).findElementByCssSelector(using);
        return new ThreadedWebElement(element, executor);
    }

    @Override
    public List<WebElement> findElementsByCssSelector(String using) {
        List<WebElement> elements = submitAndGet(
                () -> ((FindsByCssSelector) driver).findElementsByCssSelector(using));
        return threadedWebElements(elements);
    }

    @Override
    public WebElement findElementById(String using) {
        WebElement element = submitAndGet(
                () -> ((FindsById) driver).findElementById(using));
        return new ThreadedWebElement(element, executor);
    }

    @Override
    public List<WebElement> findElementsById(String using) {
        List<WebElement> elements = submitAndGet(
                () -> ((FindsById) driver).findElementsById(using));
        return threadedWebElements(elements);
    }

    @Override
    public WebElement findElementByLinkText(String using) {
        WebElement element = submitAndGet(
                () -> ((FindsByLinkText) driver).findElementByLinkText(using));
        return new ThreadedWebElement(element, executor);
    }

    @Override
    public List<WebElement> findElementsByLinkText(String using) {
        List<WebElement> elements = submitAndGet(
                () -> ((FindsByLinkText) driver).findElementsByLinkText(using));
        return threadedWebElements(elements);
    }

    @Override
    public WebElement findElementByPartialLinkText(String using) {
        WebElement element = submitAndGet(
                () -> ((FindsByLinkText) driver).findElementByPartialLinkText(using));
        return new ThreadedWebElement(element, executor);
    }

    @Override
    public List<WebElement> findElementsByPartialLinkText(String using) {
        List<WebElement> elements = submitAndGet(
                () -> ((FindsByLinkText) driver).findElementsByPartialLinkText(using));
        return threadedWebElements(elements);
    }

    @Override
    public WebElement findElementByName(String using) {
        WebElement element = submitAndGet(
                () -> ((FindsByName) driver).findElementByName(using));
        return new ThreadedWebElement(element, executor);
    }

    @Override
    public List<WebElement> findElementsByName(String using) {
        List<WebElement> elements = submitAndGet(
                () -> ((FindsByName) driver).findElementsByName(using));
        return threadedWebElements(elements);
    }

    @Override
    public WebElement findElementByTagName(String using) {
        WebElement element = submitAndGet(
                () -> ((FindsByTagName) driver).findElementByTagName(using));
        return new ThreadedWebElement(element, executor);
    }

    @Override
    public List<WebElement> findElementsByTagName(String using) {
        List<WebElement> elements = submitAndGet(
                () -> ((FindsByTagName) driver).findElementsByTagName(using));
        return threadedWebElements(elements);
    }

    @Override
    public WebElement findElementByXPath(String using) {
        WebElement element = submitAndGet(
                () -> ((FindsByXPath) driver).findElementByXPath(using));
        return new ThreadedWebElement(element, executor);
    }

    @Override
    public List<WebElement> findElementsByXPath(String using) {
        List<WebElement> elements = submitAndGet(
                () -> ((FindsByXPath) driver).findElementsByXPath(using));
        return threadedWebElements(elements);
    }

    @Override
    public Object executeScript(String script, Object... args) {
        return submitAndGet(() -> ((JavascriptExecutor) driver).executeScript(script, args));
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return submitAndGet(() -> ((JavascriptExecutor) driver).executeAsyncScript(script, args));
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return submitAndGet(() -> ((TakesScreenshot) driver).getScreenshotAs(target));
    }

    /**
     * Exposes the un-thread-protected driver.
     */
    @Override
    public WebDriver getWrappedDriver() {
        return driver;
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
