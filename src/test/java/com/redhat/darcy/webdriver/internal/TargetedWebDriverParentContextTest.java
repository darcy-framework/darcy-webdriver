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
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.DarcyException;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.web.api.Frame;
import com.redhat.darcy.webdriver.ElementConstructorMap;
import com.redhat.darcy.webdriver.testing.doubles.AlwaysLoadedView;
import com.redhat.darcy.webdriver.testing.doubles.ViewLoadedInDriver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;

import java.util.Arrays;
import java.util.HashSet;

@RunWith(JUnit4.class)
public class TargetedWebDriverParentContextTest {
    private TargetedWebDriver mockTargetedDriver = mock(TargetedWebDriver.class);
    private TargetedTargetLocator mockTargetedLocator = mock(TargetedTargetLocator.class);
    private TargetedWebDriver foundTargetedDriver = mock(TargetedWebDriver.class);

    private TargetedWebDriverParentContext context =
            new TargetedWebDriverParentContext(mockTargetedDriver, mock(ElementConstructorMap.class));

    @Before
    public void stubMocks() {
        when(mockTargetedDriver.getWebDriverTarget()).thenReturn(WebDriverTargets.window("self"));
        when(mockTargetedDriver.switchTo()).thenReturn(mockTargetedLocator);
        when(mockTargetedLocator.defaultContent()).thenReturn(mockTargetedDriver);
    }

    @Test
    public void shouldFindBrowsersById() {
        when(mockTargetedLocator.window("test")).thenReturn(foundTargetedDriver);

        Browser browser = context.findById(Browser.class, "test");

        WebDriver driver = ((WrapsDriver) browser).getWrappedDriver();
        assertThat(driver, sameInstance(foundTargetedDriver));
    }

    @Test
    public void shouldByFramesById() {
        when(mockTargetedLocator.frame("test")).thenReturn(foundTargetedDriver);

        Frame frame = context.findById(Frame.class, "test");

        WebDriver driver = ((WrapsDriver) frame).getWrappedDriver();
        assertThat(driver, sameInstance(foundTargetedDriver));
    }

    @Test
    public void shouldFindBrowsersByView() {
        TargetedWebDriver hasView = mock(TargetedWebDriver.class);
        TargetedWebDriver doesNotHaveView = mock(TargetedWebDriver.class);

        View view = new ViewLoadedInDriver(hasView);

        when(mockTargetedDriver.getWindowHandles())
                .thenReturn(new HashSet<String>(Arrays.asList("hasView", "doesNotHaveView")));
        when(mockTargetedLocator.window("hasView")).thenReturn(hasView);
        when(mockTargetedLocator.window("doesNotHaveView")).thenReturn(doesNotHaveView);

        Browser browser = context.findByView(Browser.class, view);

        WebDriver driver = ((WrapsDriver) browser).getWrappedDriver();
        assertThat(driver, sameInstance(hasView));
    }

    @Test(expected = DarcyException.class)
    public void shouldNotCreateTargetedDriversForFramesByView() {
        context.findByView(Frame.class, new AlwaysLoadedView());
    }

}
