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

import com.redhat.darcy.util.Caching;
import com.redhat.darcy.webdriver.internal.WebDriverTarget;
import com.redhat.darcy.webdriver.internal.WebDriverTargets;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * An implementation of {@link org.openqa.selenium.WebDriver.TargetLocator} that caches the current
 * target, so that attempting to switch to the same target multiple times in a row will make no call
 * to the driver.
 */
public class CachingTargetLocator implements TargetLocator, Caching {
    private WebDriverTarget currentTarget;
    private WebDriver driver;

    private static final Logger logger = Logger.getLogger(CachingTargetLocator.class.toString());

    public CachingTargetLocator(WebDriverTarget currentTarget, WebDriver driver) {
        this.currentTarget = currentTarget;
        this.driver = Objects.requireNonNull(driver);
    }

    public WebDriver frame(WebDriverTarget parent, int index) {
        return switchTo(WebDriverTargets.frame(parent, index));
    }

    public WebDriver frame(WebDriverTarget parent, String nameOrId) {
        return switchTo(WebDriverTargets.frame(parent, nameOrId));
    }

    public WebDriver frame(WebDriverTarget parent, WebElement frameElement) {
        return switchTo(WebDriverTargets.frame(parent, frameElement));
    }

    @Override
    public WebDriver frame(int index) {
        return switchTo(WebDriverTargets.frame(currentTarget, index));
    }

    @Override
    public WebDriver frame(String nameOrId) {
        return switchTo(WebDriverTargets.frame(currentTarget, nameOrId));
    }

    @Override
    public WebDriver frame(WebElement frameElement) {
        return switchTo(WebDriverTargets.frame(currentTarget, frameElement));
    }

    @Override
    public WebDriver window(String nameOrHandle) {
        return switchTo(WebDriverTargets.window(nameOrHandle));
    }

    @Override
    public WebDriver defaultContent() {
        // Can probably cache this target when we have a class for it
        invalidateCache();
        return driver.switchTo().defaultContent();
    }

    @Override
    public WebElement activeElement() {
        // Subsequent calls to active element could return different elements, so make sure we
        // don't cache this
        invalidateCache();
        return driver.switchTo().activeElement();
    }

    @Override
    public Alert alert() {
        // Don't cache this
        invalidateCache();
        return driver.switchTo().alert();
    }

    @Override
    public void invalidateCache() {
        currentTarget = null;
    }

    public WebDriverTarget getCurrentTarget() {
        return currentTarget;
    }

    private WebDriver switchTo(WebDriverTarget newTarget) {
        if (!newTarget.equals(currentTarget)) {
            logger.info("Switching to new target... " + newTarget.toString());
            newTarget.switchTo(driver.switchTo());
            currentTarget = newTarget;
        }

        return driver;
    }
}
