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

import com.redhat.darcy.ui.FindableNotPresentException;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.Transition;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.internal.SimpleTransition;
import com.redhat.darcy.web.api.Alert;
import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.web.api.CookieManager;
import com.redhat.darcy.web.api.Frame;
import com.redhat.darcy.web.api.ViewUrl;
import com.redhat.darcy.web.api.WebSelection;
import com.redhat.darcy.webdriver.internal.DelegatingWebContext;
import com.redhat.darcy.webdriver.internal.TargetedWebDriver;
import com.redhat.darcy.webdriver.internal.WebDriverCookieManager;
import com.redhat.darcy.webdriver.internal.WebDriverWebContext;
import com.redhat.darcy.webdriver.internal.WebDriverWebSelection;
import com.redhat.darcy.webdriver.internal.WrapsTargetedDriver;
import com.redhat.synq.Event;

import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.remote.SessionNotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * The main wrapper around a {@link org.openqa.selenium.WebDriver} in order to implement {@link
 * com.redhat.darcy.web.api.Browser}. This class also implements {@link com.redhat.darcy.web.api.Frame},
 * which is a subset of the Browser API.
 * <p>
 * There is one key difference between a Browser in Darcy and a WebDriver in Selenium. In Darcy, a
 * Browser is one:one with a specific window/tab or frame. In WebDriver, a single WebDriver
 * connection may manage many resulting windows or frames. It is assumed that the WebDriver passed
 * to this class is pointed at a specific target.
 * <p>
 * Implementation of {@link com.redhat.darcy.web.api.Browser} is straightforward, however, in addition
 * to forwarding calls to the relevant WebDriver method, we will use our page object structure to
 * wait for those page objects to load as is required by implementers.
 *
 * @see com.redhat.darcy.webdriver.internal.TargetedWebDriver
 */
public class WebDriverBrowser implements Browser, Frame, WebDriverWebContext, WrapsTargetedDriver {
    private final TargetedWebDriver driver;
    private final WebDriverWebContext webContext;

    /**
     * @param driver A WebDriver implementation to wrap, pointed at some target (like a specific
     *               frame or window), in order to control a browser window or frame.
     * @param webContext A WebContext that represents the driver in order to find elements and other
     *                   contexts. This class implements WebContext by forwarding to this
     *                   implementation.
     */
    public WebDriverBrowser(TargetedWebDriver driver, WebDriverWebContext webContext) {
        this.driver = Objects.requireNonNull(driver, "driver");
        this.webContext = Objects.requireNonNull(webContext, "webContext");
    }

    /**
     * @param driver A WebDriver implementation to wrap, pointed at some target (like a specific
     *               frame or window).
     * @param parentContext A parent context that can find other contexts (windows, frames). This
     *                      class implements ParentContext by forwarding to this implementation. The
     *                      parent context must be scoped to the same target as {@code driver}.
     * @param elementContext An element context that can find other elements. This class implements
     *                       ElementContext by forwarding to this implementation. The element
     *                       context must be scoped to the same target as {@code driver}.
     */
    public WebDriverBrowser(TargetedWebDriver driver, WebDriverParentContext parentContext,
                    WebDriverElementContext elementContext) {
        this(driver, new DelegatingWebContext(elementContext, parentContext));
    }

    @Override
    public boolean isPresent() {
        return driver.isPresent();
    }

    @Override
    public <T extends View> Event<T> open(ViewUrl<T> viewUrl) {
        Objects.requireNonNull(viewUrl);

        return open(viewUrl.url(), viewUrl.destination());
    }

    @Override
    public <T extends View> Event<T> open(String url, T destination) {
        Objects.requireNonNull(url);
        Objects.requireNonNull(destination);

        return after(() -> attempt(() -> driver.get(url)))
                .expect(transition().to(destination));
    }

    @Override
    public String getCurrentUrl() {
        return attemptAndGet(driver::getCurrentUrl);
    }

    @Override
    public String getTitle() {
        return attemptAndGet(driver::getTitle);
    }

    @Override
    public String getSource() {
        return attemptAndGet(driver::getPageSource);
    }

    @Override
    public <T extends View> Event<T> back(T destination) {
        Objects.requireNonNull(destination);

        return after(() -> attempt(() -> driver.navigate().back()))
                .expect(transition().to(destination));
    }

    @Override
    public <T extends View> Event<T> forward(T destination) {
        Objects.requireNonNull(destination);

        return after(() -> attempt(() -> driver.navigate().forward()))
                .expect(transition().to(destination));
    }

    @Override
    public <T extends View> Event<T> refresh(T destination) {
        Objects.requireNonNull(destination);

        return after(() -> attempt(() -> driver.navigate().refresh()))
                .expect(transition().to(destination));
    }

    @Override
    public CookieManager cookies() {
        return new WebDriverCookieManager(driver);
    }

    @Override
    public void close() {
        attempt(driver::close);
    }

    @Override
    public void closeAll() {
        // TODO: Any harm in calling this with no windows open?
        driver.quit();
    }

    @Override
    public WebSelection find() {
        return new WebDriverWebSelection(this);
    }

    @Override
    public Transition transition() {
        return new SimpleTransition(this);
    }

