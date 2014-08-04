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

import com.redhat.darcy.webdriver.internal.webdriver.CachingTargetLocator;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;

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
    
    @Deprecated
    public static WebDriverTarget nested(WebDriverTarget parent, WebDriverTarget child) {
        return new NestedWebDriverTarget(parent, child);
    }
    
    public static class WindowWebDriverTarget implements WebDriverTarget {
        private final String nameOrHandle;
        
        WindowWebDriverTarget(String nameOrHandle) {
            this.nameOrHandle = nameOrHandle;
        }
        
        @Override
        public WebDriver switchTo(TargetLocator targetLocator) {
            return targetLocator.window(nameOrHandle);
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
    
    public static class FrameByIndexWebDriverTarget implements WebDriverTarget {
        private final WebDriverTarget parent;
        private final int index;
        
        FrameByIndexWebDriverTarget(WebDriverTarget parent, int index) {
            this.parent = parent;
            this.index = index;
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
    
    public static class FrameByNameOrIdWebDriverTarget implements WebDriverTarget {
        private final WebDriverTarget parent;
        private final String nameOrId;
        
        FrameByNameOrIdWebDriverTarget(WebDriverTarget parent, String nameOrId) {
            this.parent = parent;
            this.nameOrId = nameOrId;
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
    
    public static class FrameByElementWebDriverTarget implements WebDriverTarget {
        private final WebDriverTarget parent;
        private final WebElement frameElement;
        
        FrameByElementWebDriverTarget(WebDriverTarget parent, WebElement frameElement) {
            this.parent = parent;
            this.frameElement = frameElement;
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
    
    @Deprecated
    public static class NestedWebDriverTarget implements WebDriverTarget {
        private final WebDriverTarget parent;
        private final WebDriverTarget child;
        
        public NestedWebDriverTarget(WebDriverTarget parent, WebDriverTarget child) {
            this.parent = parent;
            this.child = child;
        }
        
        @Override
        public WebDriver switchTo(TargetLocator targetLocator) {
            parent.switchTo(targetLocator);
            return child.switchTo(targetLocator);
        }
        
        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            
            if (!(object instanceof NestedWebDriverTarget)) {
                return false;
            }
            
            NestedWebDriverTarget other = (NestedWebDriverTarget) object;
            
            return this.child.equals(other.child)
                    && this.parent.equals(other.parent);
        }
        
        @Override
        public String toString() {
            return "NestedWebDriverTarget: {parent: " + parent + ", child: " + child + "}";
        }
    }
}
