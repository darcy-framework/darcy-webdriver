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

import com.redhat.darcy.ui.ElementContext;
import com.redhat.darcy.ui.elements.SelectOption;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WebDriverSelectOption extends WebDriverElement implements SelectOption {
    
    public WebDriverSelectOption(WebElement source, WebDriver parent, ElementContext elementContext) {
        super(source, parent, elementContext);
    }

    @Override
    public String readText() {
        return getWrappedElement().getText();
    }
    
    @Override
    public void select() {
        getWrappedElement().click();
    }
    
    @Override
    public boolean isSelected() {
        return getWrappedElement().isSelected();
    }
    
}
