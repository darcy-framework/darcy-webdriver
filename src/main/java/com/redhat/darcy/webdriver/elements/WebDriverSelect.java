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

import com.redhat.darcy.ui.Locator;
import com.redhat.darcy.ui.elements.Select;
import com.redhat.darcy.ui.elements.SelectOption;
import com.redhat.darcy.webdriver.ElementFinder;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class WebDriverSelect extends WebDriverElement implements Select {
    
    public WebDriverSelect(WebElement source, ElementFinder finder) {
        super(source, finder);
    }

    @Override
    public void select(Locator locator) {
        locator.find(SelectOption.class, this).select();
    }
    
    @Override
    public List<SelectOption> getOptions() {
        return finder.findElements(SelectOption.class, By.tagName("option"), me);
    }
    
    @Override
    public SelectOption getCurrentlySelectedOption() {
        for (SelectOption option : getOptions()) {
            if (option.isSelected()) {
                return option;
            }
        }
        
        // Should probably throw an exception instead of return null?
        return null;
    }
    
}
