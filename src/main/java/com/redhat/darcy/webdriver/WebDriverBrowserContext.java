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

package com.redhat.darcy.webdriver;

import static com.redhat.synq.Synq.after;
import static java.util.concurrent.TimeUnit.MINUTES;

import com.redhat.darcy.ui.Context;
import com.redhat.darcy.ui.Locator;
import com.redhat.darcy.ui.View;
import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.web.Alert;
import com.redhat.darcy.web.BrowserContext;
import com.redhat.darcy.web.FrameContext;
import com.redhat.darcy.web.StaticUrl;
import com.redhat.darcy.web.Url;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;

import java.util.List;

/**
 * The main wrapper around a {@link org.openqa.selenium.WebDriver} in order to implement 
 * {@link com.redhat.darcy.web.BrowserContext}. This class also implements 
 * {@link com.redhat.darcy.web.FrameContext}, which is a subset of the Browser API.
 * <P>
 * There is one key difference between a Browser in Darcy and a WebDriver in Selenium. In Darcy, a
 * Browser is one:one with a specific window/tab or frame. In WebDriver, a single WebDriver 
 * connection may manage many resulting windows or frames. It is assumed that the WebDriver passed
 * to this class is pointed at a specific target.
 * <P>
 * Implementation of {@link com.redhat.darcy.web.Browser} is straightforward, however, in addition
 * to forwarding calls to the relevant WebDriver method, we will use our page object structure to
 * wait for those page objects to load as is required by implementors.
 * 
 * @see com.redhat.darcy.webdriver.internal.TargetedWebDriver
 * @see com.redhat.darcy.webdriver.internal.TargetedDriverFactory
 */
public class WebDriverBrowserContext implements BrowserContext, FrameContext, 
        WebDriverElementContext, WrapsDriver {
    private final WebDriver driver;
    private final WebDriverParentContext parentContext;
    private final WebDriverElementContext elementContext;
    
    /**
     * 
     * @param driver A WebDriver implementation to wrap, pointed at some target (like a specific 
     *               frame or window).
     * @param parentContext A parent context that can find other contexts (windows, frames). This
     *                      class implements ParentContext by forwarding to this implementation.
     * @param elementContext An element context that can find other elements. This class implements
     *                       ElementContext by forwarding to this implementation.
     */
    public WebDriverBrowserContext(WebDriver driver, WebDriverParentContext parentContext, 
            WebDriverElementContext elementContext) {
        this.driver = driver;
        this.parentContext = parentContext;
        this.elementContext = elementContext;
    }
    
    @Override
    public <T extends View> T open(Url<T> url) {
        return after(() -> driver.get(url.url()))
                .expect(transition().to(url.destination()))
                .waitUpTo(1, MINUTES);
    }

    @Override
    public <T extends View> T open(String url, T destination) {
        return open(new StaticUrl<T>(url, destination));
    }

    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }
    
    @Override
    public String getSource() {
        return driver.getPageSource();
    }

    @Override
    public <T extends View> T back(T destination) {
        return after(() -> driver.navigate().back())
                .expect(transition().to(destination))
                .waitUpTo(1, MINUTES);
    }

    @Override
    public <T extends View> T forward(T destination) {
        return after(() -> driver.navigate().forward())
                .expect(transition().to(destination))
                .waitUpTo(1, MINUTES);
    }

    @Override
    public <T extends View> T refresh(T destination) {
        return after(() -> driver.navigate().refresh())
                .expect(transition().to(destination))
                .waitUpTo(1, MINUTES);
    }
    
    @Override
    public FrameContext frame(Locator locator) {
        return findContext(FrameContext.class, locator);
    }
    
    @Override
    public Alert alert() {
        return new WebDriverAlert(driver);
    }

    @Override
    public void close() {
        driver.close();
    }
    
    @Override
    public void closeAll() {
        driver.quit();
    }

    public <T extends Element> T findElement(Class<T> type, Locator locator) {
        return elementContext.findElement(type, locator);
    }

    public <T extends Element> List<T> findElements(Class<T> type, Locator locator) {
        return elementContext.findElements(type, locator);
    }
    
    public <T> List<T> findAllById(Class<T> type, String id) {
        return elementContext.findAllById(type, id);
    }

    public <T> List<T> findAllByName(Class<T> type, String name) {
        return elementContext.findAllByName(type, name);
    }

    public <T> List<T> findAllByXPath(Class<T> type, String xpath) {
        return elementContext.findAllByXPath(type, xpath);
    }

    public <T> List<T> findAllByChained(Class<T> type, Locator... locators) {
        return elementContext.findAllByChained(type, locators);
    }

    public <T> List<T> findAllByLinkText(Class<T> type, String linkText) {
        return elementContext.findAllByLinkText(type, linkText);
    }

    public <T> List<T> findAllByTextContent(Class<T> type, String textContent) {
        return elementContext.findAllByTextContent(type, textContent);
    }

    public <T> List<T> findAllByPartialTextContent(Class<T> type, String partialTextContent) {
        return elementContext.findAllByPartialTextContent(type, partialTextContent);
    }

    public <T> List<T> findAllByNested(Class<T> type, Element parent, Locator child) {
        return elementContext.findAllByNested(type, parent, child);
    }

    public <T> T findById(Class<T> type, String id) {
        return elementContext.findById(type, id);
    }

    public <T> List<T> findAllByHtmlTag(Class<T> type, String tag) {
        return elementContext.findAllByHtmlTag(type, tag);
    }

    public <T> List<T> findAllByCssSelector(Class<T> type, String css) {
        return elementContext.findAllByCssSelector(type, css);
    }

    public <T> T findByName(Class<T> type, String name) {
        return elementContext.findByName(type, name);
    }

    public <T> T findByXPath(Class<T> type, String xpath) {
        return elementContext.findByXPath(type, xpath);
    }

    public <T> T findByLinkText(Class<T> type, String linkText) {
        return elementContext.findByLinkText(type, linkText);
    }

    public <T> T findByChained(Class<T> type, Locator... locators) {
        return elementContext.findByChained(type, locators);
    }

    public <T> T findByTextContent(Class<T> type, String textContent) {
        return elementContext.findByTextContent(type, textContent);
    }

    public <T> T findByPartialTextContent(Class<T> type, String partialTextContent) {
        return elementContext.findByPartialTextContent(type, partialTextContent);
    }

    public <T> T findByHtmlTag(Class<T> type, String tag) {
        return elementContext.findByHtmlTag(type, tag);
    }

    public <T> T findByCssSelector(Class<T> type, String css) {
        return elementContext.findByCssSelector(type, css);
    }

    public <T> T findByNested(Class<T> type, Element parent, Locator child) {
        return elementContext.findByNested(type, parent, child);
    }

    @Override
    public <T extends Context> T findContext(Class<T> type, Locator locator) {
        return parentContext.findContext(type, locator);
    }
    
    @Override
    public WebDriver getWrappedDriver() {
        return driver;
    }
}
