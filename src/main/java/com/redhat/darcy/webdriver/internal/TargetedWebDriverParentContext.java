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

import com.redhat.darcy.ui.DarcyException;
import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.ParentContext;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.util.LazyList;
import com.redhat.darcy.web.api.Alert;
import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.web.api.Frame;
import com.redhat.darcy.webdriver.ElementConstructorMap;
import com.redhat.darcy.webdriver.WebDriverAlert;
import com.redhat.darcy.webdriver.WebDriverBrowser;
import com.redhat.darcy.webdriver.WebDriverParentContext;

import org.hamcrest.Matcher;
import org.openqa.selenium.WebDriver.TargetLocator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/**
 * {@link ParentContext} for {@link TargetedWebDriver}s that instantiates other
 * {@link com.redhat.darcy.webdriver.WebDriverBrowser}s with {@link TargetedWebDriver}s assigned to
 * them that point to the found driver.
 */
public class TargetedWebDriverParentContext implements WebDriverParentContext {
    private final WebDriverTarget myTarget;
    private final TargetLocator locator;
    private final ElementConstructorMap elementMap;
    private final KnowsWindowHandles knowsWindowHandles;

    @FunctionalInterface
    public interface KnowsWindowHandles {
        Set<String> getWindowHandles();
    }

    /**
     * @param myTarget Parent contexts must be targeted because frame targets depend on another,
     *         "parent" target. So each parent context must have its own WebDriver target that is
     *         associated with, because this state is used when finding frames.
     * @param locator Means of finding other WebDrivers for new targets. Each new browser shares the
     *         same locator.
     * @param knowsWindowHandles Finds open windows' handles. Must be associated with the same
     *         driver as the {@code locator}.
     * @param elementMap Each new browser must have an element conFunction or type which can provide the current set of window
     *         handles of the driver associated with the locator.structor map so it may create
     */
    public TargetedWebDriverParentContext(WebDriverTarget myTarget, TargetLocator locator,
            KnowsWindowHandles knowsWindowHandles, ElementConstructorMap elementMap) {
        this.myTarget = myTarget;
        this.locator = locator;
        this.knowsWindowHandles = knowsWindowHandles;
        this.elementMap = elementMap;
    }

    @Override
    public Alert alert() {
        return new WebDriverAlert(new ForwardingTargetedAlert(locator));
    }

    @Override
    public <T> List<T> findAllById(Class<T> type, String id) {
        return findAllByNameOrId(type, id);
    }

    @Override
    public <T> T findById(Class<T> type, String id) {
        return findByNameOrId(type, id);
    }

    @Override
    public <T> List<T> findAllByName(Class<T> type, String name) {
        return findAllByNameOrId(type, name);
    }

    @Override
    public <T> T findByName(Class<T> type, String name) {
        return findByNameOrId(type, name);
    }

    public <T> List<T> findAllByNameOrId(Class<T> type, String nameOrId) {
        return Collections.singletonList(findByNameOrId(type, nameOrId));
    }

