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

package com.redhat.darcy.webdriver;

import org.openqa.selenium.WebElement;

import com.redhat.darcy.ui.elements.Button;

public class WebDriverButton extends WebDriverElement implements Button {
    
    public WebDriverButton(WebElement source) {
        super(source);
    }
    
    @Override
    public void click() {
        me.click();
    }
    
    public static class WebDriverButtonFactory implements WebDriverElementFactory<Button> {
        @Override
        public Button element(WebElement source) {
            return new WebDriverButton(source);
        }
    }
}
