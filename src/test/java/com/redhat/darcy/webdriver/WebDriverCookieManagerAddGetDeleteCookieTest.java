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

import com.redhat.darcy.web.Cookie;
import com.redhat.darcy.webdriver.internal.TargetedWebDriver;
import com.redhat.darcy.webdriver.internal.WebDriverCookieManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.Parameter;
import org.openqa.selenium.WebDriver;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class WebDriverCookieManagerAddGetDeleteCookieTest {

    private static final Object[][] parameterizedChars = {
            {new Cookie("chocolate", "chip")},
            {new Cookie("chocolate", "chip", "")},
            {new Cookie("chocolate", "chip", "", LocalDateTime.now().plusDays(1))},
            {new Cookie("chocolate", "chip", "", null)},
            {new Cookie("chocolate", "chip", null, "", LocalDateTime.now().plusDays(1))},
            {new Cookie("chocolate", "chip", null, "", null, false)},
            {new Cookie("chocolate", "chip", null, "", null, false, false)},
    };

    @Parameter
    public Cookie cookie;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(parameterizedChars);
    }

    @Test
    public void shouldAddCookie() {
        TargetedWebDriver mockDriver = mock(TargetedWebDriver.class);
        WebDriver.Options mockOptions = mock(WebDriver.Options.class);

        when(mockDriver.manage())
                .thenReturn(mockOptions);

        WebDriverCookieManager cookieManager = new WebDriverCookieManager(mockDriver);
        cookieManager.add(cookie);

        verify(mockOptions)
                .addCookie(new org.openqa.selenium.Cookie(cookie.getName(), cookie.getValue()));
    }

    @Test
    public void shouldGetCookie() {
        TargetedWebDriver mockDriver = mock(TargetedWebDriver.class);
        WebDriver.Options mockOptions = mock(WebDriver.Options.class);

        when(mockDriver.manage())
                .thenReturn(mockOptions);

        WebDriverCookieManager cookieManager = new WebDriverCookieManager(mockDriver);
        cookieManager.get(cookie);

        verify(mockOptions)
                .getCookieNamed(cookie.getName());
    }

    @Test
    public void shouldDeleteCookie() {
        TargetedWebDriver mockDriver = mock(TargetedWebDriver.class);
        WebDriver.Options mockOptions = mock(WebDriver.Options.class);

        when(mockDriver.manage())
                .thenReturn(mockOptions);

        WebDriverCookieManager cookieManager = new WebDriverCookieManager(mockDriver);
        cookieManager.delete(cookie);

        verify(mockOptions)
                .deleteCookieNamed(cookie.getName());
    }
}