    @Override
    public Alert alert() {
        return webContext.alert();
    }

    @Override
    public <T> List<T> findAllById(Class<T> type, String id) {
        return attemptAndGet(() -> webContext.findAllById(type, id));
    }

    @Override
    public <T> List<T> findAllByName(Class<T> type, String name) {
        return attemptAndGet(() -> webContext.findAllByName(type, name));
    }

    @Override
    public <T> List<T> findAllByXPath(Class<T> type, String xpath) {
        return attemptAndGet(() -> webContext.findAllByXPath(type, xpath));
    }

    @Override
    public <T> List<T> findAllByChained(Class<T> type, Locator... locators) {
        return attemptAndGet(() -> webContext.findAllByChained(type, locators));
    }

    @Override
    public <T> List<T> findAllByLinkText(Class<T> type, String linkText) {
        return attemptAndGet(() -> webContext.findAllByLinkText(type, linkText));
    }

    @Override
    public <T> List<T> findAllByTextContent(Class<T> type, String textContent) {
        return attemptAndGet(() -> webContext.findAllByTextContent(type, textContent));
    }

    @Override
    public <T> List<T> findAllByPartialTextContent(Class<T> type, String partialTextContent) {
        return attemptAndGet(() -> webContext.findAllByPartialTextContent(type, partialTextContent));
    }

    @Override
    public <T> List<T> findAllByNested(Class<T> type, Element parent, Locator child) {
        return attemptAndGet(() -> webContext.findAllByNested(type, parent, child));
    }

    @Override
    public <T> T findById(Class<T> type, String id) {
        return attemptAndGet(() -> webContext.findById(type, id));
    }

    @Override
    public <T> List<T> findAllByHtmlTag(Class<T> type, String tag) {
        return attemptAndGet(() -> webContext.findAllByHtmlTag(type, tag));
    }

    @Override
    public <T> List<T> findAllByCss(Class<T> type, String css) {
        return attemptAndGet(() -> webContext.findAllByCss(type, css));
    }

    @Override
    public <T> List<T> findAllByClassName(Class<T> type, String className) {
        return attemptAndGet(() -> webContext.findAllByClassName(type, className));
    }

    @Override
    public <T> T findByName(Class<T> type, String name) {
        return attemptAndGet(() -> webContext.findByName(type, name));
    }

    @Override
    public <T> T findByXPath(Class<T> type, String xpath) {
        return attemptAndGet(() -> webContext.findByXPath(type, xpath));
    }

    @Override
    public <T> T findByLinkText(Class<T> type, String linkText) {
        return attemptAndGet(() -> webContext.findByLinkText(type, linkText));
    }

    @Override
    public <T> T findByChained(Class<T> type, Locator... locators) {
        return attemptAndGet(() -> webContext.findByChained(type, locators));
    }

    @Override
    public <T> T findByTextContent(Class<T> type, String textContent) {
        return attemptAndGet(() -> webContext.findByTextContent(type, textContent));
    }

    @Override
    public <T> T findByPartialTextContent(Class<T> type, String partialTextContent) {
        return attemptAndGet(() -> webContext.findByPartialTextContent(type, partialTextContent));
    }

    @Override
    public <T> T findByHtmlTag(Class<T> type, String tag) {
        return attemptAndGet(() -> webContext.findByHtmlTag(type, tag));
    }

    @Override
    public <T> T findByCss(Class<T> type, String css) {
        return attemptAndGet(() -> webContext.findByCss(type, css));
    }

    @Override
    public <T> T findByClassName(Class<T> type, String className) {
        return attemptAndGet(() -> webContext.findByClassName(type, className));
    }

    @Override
    public <T> T findByNested(Class<T> type, Element parent, Locator child) {
        return attemptAndGet(() -> webContext.findByNested(type, parent, child));
    }

    @Override
    public <T> List<T> findAllByAttribute(Class<T> type, String attribute, String value) {
        return attemptAndGet(() -> webContext.findAllByAttribute(type, attribute, value));
    }

    @Override
    public <T> T findByAttribute(Class<T> type, String attribute, String value) {
        return attemptAndGet(() -> webContext.findByAttribute(type, attribute, value));
    }

    @Override
    public <T> List<T> findAllByView(Class<T> type, View view) {
        return attemptAndGet(() -> webContext.findAllByView(type, view));
    }

    @Override
    public <T> T findByView(Class<T> type, View view) {
        return attemptAndGet(() -> webContext.findByView(type, view));
    }

    @Override
    public TargetedWebDriver getWrappedDriver() {
        return driver;
    }

    /**
     * Wrapper for interacting with a targeted driver that may or may not actually be present.
     */
    private void attempt(Runnable action) {
        try {
            action.run();
        } catch (NoSuchFrameException | NoSuchWindowException | SessionNotFoundException e) {
            throw new FindableNotPresentException(this, e);
        }
    }

    /**
     * Wrapper for interacting with a targeted driver that may or may not actually be present.
     * Returns a result.
     */
    private <T> T attemptAndGet(Supplier<T> action) {
        try {
            return action.get();
        } catch (NoSuchFrameException | NoSuchWindowException | SessionNotFoundException e) {
            throw new FindableNotPresentException(this, e);
        }
    }
}
