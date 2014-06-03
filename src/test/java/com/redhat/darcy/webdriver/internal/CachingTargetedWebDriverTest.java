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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.openqa.selenium.WebDriver.TargetLocator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@RunWith(JUnit4.class)
public class CachingTargetedWebDriverTest {
    @Test
    public void shouldKnowAssignedTarget() {
        TargetedDriverFactory targetedDriverFactory = new CachingTargetedWebDriverFactory(
                mock(WebDriver.class), WebDriverTargets.window("test"));

        TargetedWebDriver targetedWebDriver =
                targetedDriverFactory.getTargetedDriver(WebDriverTargets.window("test2"));

        assertEquals(WebDriverTargets.window("test2"), targetedWebDriver.getWebDriverTarget());
    }

    @Test
    public void shouldSwitchUntargetedDriverBeforeForwardingCall() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);

        TargetedDriverFactory targetedDriverFactory = new CachingTargetedWebDriverFactory(
                mockedDriver, WebDriverTargets.window("test"));

        TargetedWebDriver targetedWebDriver =
                targetedDriverFactory.getTargetedDriver(WebDriverTargets.window("test2"));

        targetedWebDriver.get("url");

        verify(mockedDriver).switchTo();
        verify(mockedTargetLocator).window("test2");
        verify(mockedDriver).get("url");
    }

    @Test
    public void shouldNotSwitchUntargetedDriverIfNotNecessary() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);

        TargetedDriverFactory targetedDriverFactory = new CachingTargetedWebDriverFactory(
                mockedDriver, WebDriverTargets.window("test"));

        TargetedWebDriver targetedWebDriver =
                targetedDriverFactory.getTargetedDriver(WebDriverTargets.window("test"));

        targetedWebDriver.get("url");

        verifyZeroInteractions(mockedTargetLocator);
        verify(mockedDriver).get("url");
    }

    @Test
    public void shouldCreateTargetedWebElementsThatSwitchUntargetedDriverBeforeForwardingCall() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);

        TargetedDriverFactory targetedDriverFactory = new CachingTargetedWebDriverFactory(
                mockedDriver, WebDriverTargets.window("test"));

        WebElement mockedElement = mock(WebElement.class);

        WebElement targetedWebElement = targetedDriverFactory
                .getTargetedDriver(WebDriverTargets.window("test2"))
                .createTargetedElement(mockedElement);

        targetedWebElement.getText();

        verify(mockedDriver).switchTo();
        verify(mockedTargetLocator).window("test2");
        verify(mockedElement).getText();
    }

    @Test
    public void shouldCreateTargetedWebElementsThatDoNotSwitchUntargetedDriverIfNotNecessary() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);

        TargetedDriverFactory targetedDriverFactory = new CachingTargetedWebDriverFactory(
                mockedDriver, WebDriverTargets.window("test"));

        WebElement mockedElement = mock(WebElement.class);

        WebElement targetedWebElement = targetedDriverFactory
                .getTargetedDriver(WebDriverTargets.window("test"))
                .createTargetedElement(mockedElement);

        targetedWebElement.getText();

        verifyZeroInteractions(mockedTargetLocator);
        verify(mockedElement).getText();
    }
}
