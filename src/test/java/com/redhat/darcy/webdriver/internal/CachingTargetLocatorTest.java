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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
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
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;

@RunWith(JUnit4.class)
public class CachingTargetLocatorTest {
    @Rule
    public TraceTestName traceTestName = new TraceTestName();

    @Test
    public void shouldKnowInitialTarget() {
        CachingTargetLocator targetLocator = new CachingTargetLocator(
                WebDriverTargets.window("test"),
                mock(WebDriver.class));

        assertEquals(WebDriverTargets.window("test"), targetLocator.getCurrentTarget());
    }

    @Test
    public void shouldKeepTrackOfChangedTarget() {
        WebDriver mockedDriver = mock(WebDriver.class, Mockito.RETURNS_MOCKS);

        CachingTargetLocator targetLocator = new CachingTargetLocator(
                WebDriverTargets.window("test1"),
                mockedDriver);

        targetLocator.window("test2");

        assertEquals(WebDriverTargets.window("test2"), targetLocator.getCurrentTarget());
    }

    @Test
    public void shouldNotSwitchDriverIfSameWindowIsSwitchedTo() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);

        TargetLocator targetLocator = new CachingTargetLocator(WebDriverTargets.window("test"),
                mockedDriver);

        targetLocator.window("test");

        verifyZeroInteractions(mockedDriver);
        verifyZeroInteractions(mockedTargetLocator);
    }

    @Test
    public void shouldSwitchDriverIfDifferentWindowIsSwitchedTo() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);

        TargetLocator targetLocator = new CachingTargetLocator(WebDriverTargets.window("test1"),
                mockedDriver);

        targetLocator.window("test2");

        verify(mockedDriver).switchTo();
        verify(mockedTargetLocator).window("test2");
    }

    @Test
    public void shouldNotSwitchDriverIfSameFrameIsSwitchedTo() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);

        CachingTargetLocator targetLocator = new CachingTargetLocator(
                WebDriverTargets.frame(WebDriverTargets.window("testwindow"), "testframe"),
                mockedDriver);

        targetLocator.frame(WebDriverTargets.window("testwindow"), "testframe");

        verifyZeroInteractions(mockedDriver);
        verifyZeroInteractions(mockedTargetLocator);
    }

    @Test
    public void shouldSwitchDriverIfFrameOfSameNameButDifferentParentWindowIsSwitchedTo() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);

        // Stub locator in case implementation uses the returned driver
        when(mockedTargetLocator.window(anyString())).thenReturn(mockedDriver);

        CachingTargetLocator targetLocator = new CachingTargetLocator(
                WebDriverTargets.frame(WebDriverTargets.window("testwindow1"), "testframe"),
                mockedDriver);

        targetLocator.frame(WebDriverTargets.window("testwindow2"), "testframe");

        verify(mockedTargetLocator).window("testwindow2");
        verify(mockedTargetLocator).frame("testframe");
    }

    @Test
    public void shouldSwitchToFrameWithinCurrentTargetIfNoParentIsGiven() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);

        // Stub locator in case implementation uses the returned driver
        when(mockedTargetLocator.window("testwindow2")).thenReturn(mockedDriver);

        CachingTargetLocator targetLocator = new CachingTargetLocator(
                WebDriverTargets.frame(WebDriverTargets.window("window"), "outterframe"),
                mockedDriver);

        targetLocator.frame("innerframe");

        verify(mockedTargetLocator).frame("innerframe");

        assertEquals(WebDriverTargets.frame(
                        WebDriverTargets.frame(
                                WebDriverTargets.window("window"),
                                "outterframe"),
                        "innerframe"),
                targetLocator.getCurrentTarget());
    }

    @Test
    public void shouldSwitchDriverToAlertIfCurrentTargetIsNotAlert() {
        WebDriver mockDriver = mock(WebDriver.class);
        TargetLocator mockLocator = mock(TargetLocator.class);

        when(mockDriver.switchTo()).thenReturn(mockLocator);

        CachingTargetLocator targetLocator = new CachingTargetLocator(
                WebDriverTargets.window("test"), mockDriver);
        targetLocator.alert();

        verify(mockLocator).alert();
    }

    @Test
    public void shouldNotSwitchDriverToAlertIfCurrentTargetIsAlert() {
        WebDriver mockDriver = mock(WebDriver.class);
        TargetLocator mockLocator = mock(TargetLocator.class);

        when(mockDriver.switchTo()).thenReturn(mockLocator);
        when(mockLocator.alert()).thenReturn(mock(Alert.class));

        CachingTargetLocator targetLocator = new CachingTargetLocator(
                WebDriverTargets.window("test"), mockDriver);
        targetLocator.alert();
        targetLocator.alert();

        verify(mockLocator, times(1)).alert();
    }

    @Test
    public void shouldSwitchDriverToAlertIfCurrentTargetIsNoLongerAlertAndIsSameAsOriginalTarget() {
        WebDriver mockDriver = mock(WebDriver.class);
        TargetLocator mockLocator = mock(TargetLocator.class);

        when(mockDriver.switchTo()).thenReturn(mockLocator);
        when(mockLocator.alert()).thenReturn(mock(Alert.class));

        CachingTargetLocator targetLocator = new CachingTargetLocator(
                WebDriverTargets.window("test"), mockDriver);
        targetLocator.alert();
        targetLocator.window("test");
        targetLocator.alert();

        verify(mockLocator, times(2)).alert();
    }

    @Test
    public void shouldSwitchDriverToAlertIfCurrentTargetIsNoLongerAlertAndIsDifferentThanOriginalTarget() {
        WebDriver mockDriver = mock(WebDriver.class);
        TargetLocator mockLocator = mock(TargetLocator.class);

        when(mockDriver.switchTo()).thenReturn(mockLocator);
        when(mockLocator.alert()).thenReturn(mock(Alert.class));

        CachingTargetLocator targetLocator = new CachingTargetLocator(
                WebDriverTargets.window("test"), mockDriver);
        targetLocator.alert();
        targetLocator.window("different");
        targetLocator.alert();

        verify(mockLocator, times(2)).alert();
    }
}
