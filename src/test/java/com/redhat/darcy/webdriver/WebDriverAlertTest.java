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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.openqa.selenium.WebDriver.TargetLocator;

import com.redhat.darcy.webdriver.testing.rules.TraceTestName;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

@RunWith(JUnit4.class)
public class WebDriverAlertTest {
    @Rule
    public TraceTestName traceTestName = new TraceTestName();

    @Test
    public void shouldReturnFalseForIsPresentIfDriverCannotSwitchToAlert() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);
        when(mockedTargetLocator.alert())
                .thenThrow(new NoAlertPresentException("No alert present."));

        WebDriverAlert alert = new WebDriverAlert(mockedDriver);

        assertFalse(alert.isPresent());
    }

    @Test
    public void shouldReturnTrueForIsPresentIfDriverCanSwitchToAlert() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);
        when(mockedTargetLocator.alert()).thenReturn(mock(Alert.class));

        WebDriverAlert alert = new WebDriverAlert(mockedDriver);

        assertTrue(alert.isPresent());
    }

    @Test
    public void shouldAcceptAlert() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);
        Alert mockedAlert = mock(Alert.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);
        when(mockedTargetLocator.alert()).thenReturn(mockedAlert);

        WebDriverAlert alert = new WebDriverAlert(mockedDriver);
        alert.accept();

        verify(mockedAlert).accept();
    }

    @Test
    public void shouldDismissAlert() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);
        Alert mockedAlert = mock(Alert.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);
        when(mockedTargetLocator.alert()).thenReturn(mockedAlert);

        WebDriverAlert alert = new WebDriverAlert(mockedDriver);
        alert.dismiss();

        verify(mockedAlert).dismiss();
    }

    @Test
    public void shouldSendKeysToAlert() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);
        Alert mockedAlert = mock(Alert.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);
        when(mockedTargetLocator.alert()).thenReturn(mockedAlert);

        WebDriverAlert alert = new WebDriverAlert(mockedDriver);
        alert.sendKeys("sent keys");

        verify(mockedAlert).sendKeys("sent keys");
    }

    @Test
    public void shouldGetTextFromAlert() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class);
        Alert mockedAlert = mock(Alert.class);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);
        when(mockedTargetLocator.alert()).thenReturn(mockedAlert);

        WebDriverAlert alert = new WebDriverAlert(mockedDriver);
        alert.getText();

        verify(mockedAlert).getText();
    }

    @Test
    public void shouldCacheAlertReference() {
        WebDriver mockedDriver = mock(WebDriver.class);
        TargetLocator mockedTargetLocator = mock(TargetLocator.class, Mockito.RETURNS_MOCKS);

        when(mockedDriver.switchTo()).thenReturn(mockedTargetLocator);

        WebDriverAlert alert = new WebDriverAlert(mockedDriver);
        alert.getText();
        alert.accept();

        // Fails if called more than once
        verify(mockedDriver).switchTo();
        verify(mockedTargetLocator).alert();
    }
}
