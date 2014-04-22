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

import com.redhat.darcy.DarcyException;
import com.redhat.darcy.ui.elements.Button;
import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.ui.elements.Link;
import com.redhat.darcy.ui.elements.Select;
import com.redhat.darcy.ui.elements.SelectOption;
import com.redhat.darcy.ui.elements.TextInput;
import com.redhat.darcy.webdriver.elements.WebDriverButton;
import com.redhat.darcy.webdriver.elements.WebDriverElement;
import com.redhat.darcy.webdriver.elements.WebDriverLink;
import com.redhat.darcy.webdriver.elements.WebDriverSelect;
import com.redhat.darcy.webdriver.elements.WebDriverSelectOption;
import com.redhat.darcy.webdriver.elements.WebDriverTextInput;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebElement;

public class WebDriverElementFactoryMap implements ElementFactoryMap {
    private final Map<Class<? extends Element>, ElementFactory<? extends Element>> classMap = 
            new HashMap<>();
    
    public static ElementFactoryMap defaultElementFactoryMap() {
        ElementFactoryMap map = new WebDriverElementFactoryMap();
        
        map.registerFactory(TextInput.class, WebDriverTextInput::new);
        map.registerFactory(Button.class, WebDriverButton::new);
        map.registerFactory(Link.class, WebDriverLink::new);
        map.registerFactory(Select.class, WebDriverSelect::new);
        map.registerFactory(SelectOption.class, WebDriverSelectOption::new);
        map.registerFactory(Element.class, WebDriverElement::new);
        
        return map;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Element> T getElement(Class<T> type, WebElement source) {
        ElementFactory<? extends Element> factory = classMap.get(type);
        
        if (factory == null) {
            throw new DarcyException("No element factory registered for " + type);
        }
        
        return (T) factory.newElement(source, this);
    }
    
    public <T extends Element> void registerFactory(Class<T> elementType, 
            ElementFactory<T> factory) {
        classMap.put(elementType, factory);
    }
}
