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

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebElement;

import com.redhat.darcy.ui.elements.Button;
import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.ui.elements.Link;
import com.redhat.darcy.ui.elements.TextInput;
import com.redhat.darcy.webdriver.elements.WebDriverButton;
import com.redhat.darcy.webdriver.elements.WebDriverLink;
import com.redhat.darcy.webdriver.elements.WebDriverTextInput;

public class WebDriverElementFactoryMap {
    private static final Map<Class<? extends Element>, WebDriverElementFactory<? extends Element>> classMap = 
            new HashMap<>();
    
    static {
        registerFactory(TextInput.class, WebDriverTextInput::new);
        registerFactory(Button.class, WebDriverButton::new);
        registerFactory(Link.class, WebDriverLink::new);
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends Element> T get(Class<T> type, WebElement source) {
        WebDriverElementFactory<? extends Element> factory = classMap.get(type);
        
        if (factory == null) {
            throw new RuntimeException("No element factory registered for " + type);
        }
        
        return (T) classMap.get(type).element(source);
    }
    
    public static <T extends Element> void registerFactory(Class<T> elementType, 
            WebDriverElementFactory<T> factory) {
        classMap.put(elementType, factory);
    }
}
