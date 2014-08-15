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

import com.redhat.darcy.web.api.elements.HtmlTextInput;
import com.redhat.darcy.webdriver.ElementConstructorMap;
import com.redhat.darcy.webdriver.internal.ElementLookup;

import org.openqa.selenium.WebElement;

public class WebDriverTextInput extends WebDriverElement implements HtmlTextInput {
    public WebDriverTextInput(ElementLookup source, ElementConstructorMap elementMap) {
        super(source, elementMap);
    }
    
    @Override
    public void clearAndType(String stringToType) {
        clear();
        sendKeys(stringToType);
        // TODO: send TAB key to trigger blur events
    }
    
    @Override
    public void sendKeys(CharSequence... keysToSend) {
        attempt(e -> e.sendKeys(keysToSend));
    }
    
    @Override
    public String readValue() {
        return attemptAndGet(e -> e.getAttribute("value"));
    }
    
    @Override
    public void click() {
        attempt(WebElement::click);
    }

    @Override
    public void clear() {
        attempt(WebElement::clear);
    }

    @Override
    public boolean isEnabled() {
        return attemptAndGet(WebElement::isEnabled);
    }
}
