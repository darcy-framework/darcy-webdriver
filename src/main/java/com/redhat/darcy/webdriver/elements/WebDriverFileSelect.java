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

package com.redhat.darcy.webdriver.elements;

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.web.api.elements.HtmlFileSelect;
import com.redhat.darcy.webdriver.internal.ElementLookup;

import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class WebDriverFileSelect extends WebDriverElement implements HtmlFileSelect {
    public WebDriverFileSelect(ElementLookup source, ElementContext context) {
        super(source, context);
    }

    @Override
    public Set<String> getAcceptedTypes() {
        return new HashSet<>(Arrays.asList(getAttribute("accept").split(",")));
    }

    @Override
    public void setFilePath(String path) {
        attempt(e -> e.sendKeys(path));
    }

    @Override
    public void clear() {
        attempt(WebElement::clear);
    }

    @Override
    public boolean isEnabled() {
        return attemptAndGet(WebElement::isEnabled);
    }

    @Override
    public String getValue() {
        return getAttribute("value");
    }
}
