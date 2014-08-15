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

import com.redhat.darcy.ui.DarcyException;
import com.redhat.darcy.ui.api.elements.Button;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.api.elements.Label;
import com.redhat.darcy.ui.api.elements.Link;
import com.redhat.darcy.ui.api.elements.Select;
import com.redhat.darcy.ui.api.elements.SelectOption;
import com.redhat.darcy.ui.api.elements.TextInput;
import com.redhat.darcy.web.api.elements.HtmlButton;
import com.redhat.darcy.web.api.elements.HtmlElement;
import com.redhat.darcy.web.api.elements.HtmlTextInput;
import com.redhat.darcy.webdriver.elements.WebDriverButton;
import com.redhat.darcy.webdriver.elements.WebDriverElement;
import com.redhat.darcy.webdriver.elements.WebDriverLabel;
import com.redhat.darcy.webdriver.elements.WebDriverLink;
import com.redhat.darcy.webdriver.elements.WebDriverSelect;
import com.redhat.darcy.webdriver.elements.WebDriverSelectOption;
import com.redhat.darcy.webdriver.elements.WebDriverTextInput;

import java.util.HashMap;
import java.util.Map;

/**
 * A map of element interfaces to implementations.
 */
public class ElementConstructorMap {
    // Every key MUST map to a value that constructs an implementation of THAT KEY
    private final Map<Class<?>, ElementConstructor<? extends Element>> classMap =
            new HashMap<>();

    /**
     * Defaults to standard, WebDriver compatible element implementations. If a particular browser's
     * driver has some quirk, it is encouraged that that browser factory override a relevant element
     * type with it's own implementation. When overriding element types, <strong>favor overriding
     * the most specific type (i.e. HtmlTextInput instead of TextInput).</strong> By default,
     * less specific element types will simply point to the more specific version. That is, if a
     * client asks for a TextInput, the default map will look up whatever implementation is
     * registered for HtmlTextInput. If the HtmlTextInput implementation is updated, TextInput will
     * consume that updated implementation. This way, a browser factory need not override every
     * interface that may point to the
     */
    public static ElementConstructorMap newElementConstructorMapWithDefaults() {
        ElementConstructorMap map = new ElementConstructorMap();

        map.put(HtmlTextInput.class, WebDriverTextInput::new);
        map.put(HtmlButton.class, WebDriverButton::new);
        map.put(Link.class, WebDriverLink::new);
        map.put(Select.class, WebDriverSelect::new);
        map.put(SelectOption.class, WebDriverSelectOption::new);
        map.put(Label.class, WebDriverLabel::new);
        map.put(HtmlElement.class, WebDriverElement::new);

        map.point(Element.class, HtmlElement.class);
        map.point(TextInput.class, HtmlTextInput.class);
        map.point(Button.class, HtmlButton.class);
        
        return map;
    }
    
    @SuppressWarnings("unchecked")
    public <E extends Element> ElementConstructor<E> get(Class<E> type) {
        ElementConstructor<? extends Element> factory = classMap.get(type);
        
        if (factory == null) {
            throw new DarcyException("No element factory registered for " + type);
        }
        
        return (ElementConstructor<E>) factory;
    }
    
    /**
     * 
     * @param elementType
     * @param constructor
     * @return
     */
    @SuppressWarnings("unchecked")
    public <E extends Element> ElementConstructor<E> put(
            Class<? super E> elementType, ElementConstructor<E> constructor) {
        return (ElementConstructor<E>) classMap.put(elementType, constructor);
    }

    /**
     * Often, one implementation satisfies a number of interfaces. Instead of having to register
     * that same implementation for each interface, use {@code point} to register an interface
     * <em>to another interface</em>. When the {@code from} interface is looked up in
     * {@link #get(Class)}, the constructor returned will invoke the same constructor as the one
     * registered for {@code to}, even if the {@code to} constructor is updated after the point
     * is registered.
     *
     * @param from The more-generic type which should lookup some more specific type to use as an
     * implementation.
     * @param to The specific type who's registered constructor will be used for {@code from}'s
     * lookup instead. This must be a subclass of {@code from} to prevent clients from pointing to
     * a type that does not actually implement the {@code from} interface.
     */
    public <E extends Element> void point(Class<? super E> from, Class<E> to) {
        ElementConstructor<E> eCtor = (e, m) -> get(to).newElement(e, m);
        put(from, eCtor);
    }
}
