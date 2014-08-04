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

import java.util.concurrent.ExecutorService;

public class ThreadedTargetLocator extends Threaded implements TargetLocator {
    private final TargetedTargetLocator locator;

    public ThreadedTargetLocator(TargetedTargetLocator locator, ExecutorService executor) {
        super(executor);

        this.locator = locator;
    }

    @Override
    public WebDriver frame(int index) {
        return new ThreadedTargetedWebDriver(submitAndGet(() -> locator.frame(index)), executor);
    }

    @Override
    public WebDriver frame(String nameOrId) {
        return new ThreadedTargetedWebDriver(submitAndGet(() -> locator.frame(nameOrId)), executor);
    }

    @Override
    public WebDriver frame(WebElement frameElement) {
        return new ThreadedTargetedWebDriver(submitAndGet(() -> locator.frame(frameElement)),
                executor);
    }

    @Override
    public WebDriver window(String nameOrHandle) {
        return new ThreadedTargetedWebDriver(submitAndGet(() -> locator.window(nameOrHandle)),
                executor);
    }

    @Override
    public WebDriver defaultContent() {
        // TODO?
        return submitAndGet(locator::defaultContent);
    }

    @Override
    public WebElement activeElement() {
        return new ThreadedWebElement(submitAndGet(locator::activeElement), executor);
    }

    @Override
    public Alert alert() {
        // TODO
        return submitAndGet(locator::alert);
    }
}
