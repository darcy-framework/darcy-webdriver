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

import org.openqa.selenium.WebElement;

import java.util.function.Supplier;

public interface ElementLookup extends Supplier<WebElement> {
    /**
     * Attempts to retrieve a WebElement reference, which may throw a
     * {@link org.openqa.selenium.NoSuchElementException}. The retrieved element may or may not be
     * stale.
     *
     * @throws java.util.NoSuchElementException if the element cannot be located.
     */
    @Override
    WebElement get();
}
