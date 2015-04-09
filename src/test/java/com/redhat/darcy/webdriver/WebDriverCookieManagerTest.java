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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.redhat.darcy.web.Cookie;
import com.redhat.darcy.webdriver.internal.TargetedWebDriver;
import com.redhat.darcy.webdriver.internal.WebDriverCookieManager;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

public class WebDriverCookieManagerTest {

    private TargetedWebDriver mockDriver = mock(TargetedWebDriver.class);
    private WebDriver.Options mockOptions = mock(WebDriver.Options.class);
    private WebDriverCookieManager cookieManager = new WebDriverCookieManager(mockDriver);

    @Before
    public void stubDriver() {
        when(mockDriver.manage())
                .thenReturn(mockOptions);
    }

    @Test
    public void shouldProperlyAddACookieWithANameAndValue() {
        Cookie cookie = new Cookie("chocolate", "chip");
        org.openqa.selenium.Cookie seleniumCookie =
                new org.openqa.selenium.Cookie("chocolate", "chip");

        cookieManager.add(cookie);

        verify(mockOptions)
                .addCookie(seleniumCookie);
    }

    @Test
    public void shouldProperlyAddACookieWithANameValueAndPath() {
        Cookie cookie = new Cookie("chocolate", "chip", null);
        org.openqa.selenium.Cookie seleniumCookie =
                new org.openqa.selenium.Cookie("chocolate", "chip", null);

        cookieManager.add(cookie);

        verify(mockOptions)
                .addCookie(seleniumCookie);
    }

    @Test
    public void shouldProperlyAddACookieWithANameValuePathAndExpiry() {
        Instant instantNow = Instant.now();
        Cookie cookie = new Cookie("chocolate", "chip", "home", LocalDateTime.ofInstant(instantNow,
                ZoneId.systemDefault()));
        org.openqa.selenium.Cookie seleniumCookie =
                new org.openqa.selenium.Cookie("chocolate", "chip", "home", Date.from(Instant.now()));

        cookieManager.add(cookie);

        verify(mockOptions)
                .addCookie(seleniumCookie);
    }

    @Test
    public void shouldProperlyAddACookieWithANameValuePathAndNullExpiry() {
        Cookie cookie = new Cookie("chocolate", "chip", "home", null);
        org.openqa.selenium.Cookie seleniumCookie =
                new org.openqa.selenium.Cookie("chocolate", "chip", "home", null);

        cookieManager.add(cookie);

        verify(mockOptions)
                .addCookie(seleniumCookie);
    }

    @Test
    public void shouldProperlyAddACookieWithANameValueDomainPathAndExpiry() {
        Instant instantNow = Instant.now();
        Cookie cookie = new Cookie("chocolate", "chip", null, "", LocalDateTime.ofInstant(instantNow,
                ZoneId.systemDefault()));
        org.openqa.selenium.Cookie seleniumCookie =
                new org.openqa.selenium.Cookie("chocolate", "chip", null, "", Date.from(instantNow));

        cookieManager.add(cookie);

        verify(mockOptions)
                .addCookie(seleniumCookie);
    }

    @Test
    public void shouldGetCookie() {
        Instant instantNow = Instant.now();
        Cookie cookie = new Cookie("chocolate", "chip", null, "", LocalDateTime.ofInstant(instantNow,
                ZoneId.systemDefault()));
        org.openqa.selenium.Cookie seleniumCookie = new org.openqa.selenium.Cookie("chocolate",
                "chip", null, "", Date.from(instantNow));

        when(mockOptions.getCookieNamed(cookie.getName()))
                .thenReturn(seleniumCookie);

        Cookie retrievedCookie = cookieManager.get(cookie)
                .get();

        assertThat("The retrieved cookie should be transformed properly",
                retrievedCookie, equalTo(cookie));
    }

    @Test
    public void shouldReturnEmptyIfNoCookieFound() {
        Cookie cookie = new Cookie("chocolate", "chip");

        assertThat(cookieManager.get(cookie), equalTo(Optional.empty()));
    }

    @Test
    public void shouldDeleteCookie() {
        Cookie cookie = new Cookie("chocolate", "chip");

        cookieManager.delete(cookie);

        verify(mockOptions)
                .deleteCookieNamed(cookie.getName());
    }

    @Test
    public void shouldGetAllCookies() {
        Set allCookies = cookieManager.getAll();

        verify(mockOptions)
                .getCookies();

        assertTrue(allCookies.isEmpty());
    }

    @Test
    public void shouldDeleteAllCookies() {
        cookieManager.deleteAll();

        verify(mockOptions)
                .deleteAllCookies();
    }
}
