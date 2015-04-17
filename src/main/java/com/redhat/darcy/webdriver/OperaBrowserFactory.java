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

import com.opera.core.systems.OperaDriver;
import com.opera.core.systems.OperaProfile;
import com.opera.core.systems.OperaSettings;
import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.webdriver.elements.WebDriverElement;
import org.openqa.selenium.Capabilities;

/**
 * Fluently describes a {@link com.redhat.darcy.web.api.BrowserFactory} that creates specifically
 * configured {@link com.opera.core.systems.OperaDriver}.
 */
public class OperaBrowserFactory extends WebDriverBrowserFactory<OperaBrowserFactory> {
    private Capabilities capabilities;
    private OperaProfile profile;
    private OperaSettings settings;
    private ElementConstructorMap elementImpls = ElementConstructorMap.defaultMap();

    @Override
    public Browser newBrowser() {
        OperaDriver driver;

        if (profile != null) {
            driver = new OperaDriver(profile);
        } else if (capabilities != null) {
            driver = new OperaDriver(capabilities);
        } else if (settings != null) {
            driver = new OperaDriver(settings);
        } else {
            driver = new OperaDriver();
        }
        return makeBrowser(driver, elementImpls);
    }

    public OperaBrowserFactory capableOf(Capabilities cap) {
        capabilities = cap;
        return this;
    }

    public OperaBrowserFactory usingOptions(OperaProfile prf) {
        profile = prf;
        return this;
    }

    public OperaBrowserFactory usingService(OperaSettings set) {
        settings = set;
        return this;
    }

    @Override
    public <E extends WebDriverElement> OperaBrowserFactory withElementImplementation(
            Class<? super E> type, ElementConstructor<E> constructor) {
        elementImpls.put(type, constructor);
        return this;
    }
}
