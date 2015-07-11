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

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;

import java.util.Arrays;
import java.util.HashSet;

@RunWith(JUnit4.class)
public class WindowTitleWebDriverTargetTest {

    private final WebDriver driver = mock(WebDriver.class);
    private final WebDriver fooWindow = mock(WebDriver.class);
    private final WebDriver barWindow = mock(WebDriver.class);
    private final TargetLocator locator = mock(TargetLocator.class);

    @Before
    public void stubMocks() {
        when(driver.getWindowHandles()).thenReturn(new HashSet<>(asList("fooWindow", "barWindow")));

        when(fooWindow.getTitle()).thenReturn("foo");
        when(barWindow.getTitle()).thenReturn("bar");

        when(locator.defaultContent()).thenReturn(driver);
        when(locator.window("fooWindow")).thenReturn(fooWindow);
        when(locator.window("barWindow")).thenReturn(barWindow);
    }

    @Test
    public void shouldTargetAWindowWhichExactlyMatchesTitle() {
        WebDriverTarget target = WebDriverTargets.windowByTitle("foo");
        WebDriver found = target.switchTo(locator);

        assertThat(found.getTitle(), is("foo"));
    }

    @Test
    public void shouldKeepFindingTheSameWindowEvenIfItsTitleLaterChanges() {
        WebDriverTarget target = WebDriverTargets.windowByTitle("foo");

        WebDriver found = target.switchTo(locator);
        assertThat(found.getTitle(), is("foo"));

        when(fooWindow.getTitle()).thenReturn("no longer foo");

        WebDriver foundAgain = target.switchTo(locator);
        assertThat(foundAgain.getTitle(), is("no longer foo"));
    }

    @Test(expected = NoSuchWindowException.class)
    public void shouldThrowNoSuchWindowExceptionIfNoWindowsCanBeFoundWithTitle() {
        WebDriverTarget target = WebDriverTargets.windowByTitle("awesome window");

        target.switchTo(locator);
    }
}
