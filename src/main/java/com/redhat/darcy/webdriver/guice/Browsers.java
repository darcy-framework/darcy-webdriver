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

package com.redhat.darcy.webdriver.guice;

import com.redhat.darcy.web.api.BrowserFactory;
import com.redhat.darcy.webdriver.FirefoxBrowserFactory;

import org.openqa.selenium.remote.DesiredCapabilities;

public enum Browsers implements BrowserType {
    FIREFOX(DesiredCapabilities.firefox(), new FirefoxBrowserFactory());

    /**
     * Picks a browser type based off of the "darcy.browser" environment variable. Case-insensitive.
     */
    public static Browsers getBrowserFromEnv() {
        return valueOf(System.getProperty("darcy.browser").toUpperCase());
    }

    private final DesiredCapabilities caps;
    private final BrowserFactory factory;

    Browsers(DesiredCapabilities caps, BrowserFactory factory) {
        this.caps = caps;
        this.factory = factory;
    }

    @Override
    public DesiredCapabilities asCapability() {
        return caps;
    }

    @Override
    public BrowserFactory asBrowserFactory() {
        return factory;
    }
}
