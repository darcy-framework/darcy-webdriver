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

import com.redhat.darcy.webdriver.internal.TargetedAlert;
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
    public void shouldCheckForPresence() {
        TargetedAlert mockAlert = mock(TargetedAlert.class);
        when(mockAlert.isPresent()).thenReturn(true);

        WebDriverAlert alert = new WebDriverAlert(mockAlert);

        assertTrue(alert.isPresent());

        when(mockAlert.isPresent()).thenReturn(false);

        assertFalse(alert.isPresent());
    }

    @Test
    public void shouldAcceptAlert() {
        TargetedAlert mockAlert = mock(TargetedAlert.class);

        WebDriverAlert alert = new WebDriverAlert(mockAlert);
        alert.accept();

        verify(mockAlert).accept();
    }

    @Test
    public void shouldDismissAlert() {
        TargetedAlert mockAlert = mock(TargetedAlert.class);

        WebDriverAlert alert = new WebDriverAlert(mockAlert);
        alert.dismiss();

        verify(mockAlert).dismiss();
    }

    @Test
    public void shouldSendKeysToAlert() {
        TargetedAlert mockAlert = mock(TargetedAlert.class);

        WebDriverAlert alert = new WebDriverAlert(mockAlert);
        alert.sendKeys("sent keys");

        verify(mockAlert).sendKeys("sent keys");
    }

    @Test
    public void shouldGetTextFromAlert() {
        TargetedAlert mockAlert = mock(TargetedAlert.class);

        WebDriverAlert alert = new WebDriverAlert(mockAlert);
        alert.getText();

        verify(mockAlert).getText();
    }
}
