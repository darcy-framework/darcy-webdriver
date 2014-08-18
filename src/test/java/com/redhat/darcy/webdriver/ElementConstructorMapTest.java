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

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.redhat.darcy.ui.DarcyException;
import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.elements.Button;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.webdriver.elements.WebDriverButton;
import com.redhat.darcy.webdriver.internal.ElementLookup;
import com.redhat.darcy.webdriver.testing.rules.TraceTestName;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ElementConstructorMapTest {
    @Rule
    public TraceTestName traceTestName = new TraceTestName();

    @Test
    public void shouldRegisterElementConstructorsByElementType() {
        ElementConstructorMap elementConstructorMap = new ElementConstructorMap();

        ElementConstructor buttonConstructor = WebDriverButton::new;
        elementConstructorMap.put(Button.class, buttonConstructor);

        assertSame(buttonConstructor, elementConstructorMap.get(Button.class));
    }

    @Test(expected = DarcyException.class)
    public void shouldThrowDarcyExceptionIfNoElementConstructorIsRegisteredForAGivenType() {
        ElementConstructorMap elementConstructorMap = new ElementConstructorMap();
        elementConstructorMap.get(Button.class);
    }

    @Test
    public void shouldAllowPointingOneGenericElementTypeToAMoreSpecificType() {
        ElementConstructorMap map = new ElementConstructorMap();
        ElementConstructor<Button> buttonConstructor = mock(ButtonConstructor.class);

        map.put(Button.class, buttonConstructor);
        map.point(Element.class, Button.class);

        map.get(Element.class).newElement(null, null);

        verify(buttonConstructor).newElement(any(ElementLookup.class), any(ElementContext.class));
    }

    @Test
    public void shouldPointToSpecificTypeConstructorEvenIfUpdatedAfterPointing() {
        ElementConstructorMap map = new ElementConstructorMap();
        ElementConstructor<Button> buttonConstructor = mock(ButtonConstructor.class);
        ElementConstructor<Button> updatedButtonConstructor = mock(ButtonConstructor.class);

        map.put(Button.class, buttonConstructor);
        map.point(Element.class, Button.class);
        map.put(Button.class, updatedButtonConstructor);

        map.get(Element.class).newElement(null, null);

        verify(updatedButtonConstructor).newElement(null, null);
        verify(buttonConstructor, never()).newElement(any(ElementLookup.class), any(ElementContext.class));
    }

    interface ButtonConstructor extends ElementConstructor<Button> {}
}
