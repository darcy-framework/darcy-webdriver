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

import static org.openqa.selenium.WebDriver.TargetLocator;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Since target locators return drivers, the returned drivers must themselves be finding. In this
 * way, {@link org.openqa.selenium.WebDriver.TargetLocator TargetLocators} are factories.
 */
public class RefindingTargetLocator implements TargetLocator {
    private final TargetLocator locator;

    public RefindingTargetLocator(TargetLocator locator) {
        this.locator = locator;
    }

    @Override
    public WebDriver frame(int index) {
        return new RefindingWebDriver(locator.frame(index));
    }

    @Override
    public WebDriver frame(String nameOrId) {
        return new RefindingWebDriver(locator.frame(nameOrId));
    }

    @Override
    public WebDriver frame(WebElement frameElement) {
        return new RefindingWebDriver(locator.frame(frameElement));
    }

    @Override
    public WebDriver window(String nameOrHandle) {
        return new RefindingWebDriver(locator.window(nameOrHandle));
    }

    @Override
    public WebDriver defaultContent() {
        return new RefindingWebDriver(locator.defaultContent());
    }

    // TODO: Implement? How?
    @Override
    public WebElement activeElement() {
        return locator.activeElement();
    }

    @Override
    public Alert alert() {
        return locator.alert();
    }
}
