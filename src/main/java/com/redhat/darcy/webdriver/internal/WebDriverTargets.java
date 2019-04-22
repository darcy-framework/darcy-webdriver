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

import com.redhat.darcy.ui.By;
import com.redhat.darcy.ui.api.ParentContext;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.internal.FindsById;
import com.redhat.darcy.util.Caching;
import com.redhat.darcy.web.api.Browser;

import org.hamcrest.Matcher;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;

import java.util.Objects;

/**
 * Factories for the various possible {@link WebDriverTarget}s.
 */
public abstract class WebDriverTargets {
    public static WebDriverTarget window(String nameOrHandle) {
        return new WindowWebDriverTarget(nameOrHandle);
    }
    
    public static WebDriverTarget frame(WebDriverTarget parent, int index) {
        return new FrameByIndexWebDriverTarget(parent, index);
    }
    
    public static WebDriverTarget frame(WebDriverTarget parent, String nameOrId) {
        return new FrameByNameOrIdWebDriverTarget(parent, nameOrId);
    }
    
    public static WebDriverTarget frame(WebDriverTarget parent, WebElement frameElement) {
        return new FrameByElementWebDriverTarget(parent, frameElement);
    }

    public static WebDriverTarget defaultContent() {
        return new DefaultContextWebDriverTarget();
    }

    public static WebDriverTarget withViewLoaded(View view, ParentContext parentContext) {
        return new ViewWebDriverTarget(view, parentContext);
    }

    public static WebDriverTarget windowByTitle(String title) {
        return new WindowTitleWebDriverTarget(title);
    }

    public static WebDriverTarget windowByUrl(Matcher<? super String> urlMatcher) {
        return new WindowUrlWebDriverTarget(urlMatcher);
    }

    /**
     * Determines the parent target of the specified
     * {@link com.redhat.darcy.webdriver.internal.WebDriverTarget}. If the target has no parent
     * (that is, it is not a target to a frame), then this returns the same target that it was
     * passed. This matches the behavior of
     * {@link org.openqa.selenium.WebDriver.TargetLocator#parentFrame()}.
     */
    public static WebDriverTarget parentOf(WebDriverTarget target) {
        if (target instanceof FrameTarget) {
            return ((FrameTarget) target).getParent();
        }

        return target;
    }

    public static class WindowWebDriverTarget implements WebDriverTarget {
        private final String nameOrHandle;

        WindowWebDriverTarget(String nameOrHandle) {
            this.nameOrHandle = Objects.requireNonNull(nameOrHandle, "nameOrHandle");
        }

        @Override
        public WebDriver switchTo(TargetLocator targetLocator) {
            return targetLocator.window(nameOrHandle);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nameOrHandle);
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }

            if (!(object instanceof WindowWebDriverTarget)) {
                return false;
            }

            WindowWebDriverTarget other = (WindowWebDriverTarget) object;

