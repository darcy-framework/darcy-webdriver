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

package com.redhat.darcy.webdriver.locators;

import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.webdriver.internal.WebElementContext;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ByChained extends By {
    private Locator[] locators;

    public ByChained(Locator[] locators) {
        this.locators = Objects.requireNonNull(locators, "locators");
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        return new WebElementContext(context).findAllByChained(WebElement.class, locators);
    }

    @Override
    public String toString() {
        return "ByChained: {locators: " + Arrays.toString(locators) + "}";
    }
}
