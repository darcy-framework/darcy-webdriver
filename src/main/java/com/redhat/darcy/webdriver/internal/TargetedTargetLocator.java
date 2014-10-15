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

import static org.openqa.selenium.WebDriver.TargetLocator;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TargetedTargetLocator implements TargetLocator {
    private final TargetLocator locator;
    private final WebDriverTarget self;

    public TargetedTargetLocator(TargetLocator locator, WebDriverTarget self) {
        this.locator = locator;
        this.self = self;
    }

    @Override
    public ForwardingTargetedWebDriver frame(int index) {
        return new ForwardingTargetedWebDriver(locator, WebDriverTargets.frame(self, index));
    }

    @Override
    public ForwardingTargetedWebDriver frame(String nameOrId) {
        return new ForwardingTargetedWebDriver(locator, WebDriverTargets.frame(self, nameOrId));
    }

    @Override
    public ForwardingTargetedWebDriver frame(WebElement frameElement) {
        return new ForwardingTargetedWebDriver(locator, WebDriverTargets.frame(self, frameElement));
    }

    @Override
    public WebDriver parentFrame() {
        return new ForwardingTargetedWebDriver(locator, WebDriverTargets.parentOf(self));
    }

    @Override
    public ForwardingTargetedWebDriver window(String nameOrHandle) {
        return new ForwardingTargetedWebDriver(locator, WebDriverTargets.window(nameOrHandle));
    }

    @Override
    public WebDriver defaultContent() {
        // TODO: This target is kind of irrelevant to darcy, how to deal?
        return locator.defaultContent();
    }

    @Override
    public WebElement activeElement() {
        return locator.activeElement();
    }

    @Override
    public Alert alert() {
        // TODO
        return locator.alert();
    }
}
