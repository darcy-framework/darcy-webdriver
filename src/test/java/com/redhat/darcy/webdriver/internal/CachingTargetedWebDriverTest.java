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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.openqa.selenium.WebDriver.TargetLocator;

import com.redhat.darcy.webdriver.testing.rules.TraceTestName;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@RunWith(JUnit4.class)
public class CachingTargetedWebDriverTest {
    @Rule
    public TraceTestName traceTestName = new TraceTestName();

    @Test
    public void shouldKnowAssignedTarget() {
        TargetedWebDriverFactory targetedDriverFactory = new CachingTargetedWebWebDriverFactory(
                mock(WebDriver.class), WebDriverTargets.window("test"));

        TargetedWebDriver targetedWebDriver =
                targetedDriverFactory.getTargetedWebDriver(WebDriverTargets.window("test2"));

        assertEquals(WebDriverTargets.window("test2"), targetedWebDriver.getWebDriverTarget());
    }

    @Test
    public void shouldSwitchUntargetedDriverBeforeForwardingCall() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);

        TargetedWebDriverFactory targetedDriverFactory = new CachingTargetedWebWebDriverFactory(
                mockedDriver, WebDriverTargets.window("test"));

        TargetedWebDriver targetedWebDriver =
                targetedDriverFactory.getTargetedWebDriver(WebDriverTargets.window("test2"));

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

        TargetedWebDriverFactory targetedDriverFactory = new CachingTargetedWebWebDriverFactory(
                mockedDriver, WebDriverTargets.window("test"));

        TargetedWebDriver targetedWebDriver =
                targetedDriverFactory.getTargetedWebDriver(WebDriverTargets.window("test"));

        targetedWebDriver.get("url");

        verifyZeroInteractions(mockedTargetLocator);
        verify(mockedDriver).get("url");
    }

    @Test
    public void shouldCreateTargetedWebElementsThatSwitchUntargetedDriverBeforeForwardingCall() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);

        TargetedWebDriverFactory targetedDriverFactory = new CachingTargetedWebWebDriverFactory(
                mockedDriver, WebDriverTargets.window("test"));

        WebElement mockedElement = mock(WebElement.class);

        WebElement targetedWebElement = targetedDriverFactory
                .getTargetedWebDriver(WebDriverTargets.window("test2"))
                .createTargetedWebElement(mockedElement);

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

        TargetedWebDriverFactory targetedDriverFactory = new CachingTargetedWebWebDriverFactory(
                mockedDriver, WebDriverTargets.window("test"));

        WebElement mockedElement = mock(WebElement.class);

        WebElement targetedWebElement = targetedDriverFactory
                .getTargetedWebDriver(WebDriverTargets.window("test"))
                .createTargetedWebElement(mockedElement);

        targetedWebElement.getText();

        verifyZeroInteractions(mockedTargetLocator);
        verify(mockedElement).getText();
    }

    @Test
    public void shouldCreateTargetedWebElementsThatReturnFalseForIsDisplayedIfCannotSwitchDriver() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);
        when(mockedTargetLocator.window(anyString()))
                .thenThrow(new NoSuchWindowException("No such window"));

        TargetedWebDriverFactory targetedDriverFactory = new CachingTargetedWebWebDriverFactory(
                mockedDriver, WebDriverTargets.window("test"));

        WebElement mockedElement = mock(WebElement.class);

        WebElement targetedWebElement = targetedDriverFactory
                .getTargetedWebDriver(WebDriverTargets.window("not-present"))
                .createTargetedWebElement(mockedElement);

        assertFalse(targetedWebElement.isDisplayed());
    }

    @Test
    public void shouldReturnFalseForIsPresentIfCannotSwitchToTargetWindow() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);
        when(mockedTargetLocator.window(anyString()))
                .thenThrow(new NoSuchWindowException("No such window"));

        TargetedWebDriverFactory targetedDriverFactory = new CachingTargetedWebWebDriverFactory(
                mockedDriver, WebDriverTargets.window("test"));

        TargetedWebDriver targetedDriver = targetedDriverFactory
                .getTargetedWebDriver(WebDriverTargets.window("not-present"));

        assertFalse(targetedDriver.isPresent());
    }

    @Test
    public void shouldReturnFalseForIsPresentIfCannotSwitchToTargetFrame() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);
        when(mockedTargetLocator.frame("not-present"))
                .thenThrow(new NoSuchFrameException("No such window"));

        TargetedWebDriverFactory targetedDriverFactory = new CachingTargetedWebWebDriverFactory(
                mockedDriver, WebDriverTargets.window("test"));

        TargetedWebDriver targetedDriver = targetedDriverFactory.getTargetedWebDriver(
                WebDriverTargets.frame(WebDriverTargets.window("test"), "not-present"));

        assertFalse(targetedDriver.isPresent());
    }

    @Test
    public void shouldReturnTrueForIsPresentIfCanSwitchToTargetWindow() {
        WebDriver mockedDriver = mock(WebDriver.class, Mockito.RETURNS_MOCKS);

        TargetedWebDriverFactory targetedDriverFactory = new CachingTargetedWebWebDriverFactory(
                mockedDriver, WebDriverTargets.window("test"));

        TargetedWebDriver targetedDriver = targetedDriverFactory
                .getTargetedWebDriver(WebDriverTargets.window("present"));

        assertTrue(targetedDriver.isPresent());
    }

    @Test
    public void shouldReturnTrueForIsPresentIfCanSwitchToTargetFrame() {
        WebDriver mockedDriver = mock(WebDriver.class, Mockito.RETURNS_MOCKS);

        TargetedWebDriverFactory targetedDriverFactory = new CachingTargetedWebWebDriverFactory(
                mockedDriver, WebDriverTargets.window("test"));

        TargetedWebDriver targetedDriver = targetedDriverFactory.getTargetedWebDriver(
                WebDriverTargets.frame(WebDriverTargets.window("test"), "present"));

        assertTrue(targetedDriver.isPresent());
    }
}
