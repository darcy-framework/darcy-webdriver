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

import org.openqa.selenium.remote.DesiredCapabilities;

public interface BrowserType {
    /**
     * Used for configuring a remote driver to use this browser.
     * @return A {@link org.openqa.selenium.remote.DesiredCapabilities} object that requests the
     * browser type.
     */
    DesiredCapabilities asCapability();

    /**
     * Used for launching a local browser.
     * @return A {@link com.redhat.darcy.web.api.BrowserFactory} that instantiates browsers of the
     * browser type.
     */
    BrowserFactory asBrowserFactory();
}
