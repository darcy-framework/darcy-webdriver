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
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;

/**
 * Fluently describes a {@link com.redhat.darcy.web.api.BrowserFactory} that creates specifically
 * configured {@link org.openqa.selenium.ie.InternetExplorerDriver}.
 */
public class InternetExplorerBrowserFactory extends WebDriverBrowserFactory<InternetExplorerBrowserFactory> {
    private InternetExplorerDriverService service;
    private Capabilities capabilities;
    private Integer port;
    private ElementConstructorMap elementImpls = ElementConstructorMap.defaultMap();

    @Override
    public Browser newBrowser() {
        InternetExplorerDriver driver;
        if (service != null) {
            if (capabilities != null) {
                if (port != null) {
                    driver = new InternetExplorerDriver(service, capabilities, port);
                }
                else {
                    driver = new InternetExplorerDriver(service, capabilities);
                }
            }
            else {
                driver = new InternetExplorerDriver(service);
            }
        }
        else if (capabilities != null) {
            driver = new InternetExplorerDriver(capabilities);
        }
        else {
            driver = new InternetExplorerDriver();
        }
        return makeBrowser(driver, elementImpls);
    }

    public InternetExplorerBrowserFactory capableOf(Capabilities cap) {
        capabilities = cap;
        return this;
    }

    public InternetExplorerBrowserFactory usingService(InternetExplorerDriverService ieds) {
        service = ieds;
        return this;
    }

    public InternetExplorerBrowserFactory usingPort(Integer port) {
        this.port = port;
        return this;
    }

    @Override
    public <E extends WebDriverElement> InternetExplorerBrowserFactory withElementImplementation
            (Class<? super E> type, ElementConstructor<E> constructor) {
        elementImpls.put(type, constructor);
        return this;
    }
}
