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

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

public class WebElementListLookup implements ElementListLookup {
    private final By by;
    private final SearchContext sc;

    public WebElementListLookup(By by, SearchContext sc) {
        this.by = by;
        this.sc = sc;
    }

    @Override
    public List<WebElement> lookup() {
        return by.findElements(sc);
    }

    @Override
    public String toString() {
        return "A list of elements found by, " + by + "\n" +
                "in search context, " + sc;
    }
}
