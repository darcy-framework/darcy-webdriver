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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.redhat.darcy.webdriver.internal.TargetedWebDriver;
import com.redhat.darcy.webdriver.internal.WebDriverCookieManager;

import org.junit.Test;
import org.openqa.selenium.WebDriver;

public class WebDriverCookieManagerTest {

    @Test
    public void shouldDeleteAllCookies() {
        TargetedWebDriver mockDriver = mock(TargetedWebDriver.class);
        WebDriver.Options mockOptions = mock(WebDriver.Options.class);

        when(mockDriver.manage())
                .thenReturn(mockOptions);

        WebDriverCookieManager cookieManager = new WebDriverCookieManager(mockDriver);
        cookieManager.deleteAll();

        verify(mockOptions)
                .deleteAllCookies();
    }

    @Test
    public void shouldGetAllCookies() {
        TargetedWebDriver mockDriver = mock(TargetedWebDriver.class);
        WebDriver.Options mockOptions = mock(WebDriver.Options.class);

        when(mockDriver.manage())
                .thenReturn(mockOptions);

        WebDriverCookieManager cookieManager = new WebDriverCookieManager(mockDriver);
        cookieManager.getAll();

        verify(mockOptions)
                .getCookies();
    }
}
