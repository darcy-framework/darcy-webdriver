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

import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.webdriver.elements.WebDriverElement;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

/**
 * Fluently describes a {@link com.redhat.darcy.web.api.BrowserFactory} that creates specifically
 * configured {@link org.openqa.selenium.safari.SafariDriver}.
 */
public class SafariBrowserFactory extends WebDriverBrowserFactory<SafariBrowserFactory> {
    private Capabilities capabilities;
    private SafariOptions safariOptions;
    private ElementConstructorMap elementImpls = ElementConstructorMap.defaultMap();

    @Override
    public Browser newBrowser() {
        SafariDriver driver;
        if (capabilities != null) {
            driver = new SafariDriver(capabilities);
        }
        else if (safariOptions != null) {
            driver = new SafariDriver(safariOptions);
        }
        else {
            driver = new SafariDriver();
        }
        return makeBrowser(driver, elementImpls);
    }

    public SafariBrowserFactory capableOf(Capabilities cap) {
        capabilities = cap;
        return this;
    }

    public SafariBrowserFactory usingOptions(SafariOptions options) {
        safariOptions = options;
        return this;
    }

    @Override
    public <E extends WebDriverElement> SafariBrowserFactory withElementImplementation(Class<?
            super E> type, ElementConstructor<E> constructor) {
        elementImpls.put(type, constructor);
        return this;
    }
}
