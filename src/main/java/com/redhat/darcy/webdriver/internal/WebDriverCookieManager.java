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

package com.redhat.darcy.webdriver.internal;

import com.redhat.darcy.web.Cookie;
import com.redhat.darcy.web.api.CookieManager;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

public class WebDriverCookieManager implements CookieManager {
    private final TargetedWebDriver driver;

    public WebDriverCookieManager(TargetedWebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void add(Cookie cookie) {
        cookie.validate();

        driver.manage().addCookie(transformToSeleniumCookie(cookie));
    }

    @Override
    public void delete(String name) {
        driver.manage().deleteCookieNamed(name);
    }

    @Override
    public void deleteAll() {
        driver.manage().deleteAllCookies();
    }

    @Override
    public Set<Cookie> getAll() {
        return driver.manage().getCookies().stream()
                .map(this::transformToDarcyCookie)
                .collect(Collectors.toSet());
    }

    @Override
    public Cookie get(String name) {
        if(driver.manage().getCookieNamed(name) == null) {
            return null;
        }
        return transformToDarcyCookie(driver.manage().getCookieNamed(name));
    }

    private org.openqa.selenium.Cookie transformToSeleniumCookie(Cookie cookie) {
        if (cookie.getExpiry() == null) {
            if(cookie.getPath() == null || cookie.getPath().equals("/")) {
                return new org.openqa.selenium.Cookie(cookie.getName(), cookie.getValue());
            }
            return new org.openqa.selenium.Cookie(cookie.getName(),
                cookie.getValue(), cookie.getPath());
        }
        //From java.time.LocalDateTime to java.util.Date
        Date legacyDate = Date.from(cookie.getExpiry().atZone(ZoneId.systemDefault()).toInstant());

        if(cookie.getDomain() == null) {
            return new org.openqa.selenium.Cookie(cookie.getName(), cookie.getValue(),
                    cookie.getPath(), legacyDate);
        }
        return new org.openqa.selenium.Cookie(cookie.getName(), cookie.getValue(),
                cookie.getDomain(), cookie.getPath(), legacyDate, cookie.isSecure(),
                cookie.isHttpOnly());
    }

    private Cookie transformToDarcyCookie(org.openqa.selenium.Cookie cookie) {
        if (cookie.getExpiry() == null) {
            if(cookie.getPath() == null || cookie.getPath().equals("/")) {
                return new Cookie(cookie.getName(), cookie.getValue());
            }
            return new Cookie(cookie.getName(), cookie.getValue(), cookie.getPath());
        }
        // From java.util.Date to java.time.LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.ofInstant(cookie.getExpiry().toInstant(),
                ZoneId.systemDefault());

        if(cookie.getDomain() == null) {
            return new Cookie(cookie.getName(), cookie.getValue(),  cookie.getPath(), localDateTime);
        }
        return new Cookie(cookie.getName(), cookie.getValue(),
                cookie.getDomain(), cookie.getPath(), localDateTime, cookie.isSecure(),
                cookie.isHttpOnly());
    }
}
