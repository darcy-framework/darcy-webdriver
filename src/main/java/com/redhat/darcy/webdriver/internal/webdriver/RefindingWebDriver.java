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
import java.util.Set;

/**
 * A WebDriver who's found elements are "refinding"&mdash;that is, they know how to find themselves
 * again in the event that their reference becomes stale.
 */
public class RefindingWebDriver implements WebDriver, FindsByClassName, FindsByCssSelector,
        FindsById, FindsByLinkText, FindsByName, FindsByTagName, FindsByXPath, TakesScreenshot,
        JavascriptExecutor, WrapsDriver {
    private final WebDriver driver;

    /**
     * @param driver Used as the underlying implementation; all calls ultimately get forwarded to
     * this driver, and refinding elements will use this driver to refind themselves.
     */
    public RefindingWebDriver(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void get(String url) {
        driver.get(url);
    }

    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    // TODO: Refinding list elements?
    @Override
    public List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return by.findElement(this);
    }

    @Override
    public String getPageSource() {
        return driver.getPageSource();
    }

    @Override
    public void close() {
        driver.close();
    }

    @Override
    public void quit() {
        driver.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return new RefindingTargetLocator(driver.switchTo());
    }

    @Override
    public Navigation navigate() {
        return driver.navigate();
    }

    @Override
    public Options manage() {
        return driver.manage();
    }

    @Override
    public WebElement findElementByClassName(String using) {
        return new RefindingWebElement(By.className(using), driver);
    }

    @Override
    public List<WebElement> findElementsByClassName(String using) {
        return ((FindsByClassName) driver).findElementsByClassName(using);
    }

    @Override
    public WebElement findElementByCssSelector(String using) {
        return new RefindingWebElement(By.cssSelector(using), driver);
    }

    @Override
    public List<WebElement> findElementsByCssSelector(String using) {
        return ((FindsByCssSelector) driver).findElementsByCssSelector(using);
    }

    @Override
    public WebElement findElementById(String using) {
        return new RefindingWebElement(By.id(using), driver);
    }

    @Override
    public List<WebElement> findElementsById(String using) {
        return ((FindsById) driver).findElementsById(using);
    }

    @Override
    public WebElement findElementByLinkText(String using) {
        return new RefindingWebElement(By.linkText(using), driver);
    }

    @Override
    public List<WebElement> findElementsByLinkText(String using) {
        return ((FindsByLinkText) driver).findElementsByLinkText(using);
    }

    @Override
    public WebElement findElementByPartialLinkText(String using) {
        return new RefindingWebElement(By.partialLinkText(using), driver);
    }

    @Override
    public List<WebElement> findElementsByPartialLinkText(String using) {
        return ((FindsByLinkText) driver).findElementsByPartialLinkText(using);
    }

    @Override
    public WebElement findElementByName(String using) {
        return new RefindingWebElement(By.name(using), driver);
    }

    @Override
    public List<WebElement> findElementsByName(String using) {
        return ((FindsByName) driver).findElementsByName(using);
    }

    @Override
    public WebElement findElementByTagName(String using) {
        return new RefindingWebElement(By.tagName(using), driver);
    }

    @Override
    public List<WebElement> findElementsByTagName(String using) {
        return ((FindsByTagName) driver).findElementsByTagName(using);
    }

    @Override
    public WebElement findElementByXPath(String using) {
        return new RefindingWebElement(By.xpath(using), driver);
    }

    @Override
    public List<WebElement> findElementsByXPath(String using) {
        return ((FindsByXPath) driver).findElementsByXPath(using);
    }

    @Override
    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeAsyncScript(script, args);
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return ((TakesScreenshot) driver).getScreenshotAs(target);
    }

    @Override
    public WebDriver getWrappedDriver() {
        return driver;
    }
}