    @SuppressWarnings("unchecked")
    public <T> T findByNameOrId(Class<T> type, String nameOrId) {
        if (!type.isAssignableFrom(WebDriverBrowser.class)) {
            throw new DarcyException("Cannot find contexts of type: " + type);
        }

        WebDriverTarget newTarget = Frame.class.equals(type)
                ? WebDriverTargets.frame(myTarget, nameOrId)
                : WebDriverTargets.window(nameOrId);

        return (T) newBrowser(newTarget);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllByView(Class<T> type, View view) {
        if (!type.isAssignableFrom(WebDriverBrowser.class)) {
            throw new DarcyException("Cannot find contexts of type: " + type);
        }

        if (Frame.class.equals(type)) {
            throw new DarcyException("Cannot find Frames by view. Unable to iterate through all "
                    + "available frames.");
        }

        return (List<T>) new LazyList<>(new FoundByViewSupplier(view));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findByView(Class<T> type, View view) {
        if (!type.isAssignableFrom(WebDriverBrowser.class)) {
            throw new DarcyException("Cannot find contexts of type: " + type);
        }

        if (Frame.class.equals(type)) {
            throw new DarcyException("Cannot find Frames by view. Unable to iterate through all "
                    + "available frames.");
        }

        return (T) newBrowser(WebDriverTargets.withViewLoaded(view, this));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllByTitle(Class<T> type, String title) {
        if (!type.isAssignableFrom(WebDriverBrowser.class)) {
            throw new DarcyException("Cannot find contexts of type: " + type);
        }

        if (Frame.class.equals(type)) {
            throw new DarcyException("Cannot find Frames by title. Unable to iterate through all "
                    + "available frames.");
        }

        return (List<T>) new LazyList<>(new FoundByTitleSupplier(title));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findByTitle(Class<T> type, String title) {
        if (!type.isAssignableFrom(WebDriverBrowser.class)) {
            throw new DarcyException("Cannot find contexts of type: " + type);
        }

        if (Frame.class.equals(type)) {
            throw new DarcyException("Cannot find Frames by title. Unable to iterate through all "
                    + "available frames.");
        }

        return (T) newBrowser(WebDriverTargets.windowByTitle(title));
    }

    @Override
    public <T> List<T> findAllByUrl(Class<T> type, Matcher<? super String> urlMatcher) {
        if (!type.isAssignableFrom(WebDriverBrowser.class)) {
            throw new DarcyException("Cannot find contexts of type: " + type);
        }

        if (Frame.class.equals(type)) {
            throw new DarcyException("Cannot find Frames by url. Unable to iterate through all "
                    + "available frames.");
        }

        return (List<T>) new LazyList<>(new FoundByUrlSupplier(urlMatcher));
    }

    @Override
    public <T> T findByUrl(Class<T> type, Matcher<? super String> urlMatcher) {
        if (!type.isAssignableFrom(WebDriverBrowser.class)) {
            throw new DarcyException("Cannot find contexts of type: " + type);
        }

        if (Frame.class.equals(type)) {
            throw new DarcyException("Cannot find Frames by url. Unable to iterate through all "
                    + "available frames.");
        }

        return (T) newBrowser(WebDriverTargets.windowByUrl(urlMatcher));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TargetedWebDriverParentContext that = (TargetedWebDriverParentContext) o;
        return Objects.equals(myTarget, that.myTarget) &&
                Objects.equals(locator, that.locator) &&
                Objects.equals(elementMap, that.elementMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(myTarget, locator, elementMap);
    }

    @Override
    public String toString() {
        return "TargetedWebDriverParentContext{" +
                "myTarget=" + myTarget +
                ", locator=" + locator +
                ", elementMap=" + elementMap +
                '}';
    }

    private WebDriverBrowser newBrowser(WebDriverTarget target) {
        TargetedWebDriver targetedDriver = new ForwardingTargetedWebDriver(locator, target);

        return new WebDriverBrowser(targetedDriver,
            new TargetedWebDriverParentContext(target, locator, knowsWindowHandles, elementMap),
            new DefaultWebDriverElementContext(targetedDriver, elementMap));
    }

    class FoundByViewSupplier implements Supplier<List<Browser>> {
        private final View view;

        FoundByViewSupplier(View view) {
            this.view = view;
        }

        @Override
        public List<Browser> get() {
            List<Browser> found = new ArrayList<>();

            for (String windowHandle : knowsWindowHandles.getWindowHandles()) {
                Browser forWindowHandle = findById(Browser.class, windowHandle);

                ElementContext priorContext = view.getContext();
                view.setContext(forWindowHandle);

                if (view.isLoaded()) {
                    found.add(forWindowHandle);
                }

                if (priorContext != null) {
                    view.setContext(priorContext);
                }
            }

            return found;
        }
    }

    class FoundByTitleSupplier implements Supplier<List<Browser>> {
        private final String title;

        FoundByTitleSupplier(String title) {
            this.title = title;
        }

        @Override
        public List<Browser> get() {
            List<Browser> found = new ArrayList<>();

            for (String windowHandle : knowsWindowHandles.getWindowHandles()) {
                if (locator.window(windowHandle).getTitle().equals(title)) {
                    found.add(findById(Browser.class, windowHandle));
                }
            }

            return found;
        }
    }

    private class FoundByUrlSupplier implements Supplier<List<Browser>> {
        private final Matcher<? super String> urlMatcher;

        public FoundByUrlSupplier(Matcher<? super String> urlMatcher) {
            this.urlMatcher = urlMatcher;
        }

        @Override
        public List<Browser> get() {
            List<Browser> found = new ArrayList<>();

            for (String windowHandle : knowsWindowHandles.getWindowHandles()) {
                if (urlMatcher.matches(locator.window(windowHandle).getCurrentUrl())) {
                    found.add(findById(Browser.class, windowHandle));
                }
            }

            return found;
        }
    }
}
