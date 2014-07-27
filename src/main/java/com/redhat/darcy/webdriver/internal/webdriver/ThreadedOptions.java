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

package com.redhat.darcy.webdriver.internal.webdriver;

import static org.openqa.selenium.WebDriver.Options;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.Logs;

import java.util.Set;
import java.util.concurrent.ExecutorService;

public class ThreadedOptions implements Options {
    private final Options options;
    private final ExecutorService executor;

    public ThreadedOptions(Options options, ExecutorService executor) {
        this.options = options;
        this.executor = executor;
    }

    @Override
    public void addCookie(Cookie cookie) {

    }

    @Override
    public void deleteCookieNamed(String name) {

    }

    @Override
    public void deleteCookie(Cookie cookie) {

    }

    @Override
    public void deleteAllCookies() {

    }

    @Override
    public Set<Cookie> getCookies() {
        return null;
    }

    @Override
    public Cookie getCookieNamed(String name) {
        return null;
    }

    @Override
    public WebDriver.Timeouts timeouts() {
        return null;
    }

    @Override
    public WebDriver.ImeHandler ime() {
        return null;
    }

    @Override
    public WebDriver.Window window() {
        return null;
    }

    @Override
    public Logs logs() {
        return null;
    }
}
