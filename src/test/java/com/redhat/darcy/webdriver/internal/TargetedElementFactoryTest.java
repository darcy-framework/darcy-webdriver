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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.By;
import com.redhat.darcy.ui.ElementContext;
import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.webdriver.ElementConstructorMap;
import com.redhat.darcy.webdriver.elements.WebDriverElement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.WrapsElement;

import java.util.Collections;

@RunWith(JUnit4.class)
public class TargetedElementFactoryTest {
    private TargetedWebDriver mockedTargetedWebDriver = mock(TargetedWebDriver.class);
    private ElementConstructorMap mockedElementCtorMap = mock(ElementConstructorMap.class);
    private TargetedElementFactory elementFactory = new TargetedElementFactory(
            mockedTargetedWebDriver, mockedElementCtorMap);

    @Before
    public void setup() {
        when(mockedElementCtorMap.get(Element.class)).thenReturn(ElementImpl::new);

        // Just return the same element back
        when(mockedTargetedWebDriver.createTargetedWebElement(any(WebElement.class)))
                .then(new Answer<WebElement>() {
                    @Override
                    public WebElement answer(InvocationOnMock invocation) throws Throwable {
                        return (WebElement) invocation.getArguments()[0];
                    }
                });
    }

    @Test
    public void shouldCreateElementsWithTargetedSourceWebElements() {
        WebElement mockedWebElement = mock(WebElement.class);
        TargetedWebDriver mockedTargetedWebDriver = mock(TargetedWebDriver.class);

        when(mockedTargetedWebDriver.createTargetedWebElement(any(WebElement.class)))
                .thenReturn(mockedWebElement);

        TargetedElementFactory elementFactory = new TargetedElementFactory(
                mockedTargetedWebDriver, mockedElementCtorMap);

        Element element = elementFactory.newElement(Element.class, mock(WebElement.class));

        assertSame(mockedWebElement, ((WrapsElement) element).getWrappedElement());
    }

    @Test
    public void shouldCreateElementsUsingElementConstructorMap() {
        Element element = elementFactory.newElement(Element.class, mock(WebElement.class));

        assertThat(element, instanceOf(ElementImpl.class));
    }

    @Test
    public void shouldCreateElementsWhosElementContextSearchesWithinThatElement() {
        FindsByIdWebElement mockedParentElement = mock(FindsByIdWebElement.class);
        WebElement mockedChildElement = mock(WebElement.class);

        when(mockedParentElement.findElementById("test")).thenReturn(mockedChildElement);
        when(mockedParentElement.findElementsById("test"))
                .thenReturn(Collections.singletonList(mockedChildElement));
        when(mockedChildElement.isDisplayed()).thenReturn(true);

        Element element = elementFactory.newElement(Element.class, mockedParentElement);

        Element childElement = ((WebDriverElement) element).getElementContext().find()
                .element(By.id("test"));

        assertTrue(childElement.isDisplayed());
    }

    class ElementImpl extends WebDriverElement {
        ElementImpl(WebElement source, WebDriver driver, ElementContext context) {
            super(source, driver, context);
        }
    }

    interface FindsByIdWebElement extends WebElement, FindsById {}
}
