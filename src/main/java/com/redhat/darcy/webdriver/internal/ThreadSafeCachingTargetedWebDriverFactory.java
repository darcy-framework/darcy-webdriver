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

import com.redhat.darcy.webdriver.internal.webdriver.ForwardingTargetedWebDriver;
import com.redhat.darcy.webdriver.internal.webdriver.RefindingWebDriver;
import com.redhat.darcy.webdriver.internal.webdriver.ThreadedTargetedWebDriver;

import org.openqa.selenium.WebDriver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadSafeCachingTargetedWebDriverFactory implements TargetedWebDriverFactory {
    // It's important that all access to driver is gated by same executor
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final CachingTargetLocator cachingTargetLocator;

    public ThreadSafeCachingTargetedWebDriverFactory(WebDriver untargetedDriver,
            WebDriverTarget currentTarget) {
        cachingTargetLocator = new CachingTargetLocator(currentTarget,
                new RefindingWebDriver(untargetedDriver));
    }

    @Override
    public TargetedWebDriver getTargetedWebDriver(WebDriverTarget target) {
        // Order is important here: we want atomic operations to be executed in a single threaded
        // queue, so driver switching needs to happen in the same queued task as the subsequent
        // action. This means that the outer most driver should queue the operations of the
        // forwarded driver -- which takes care of the switching.

        // Inner to outer:
        // 1. Refinding -- finds elements that can refind themselves if stale (these show up because
        //    CachingTargetLocator is powered by a RefindingDriver).
        // 2. Targeted -- targets a specific driver before proceeding, creates elements that do same
        //    (So with refinding, creates elements that target a driver, then can refind themselves
        //    if stale).
        // 3. Threaded -- Assures atomic, single threaded interaction with driver from any source
        return new ThreadedTargetedWebDriver(
                new ForwardingTargetedWebDriver(cachingTargetLocator, target), executor);
    }
}
