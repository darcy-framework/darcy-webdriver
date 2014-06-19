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

import com.redhat.darcy.ui.Locator;
import com.redhat.darcy.ui.View;
import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.web.Alert;
import com.redhat.darcy.web.Browser;
import com.redhat.darcy.web.DefaultWebSelection;
import com.redhat.darcy.web.Frame;
import com.redhat.darcy.web.StaticUrl;
import com.redhat.darcy.web.Url;
import com.redhat.darcy.web.WebSelection;
import com.redhat.darcy.webdriver.internal.DelegatingWebDriverWebContext;
import com.redhat.darcy.webdriver.internal.WebDriverWebContext;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;

import java.util.List;
import java.util.Objects;

/**
 * The main wrapper around a {@link org.openqa.selenium.WebDriver} in order to implement {@link
 * com.redhat.darcy.web.Browser}. This class also implements {@link com.redhat.darcy.web.Frame},
 * which is a subset of the Browser API.
 * <p>
 * There is one key difference between a Browser in Darcy and a WebDriver in Selenium. In Darcy, a
 * Browser is one:one with a specific window/tab or frame. In WebDriver, a single WebDriver
 * connection may manage many resulting windows or frames. It is assumed that the WebDriver passed
 * to this class is pointed at a specific target.
 * <p>
 * Implementation of {@link com.redhat.darcy.web.Browser} is straightforward, however, in addition
 * to forwarding calls to the relevant WebDriver method, we will use our page object structure to
 * wait for those page objects to load as is required by implementers.
 *
 * @see com.redhat.darcy.webdriver.internal.TargetedWebDriver
 * @see com.redhat.darcy.webdriver.internal.TargetedWebDriverFactory
 */
public class WebDriverBrowser implements Browser, Frame, WebDriverWebContext, WrapsDriver {
    private final WebDriver driver;
    private final WebDriverWebContext webContext;

    /**
     * @param driver A WebDriver implementation to wrap, pointed at some target (like a specific
     *               frame or window), in order to control a browser window or frame.
     * @param webContext A WebContext that represents the driver in order to find elements and other
     *                   contexts. This class implements WebContext by forwarding to this
     *                   implementation.
     */
    public WebDriverBrowser(WebDriver driver, WebDriverWebContext webContext) {
        this.driver = Objects.requireNonNull(driver);
        this.webContext = Objects.requireNonNull(webContext);
    }

    /**
     * @param driver A WebDriver implementation to wrap, pointed at some target (like a specific
     *               frame or window).
     * @param parentContext A parent context that can find other contexts (windows, frames). This
     *                      class implements ParentContext by forwarding to this implementation.
     * @param elementContext An element context that can find other elements. This class implements
     *                       ElementContext by forwarding to this implementation.
     */
    public WebDriverBrowser(WebDriver driver, WebDriverParentContext parentContext,
            WebDriverElementContext elementContext) {
        this.driver = Objects.requireNonNull(driver);
        this.webContext = new DelegatingWebDriverWebContext(elementContext, parentContext);
    }

    @Override
    public <T extends View> T open(Url<T> url) {
        Objects.requireNonNull(url);

        return after(() -> driver.get(url.url()))
                .expect(transition().to(url.destination()))
                .waitUpTo(1, MINUTES);
    }

    @Override
    public <T extends View> T open(String url, T destination) {
        Objects.requireNonNull(url);
        Objects.requireNonNull(destination);

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
        Objects.requireNonNull(destination);

        return after(() -> driver.navigate().back())
                .expect(transition().to(destination))
                .waitUpTo(1, MINUTES);
    }

    @Override
    public <T extends View> T forward(T destination) {
        Objects.requireNonNull(destination);

        return after(() -> driver.navigate().forward())
                .expect(transition().to(destination))
                .waitUpTo(1, MINUTES);
    }

    @Override
    public <T extends View> T refresh(T destination) {
        Objects.requireNonNull(destination);

        return after(() -> driver.navigate().refresh())
                .expect(transition().to(destination))
                .waitUpTo(1, MINUTES);
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

    @Override
    public WebSelection find() {
        return new DefaultWebSelection(this);
    }

    @Override
    public <T> List<T> findAllById(Class<T> type, String id) {
        return webContext.findAllById(type, id);
    }

    @Override
    public <T> List<T> findAllByName(Class<T> type, String name) {
        return webContext.findAllByName(type, name);
    }

    @Override
    public <T> List<T> findAllByXPath(Class<T> type, String xpath) {
        return webContext.findAllByXPath(type, xpath);
    }

    @Override
    public <T> List<T> findAllByChained(Class<T> type, Locator... locators) {
        return webContext.findAllByChained(type, locators);
    }

    @Override
    public <T> List<T> findAllByLinkText(Class<T> type, String linkText) {
        return webContext.findAllByLinkText(type, linkText);
    }

    @Override
    public <T> List<T> findAllByTextContent(Class<T> type, String textContent) {
        return webContext.findAllByTextContent(type, textContent);
    }

    @Override
    public <T> List<T> findAllByPartialTextContent(Class<T> type, String partialTextContent) {
        return webContext.findAllByPartialTextContent(type, partialTextContent);
    }

    @Override
    public <T> List<T> findAllByNested(Class<T> type, Element parent, Locator child) {
        return webContext.findAllByNested(type, parent, child);
    }

    @Override
    public <T> T findById(Class<T> type, String id) {
        return webContext.findById(type, id);
    }

    @Override
    public <T> List<T> findAllByHtmlTag(Class<T> type, String tag) {
        return webContext.findAllByHtmlTag(type, tag);
    }

    @Override
    public <T> List<T> findAllByCssSelector(Class<T> type, String css) {
        return webContext.findAllByCssSelector(type, css);
    }

    @Override
    public <T> T findByName(Class<T> type, String name) {
        return webContext.findByName(type, name);
    }

    @Override
    public <T> T findByXPath(Class<T> type, String xpath) {
        return webContext.findByXPath(type, xpath);
    }

    @Override
    public <T> T findByLinkText(Class<T> type, String linkText) {
        return webContext.findByLinkText(type, linkText);
    }

    @Override
    public <T> T findByChained(Class<T> type, Locator... locators) {
        return webContext.findByChained(type, locators);
    }

    @Override
    public <T> T findByTextContent(Class<T> type, String textContent) {
        return webContext.findByTextContent(type, textContent);
    }

    @Override
    public <T> T findByPartialTextContent(Class<T> type, String partialTextContent) {
        return webContext.findByPartialTextContent(type, partialTextContent);
    }

    @Override
    public <T> T findByHtmlTag(Class<T> type, String tag) {
        return webContext.findByHtmlTag(type, tag);
    }

    @Override
    public <T> T findByCssSelector(Class<T> type, String css) {
        return webContext.findByCssSelector(type, css);
    }

    @Override
    public <T> T findByNested(Class<T> type, Element parent, Locator child) {
        return webContext.findByNested(type, parent, child);
    }

    @Override
    public WebDriver getWrappedDriver() {
        return driver;
    }
}
