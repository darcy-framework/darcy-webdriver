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

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.redhat.darcy.web.By;
import com.redhat.darcy.webdriver.elements.WebDriverElement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InOrder;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByClassName;

@RunWith(JUnit4.class)
public class NestedElementLookupTest {
    @Test
    public void shouldLookupNestedElements() {
        WebDriverElement mockParentElement = mock(WebDriverElement.class);
        MockWebElement mockParentWebElement = mock(MockWebElement.class);
        WebElement mockChildWebElement = mock(WebElement.class);

        when(mockParentElement.getWrappedElement()).thenReturn(mockParentWebElement);
        when(mockParentWebElement.findElementByClassName("test")).thenReturn(mockChildWebElement);

        ElementLookup lookup = new NestedElementLookup(mockParentElement, By.className("test"));
        WebElement child = lookup.lookup();

        assertSame(mockChildWebElement, child);
    }

    @Test
    public void shouldInvalidateCacheOfParentIfParentIsStale() {
        WebDriverElement mockParentElement = mock(WebDriverElement.class);
        MockWebElement staleParentWebElement = mock(MockWebElement.class);
        MockWebElement freshParentWebElement = mock(MockWebElement.class);
        WebElement mockChildWebElement = mock(WebElement.class);

        when(mockParentElement.getWrappedElement())
                .thenReturn(staleParentWebElement)
                .thenReturn(freshParentWebElement);

        when(staleParentWebElement.findElementByClassName("test"))
                .thenThrow(new StaleElementReferenceException(""));

        when(freshParentWebElement.findElementByClassName("test"))
                .thenReturn(mockChildWebElement);

        ElementLookup lookup = new NestedElementLookup(mockParentElement, By.className("test"));
        WebElement child = lookup.lookup();

        InOrder inOrder = inOrder(mockParentElement);
        inOrder.verify(mockParentElement).getWrappedElement();
        inOrder.verify(mockParentElement).invalidateCache();
        inOrder.verify(mockParentElement).getWrappedElement();

        assertSame(mockChildWebElement, child);
    }

    private interface MockWebElement extends WebElement, FindsByClassName {};
}
