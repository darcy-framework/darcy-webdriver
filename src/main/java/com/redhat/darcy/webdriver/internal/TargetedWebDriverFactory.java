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

/**
 * Wraps a vanilla {@link org.openqa.selenium.WebDriver} implementation, creating
 * {@link TargetedWebDriver}s for given {@link WebDriverTarget}s found within the wrapped
 * {@link org.openqa.selenium.WebDriver}.
 * <P>
 * {@link TargetedWebDriver}s are NOT thread safe.
 */
public interface TargetedWebDriverFactory {
    /**
     * Create a {@link TargetedWebDriver} for a given {@link WebDriverTarget}.
     * 
     * @param target
     * @return
     */
    TargetedWebDriver getTargetedWebDriver(WebDriverTarget target);
}
