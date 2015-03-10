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

import com.redhat.darcy.web.Cookie;
import com.redhat.darcy.webdriver.internal.TargetedWebDriver;
import com.redhat.darcy.webdriver.internal.WebDriverCookieManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RunWith(JUnit4.class)
public class WebDriverCookieManagerTest {

    @Test
    public void shouldTransformDarcyCookieWith2ArgsToSeleniumProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToSeleniumCookie(new Cookie("chocolate4", "chip"));
    }

    @Test
    public void shouldTransformDarcyCookieWith3ArgsToSeleniumProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToSeleniumCookie(new Cookie("chocolate", "chip", ""));
    }

    @Test
    public void shouldTransformDarcyCookieWith4ArgsToSeleniumProperlyWhenExpiryIsNotNull() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToSeleniumCookie(new Cookie("chocolate", "chip", "",
                        LocalDateTime.now().plusDays(1)));
    }

    @Test
    public void shouldTransformDarcyCookieWith4ArgsToSeleniumProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToSeleniumCookie(new Cookie("chocolate", "chip", "", null));
    }

    @Test
    public void shouldTransformDarcyCookieWith5ArgsToSeleniumProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToSeleniumCookie(new Cookie("chocolate", "chip", null, "",
                        LocalDateTime.now().plusDays(1)));
    }

    @Test
    public void shouldTransformDarcyCookieWith6ArgsToSeleniumProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToSeleniumCookie(new Cookie("chocolate", "chip", null, "", null, false));
    }

    @Test
    public void shouldTransformDarcyCookieWith7ArgsToSeleniumProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToSeleniumCookie(new Cookie("chocolate", "chip", null, "", null, false,
                        false));
    }

    @Test
    public void shouldTransformSeleniumCookieWith2ArgsToDarcyProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToDarcyCookie(new org.openqa.selenium.Cookie("chocolate", "chip"));
    }

    @Test
    public void shouldTransformSeleniumCookieWith3ArgsToDarcyProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToDarcyCookie(new org.openqa.selenium.Cookie("chocolate", "chip", ""));
    }

    @Test
    public void shouldTransformSeleniumCookieWith4ArgsToDarcyProperlyWhenExpiryNotNull() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToDarcyCookie(new org.openqa.selenium.Cookie("chocolate", "chip", "",
                        Date.valueOf(LocalDate.now().plusDays(1))));
    }

    @Test
    public void shouldTransformSeleniumCookieWith4ArgsToDarcyProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToDarcyCookie(new org.openqa.selenium.Cookie("chocolate", "chip", "",
                        null));
    }

    @Test
    public void shouldTransformSeleniumCookieWith5ArgsToDarcyProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToDarcyCookie(new org.openqa.selenium.Cookie("chocolate", "chip", null,
                        "", null));
    }

    @Test
    public void shouldTransformSeleniumCookieWith6ArgsToDarcyProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToDarcyCookie(new org.openqa.selenium.Cookie("chocolate", "chip", null,
                        "", null, false));
    }

    @Test
    public void shouldTransformSeleniumCookieWith7ArgsToDarcyProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToDarcyCookie(new org.openqa.selenium.Cookie("chocolate", "chip", null,
                        "", null, false, false));
    }
}