            return this.nameOrHandle.equals(other.nameOrHandle);
        }

        @Override
        public String toString() {
            return "WindowWebDriverTarget: {nameOrHandle: " + nameOrHandle + "}";
        }
    }
    
    public static class FrameByIndexWebDriverTarget implements FrameTarget {
        private final WebDriverTarget parent;
        private final int index;
        
        FrameByIndexWebDriverTarget(WebDriverTarget parent, int index) {
            this.parent = Objects.requireNonNull(parent, "parent");
            this.index = index;
        }

        @Override
        public WebDriverTarget getParent() {
            return parent;
        }
        
        @Override
        public WebDriver switchTo(TargetLocator targetLocator) {
            // This is ugly :(
            if (targetLocator instanceof CachingTargetLocator) {
                return ((CachingTargetLocator) targetLocator).frame(parent, index);
            }
            
            parent.switchTo(targetLocator);
            return targetLocator.frame(index);
        }

        @Override
        public int hashCode() {
            return Objects.hash(parent, index);
        }
        
        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            
            if (!(object instanceof FrameByIndexWebDriverTarget)) {
                return false;
            }
            
            FrameByIndexWebDriverTarget other = (FrameByIndexWebDriverTarget) object;
            
            return this.index == other.index
                    && this.parent.equals(other.parent);
        }
    }
    
    public static class FrameByNameOrIdWebDriverTarget implements FrameTarget {
        private final WebDriverTarget parent;
        private final String nameOrId;
        
        FrameByNameOrIdWebDriverTarget(WebDriverTarget parent, String nameOrId) {
            this.parent = Objects.requireNonNull(parent, "parent");
            this.nameOrId = nameOrId;
        }

        @Override
        public WebDriverTarget getParent() {
            return parent;
        }
        
        @Override
        public WebDriver switchTo(TargetLocator targetLocator) {
            // This is ugly :(
            if (targetLocator instanceof CachingTargetLocator) {
                return ((CachingTargetLocator) targetLocator).frame(parent, nameOrId);
            }
            
            parent.switchTo(targetLocator);
            return targetLocator.frame(nameOrId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(parent, nameOrId);
        }
        
        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            
            if (!(object instanceof FrameByNameOrIdWebDriverTarget)) {
                return false;
            }
            
            FrameByNameOrIdWebDriverTarget other = (FrameByNameOrIdWebDriverTarget) object;
            
            return this.nameOrId.equals(other.nameOrId)
                    && this.parent.equals(other.parent);
        }
        
        @Override
        public String toString() {
            return "FrameByNameOrIdWebDriverTarget: {parent: " + parent + ", nameOrId: " 
                    + nameOrId + "}";
        }
    }
    
    public static class FrameByElementWebDriverTarget implements FrameTarget {
        private final WebDriverTarget parent;
        private final WebElement frameElement;
        
        FrameByElementWebDriverTarget(WebDriverTarget parent, WebElement frameElement) {
            this.parent = Objects.requireNonNull(parent, "parent");
            this.frameElement = Objects.requireNonNull(frameElement, "frameElement");
        }

        @Override
        public WebDriverTarget getParent() {
            return parent;
        }
        
        @Override
        public WebDriver switchTo(TargetLocator targetLocator) {
            // This is ugly :(
            if (targetLocator instanceof CachingTargetLocator) {
                return ((CachingTargetLocator) targetLocator).frame(parent, frameElement);
            }
            
            parent.switchTo(targetLocator);
            return targetLocator.frame(frameElement);
        }

        @Override
        public int hashCode() {
            return Objects.hash(parent, frameElement);
        }
        
        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            
            if (!(object instanceof FrameByElementWebDriverTarget)) {
                return false;
            }
            
            FrameByElementWebDriverTarget other = (FrameByElementWebDriverTarget) object;
            
            return this.frameElement.equals(other.frameElement)
                    && this.parent.equals(other.parent);
        }
        
        @Override
        public String toString() {
            return "FrameByElementWebDriverTarget: {parent: " + parent + ", frameElement: " 
                    + frameElement + "}";
        }
    }

    public static class DefaultContextWebDriverTarget implements WebDriverTarget {

        @Override
        public WebDriver switchTo(TargetLocator targetLocator) {
            return targetLocator.defaultContent();
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
        }

        @Override
        public boolean equals(Object object) {
            return object instanceof DefaultContextWebDriverTarget;
        }

        @Override
        public String toString() {
            return "DefaultContextWebDriverTarget";
        }
    }

    public static final class ViewWebDriverTarget implements WebDriverTarget, Caching {
        private final View view;
        private final ParentContext parentContext;

        /**
         * Stores window handle of window which has view loaded so subsequent lookups always refer
         * to same window.
         */
        private String windowHandle;

        public ViewWebDriverTarget(View view, ParentContext parentContext) {
            this.view = Objects.requireNonNull(view, "view");
            this.parentContext = Objects.requireNonNull(parentContext, "parentContext");

            if (!(parentContext instanceof FindsById)) {
                throw new IllegalArgumentException("Context must be able to find by id using " +
                        "WebDriver window handles.");
            }
        }

        @Override
        public WebDriver switchTo(TargetLocator targetLocator) {
            if (windowHandle == null) {
                windowHandle = findWindow(targetLocator);
            }

            return targetLocator.window(windowHandle);
        }

        @Override
        public void invalidateCache() {
            windowHandle = null;
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ViewWebDriverTarget that = (ViewWebDriverTarget) o;
            return Objects.equals(view, that.view) &&
                    Objects.equals(parentContext, that.parentContext);
        }

        @Override
        public String toString() {
            return "ViewWebDriverTarget{" +
                    "view=" + view +
                    ", parentContext=" + parentContext +
                    ", windowHandle='" + windowHandle + '\'' +
                    '}';
        }

        private String findWindow(TargetLocator targetLocator) {
            for (String windowHandle : targetLocator.defaultContent().getWindowHandles()) {
                Browser forWindowHandle = By.id(windowHandle).find(Browser.class, parentContext);

                view.setContext(forWindowHandle);

                if (view.isLoaded()) {
                    return windowHandle;
                }
            }

            throw new NoSuchWindowException("No window in driver found which has " + view + " "
                    + "currently loaded.");
        }
    }

    private static class WindowTitleWebDriverTarget implements WebDriverTarget, Caching {
        private final String title;

        /**
         * Stores window handle of window which has the title so subsequent lookups always refer to
         * the same window.
         */
        private String windowHandle;

        public WindowTitleWebDriverTarget(String title) {
            this.title = Objects.requireNonNull(title, "title");
        }

        @Override
        public WebDriver switchTo(TargetLocator targetLocator) {
            if (windowHandle == null) {
                windowHandle = findWindow(targetLocator);
            }

            return targetLocator.window(windowHandle);
        }

        @Override
        public void invalidateCache() {
            windowHandle = null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            WindowTitleWebDriverTarget that = (WindowTitleWebDriverTarget) o;
            return Objects.equals(title, that.title);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title);
        }

        @Override
        public String toString() {
            return "WindowTitleWebDriverTarget{" +
                    "title='" + title + '\'' +
                    ", windowHandle='" + windowHandle + '\'' +
                    '}';
        }

        private String findWindow(TargetLocator targetLocator) {
            for (String windowHandle : targetLocator.defaultContent().getWindowHandles()) {
                if (targetLocator.window(windowHandle).getTitle().equals(title)) {
                    return windowHandle;
                }
            }

            throw new NoSuchWindowException("No window in driver found which has title: " + title);
        }
    }

    public static class WindowUrlWebDriverTarget implements WebDriverTarget, Caching {
        private final Matcher<? super String> urlMatcher;

        /**
         * Stores window handle of window which has the title so subsequent lookups always refer to
         * the same window.
         */
        private String windowHandle;

        public WindowUrlWebDriverTarget(Matcher<? super String> urlMatcher) {
            this.urlMatcher = Objects.requireNonNull(urlMatcher, "urlMatcher");
        }

        @Override
        public WebDriver switchTo(TargetLocator targetLocator) {
            if (windowHandle == null) {
                windowHandle = findWindow(targetLocator);
            }

            return targetLocator.window(windowHandle);
        }

        @Override
        public void invalidateCache() {
            windowHandle = null;
        }

        private String findWindow(TargetLocator targetLocator) {
            for (String windowHandle : targetLocator.defaultContent().getWindowHandles()) {
                if (urlMatcher.matches(targetLocator.window(windowHandle).getCurrentUrl())) {
                    return windowHandle;
                }
            }

            throw new NoSuchWindowException("No window in driver found which has url matching: " + urlMatcher);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            WindowUrlWebDriverTarget that = (WindowUrlWebDriverTarget) o;
            return Objects.equals(urlMatcher, that.urlMatcher) &&
                    Objects.equals(windowHandle, that.windowHandle);
        }

        @Override
        public int hashCode() {
            return Objects.hash(urlMatcher, windowHandle);
        }

        @Override
        public String toString() {
            return "WindowUrlWebDriverTarget{" +
                    "urlMatcher=" + urlMatcher +
                    ", windowHandle='" + windowHandle + '\'' +
                    '}';
        }
    }
}
