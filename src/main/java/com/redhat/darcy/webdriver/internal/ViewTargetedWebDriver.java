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

public class ViewTargetedWebDriver implements TargetedWebDriver, FindsByClassName,
        FindsByCssSelector, FindsById, FindsByLinkText, FindsByName, FindsByTagName, FindsByXPath,
        TakesScreenshot, JavascriptExecutor, WrapsDriver {


    @Override
    public boolean isPresent() {
        return false;
    }

    @Override
    public WebElement findElementByClassName(String using) {
        return null;
    }

    @Override
    public List<WebElement> findElementsByClassName(String using) {
        return null;
    }

    @Override
    public WebElement findElementByCssSelector(String using) {
        return null;
    }

    @Override
    public List<WebElement> findElementsByCssSelector(String using) {
        return null;
    }

    @Override
    public WebElement findElementById(String using) {
        return null;
    }

    @Override
    public List<WebElement> findElementsById(String using) {
        return null;
    }

    @Override
    public WebElement findElementByLinkText(String using) {
        return null;
    }

    @Override
    public List<WebElement> findElementsByLinkText(String using) {
        return null;
    }

    @Override
    public WebElement findElementByPartialLinkText(String using) {
        return null;
    }

    @Override
    public List<WebElement> findElementsByPartialLinkText(String using) {
        return null;
    }

    @Override
    public WebElement findElementByName(String using) {
        return null;
    }

    @Override
    public List<WebElement> findElementsByName(String using) {
        return null;
    }

    @Override
    public WebElement findElementByTagName(String using) {
        return null;
    }

    @Override
    public List<WebElement> findElementsByTagName(String using) {
        return null;
    }

    @Override
    public WebElement findElementByXPath(String using) {
        return null;
    }

    @Override
    public List<WebElement> findElementsByXPath(String using) {
        return null;
    }

    @Override
    public Object executeScript(String script, Object... args) {
        return null;
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        return null;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return null;
    }

    @Override
    public WebDriverTarget getWebDriverTarget() {
        return null;
    }

    @Override
    public void get(String url) {

    }

    @Override
    public String getCurrentUrl() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public List<WebElement> findElements(By by) {
        return null;
    }

    @Override
    public WebElement findElement(By by) {
        return null;
    }

    @Override
    public String getPageSource() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public void quit() {

    }

    @Override
    public Set<String> getWindowHandles() {
        return null;
    }

    @Override
    public String getWindowHandle() {
        return null;
    }

    @Override
    public TargetedTargetLocator switchTo() {
        return null;
    }

    @Override
    public Navigation navigate() {
        return null;
    }

    @Override
    public Options manage() {
        return null;
    }

    @Override
    public WebDriver getWrappedDriver() {
        return null;
    }
}
