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

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

import com.redhat.darcy.webdriver.internal.CachingTargetLocator;
import com.redhat.darcy.webdriver.internal.ForwardingTargetedWebDriver;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class HtmlUnitBrowserFactoryTest {
    private WebDriverBrowser browser;

    @After
    public void closeDriver() {
        if (browser != null) {
            browser.close();
        }
    }
    @Test
    public void shouldBeInstanceOfUntargetedHtmlUnitDriver() {
        WebDriverBrowserFactory browserFactory = new HtmlUnitBrowserFactory();

        browser = (WebDriverBrowser) browserFactory.newBrowser();
        ForwardingTargetedWebDriver webDriver = (ForwardingTargetedWebDriver) browser.getWrappedDriver();
        CachingTargetLocator targetLocator = (CachingTargetLocator) webDriver.getTargetLocator();

        assertThat(targetLocator.getUntargetedDriver(), instanceOf(HtmlUnitDriver.class));
    }
}
