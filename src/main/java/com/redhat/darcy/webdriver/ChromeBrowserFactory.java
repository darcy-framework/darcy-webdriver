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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;


/**
 * Fluently describes a {@link com.redhat.darcy.web.api.BrowserFactory} that creates specifically
 * configured {@link org.openqa.selenium.chrome.ChromeDriver}.
 */
public class ChromeBrowserFactory extends WebDriverBrowserFactory<ChromeBrowserFactory> {
    private DesiredCapabilities capabilities;
    private ChromeOptions options;
    private ChromeDriverService service;
    private ElementConstructorMap elementImpls = ElementConstructorMap.defaultMap();

    @Override
    public Browser newBrowser() {
        ChromeDriver driver;

        if (service != null) {
            if (options != null) {
                driver = new ChromeDriver(service, options);
            } else if (capabilities != null) {
                driver = new ChromeDriver(service, capabilities);
            } else {
                driver = new ChromeDriver(service);
            }
        } else if (capabilities != null) {
            driver = new ChromeDriver(capabilities);
        } else if (options != null) {
            driver = new ChromeDriver(options);
        } else {
            driver = new ChromeDriver();
        }
        return makeBrowser(driver, elementImpls);
    }

    public ChromeBrowserFactory capableOf(Capabilities cap) {
        capabilities = new DesiredCapabilities(cap, capabilities);
        return this;
    }

    public ChromeBrowserFactory usingOptions(ChromeOptions co) {
        options = co;
        return this;
    }

    public ChromeBrowserFactory usingService(ChromeDriverService cds) {
        service = cds;
        return this;
    }

    @Override
    public <E extends WebDriverElement> ChromeBrowserFactory withElementImplementation(
            Class<? super E> type, ElementConstructor<E> constructor) {
        elementImpls.put(type, constructor);
        return this;
    }
}
