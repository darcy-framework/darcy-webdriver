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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.redhat.darcy.web.Cookie;
import com.redhat.darcy.webdriver.internal.TargetedWebDriver;
import com.redhat.darcy.webdriver.internal.WebDriverCookieManager;

import org.junit.Test;
import org.openqa.selenium.WebDriver;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

public class WebDriverCookieManagerTest {

    @Test
    public void shouldProperlyAddACookieWithANameAndValue() {
        Cookie cookie = new Cookie("chocolate", "chip");
        org.openqa.selenium.Cookie seleniumCookie =
                new org.openqa.selenium.Cookie("chocolate", "chip");

        TargetedWebDriver mockDriver = mock(TargetedWebDriver.class);
        WebDriver.Options mockOptions = mock(WebDriver.Options.class);

        when(mockDriver.manage())
                .thenReturn(mockOptions);

        WebDriverCookieManager cookieManager = new WebDriverCookieManager(mockDriver);
        cookieManager.add(cookie);

        verify(mockOptions)
                .addCookie(seleniumCookie);
    }

    @Test
    public void shouldProperlyAddACookieWithANameValueAndPath() {
        Cookie cookie = new Cookie("chocolate", "chip", null);
        org.openqa.selenium.Cookie seleniumCookie =
                new org.openqa.selenium.Cookie("chocolate", "chip", null);

        TargetedWebDriver mockDriver = mock(TargetedWebDriver.class);
        WebDriver.Options mockOptions = mock(WebDriver.Options.class);

        when(mockDriver.manage())
                .thenReturn(mockOptions);

        WebDriverCookieManager cookieManager = new WebDriverCookieManager(mockDriver);
        cookieManager.add(cookie);

        verify(mockOptions)
                .addCookie(seleniumCookie);
    }

    @Test
    public void shouldProperlyAddACookieWithANameValuePathAndExpiry() {
        Cookie cookie = new Cookie("chocolate", "chip", "home", LocalDateTime.now());
        org.openqa.selenium.Cookie seleniumCookie =
                new org.openqa.selenium.Cookie("chocolate", "chip", "home", Date.from(Instant.now()));

        TargetedWebDriver mockDriver = mock(TargetedWebDriver.class);
        WebDriver.Options mockOptions = mock(WebDriver.Options.class);

        when(mockDriver.manage())
                .thenReturn(mockOptions);

        WebDriverCookieManager cookieManager = new WebDriverCookieManager(mockDriver);
        cookieManager.add(cookie);

        verify(mockOptions)
                .addCookie(seleniumCookie);
    }

    @Test
    public void shouldProperlyAddACookieWithANameValuePathAndNullExpiry() {
        Cookie cookie = new Cookie("chocolate", "chip", "home", null);
        org.openqa.selenium.Cookie seleniumCookie =
                new org.openqa.selenium.Cookie("chocolate", "chip", "home", null);

        TargetedWebDriver mockDriver = mock(TargetedWebDriver.class);
        WebDriver.Options mockOptions = mock(WebDriver.Options.class);

        when(mockDriver.manage())
                .thenReturn(mockOptions);

        WebDriverCookieManager cookieManager = new WebDriverCookieManager(mockDriver);
        cookieManager.add(cookie);

        verify(mockOptions)
                .addCookie(seleniumCookie);
    }

    @Test
    public void shouldProperlyAddACookieWithANameValueDomainPathAndExpiry() {
        Cookie cookie = new Cookie("chocolate", "chip", null, "", LocalDateTime.now());
        org.openqa.selenium.Cookie seleniumCookie =
                new org.openqa.selenium.Cookie("chocolate", "chip", null, "",
                        Date.from(Instant.now()));

        TargetedWebDriver mockDriver = mock(TargetedWebDriver.class);
        WebDriver.Options mockOptions = mock(WebDriver.Options.class);

        when(mockDriver.manage())
                .thenReturn(mockOptions);

        WebDriverCookieManager cookieManager = new WebDriverCookieManager(mockDriver);
        cookieManager.add(cookie);

        verify(mockOptions)
                .addCookie(seleniumCookie);
    }

    @Test
    public void shouldGetCookie() {
        Cookie cookie = new Cookie("chocolate", "chip");
        org.openqa.selenium.Cookie seleniumCookie =
                new org.openqa.selenium.Cookie("chocolate", "chip");

        TargetedWebDriver mockDriver = mock(TargetedWebDriver.class);
        WebDriver.Options mockOptions = mock(WebDriver.Options.class);

        when(mockDriver.manage())
                .thenReturn(mockOptions);
        when(mockOptions.getCookieNamed(cookie.getName()))
                .thenReturn(seleniumCookie);

        WebDriverCookieManager cookieManager = new WebDriverCookieManager(mockDriver);

        assertThat("The retrieved cookie should be transformed properly",
                cookieManager.get(cookie), equalTo(cookie));
    }
}
