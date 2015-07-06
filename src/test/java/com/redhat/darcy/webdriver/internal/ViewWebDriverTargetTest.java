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
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.ParentContext;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.internal.FindsById;
import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.webdriver.testing.doubles.NeverLoadedView;
import com.redhat.darcy.webdriver.testing.doubles.ViewLoadedInTarget;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;
import java.util.HashSet;

@RunWith(JUnit4.class)
public class ViewWebDriverTargetTest {

    private TestBrowser browserWithView = mock(TestBrowser.class);
    private TestBrowser browserWithoutView = mock(TestBrowser.class);
    private TargetedWebDriver driverWithView = mock(TargetedWebDriver.class);
    private TargetedWebDriver driverWithoutView = mock(TargetedWebDriver.class);
    private TargetedTargetLocator locator = mock(TargetedTargetLocator.class);
    private TestContext context = mock(TestContext.class);

    @Before
    public void stubMocks() {
        when(driverWithoutView.getWindowHandles())
                .thenReturn(new HashSet<String>(Arrays.asList("foo", "bar", "baz", "hasView")));
        when(driverWithView.getWindowHandles())
                .thenReturn(new HashSet<String>(Arrays.asList("foo", "bar", "baz", "hasView")));

        when(driverWithoutView.switchTo()).thenReturn(locator);
        when(driverWithView.switchTo()).thenReturn(locator);
        when(driverWithoutView.getWebDriverTarget()).thenReturn(WebDriverTargets.window("foo"));
        when(driverWithView.getWebDriverTarget()).thenReturn(WebDriverTargets.window("hasView"));

        when(locator.defaultContent()).thenReturn(driverWithoutView);
        when(locator.window(anyString())).thenReturn(driverWithoutView);
        when(locator.window("hasView")).thenReturn(driverWithView);

        when(browserWithView.getWrappedDriver()).thenReturn(driverWithView);
        when(browserWithoutView.getWrappedDriver()).thenReturn(driverWithoutView);

        when(context.findById(any(Class.class), anyString())).thenReturn(browserWithoutView);
        when(context.findById(Browser.class, "hasView")).thenReturn(browserWithView);
    }

    @Test
    public void shouldTargetAWindowInWhichViewIsLoaded() {
        View view = new ViewLoadedInTarget(WebDriverTargets.window("hasView"));

        WebDriverTarget target = WebDriverTargets.withViewLoaded(view, context);

        WebDriver result = target.switchTo(locator);

        assertEquals(driverWithView, result);
    }

    @Test
    public void shouldCacheWindowHandleOnceFound() {
        ControllableView view = new ControllableView();
        view.setIsLoaded(true);

        WebDriverTarget target = WebDriverTargets.withViewLoaded(view, context);

        WebDriver firstSwitch = target.switchTo(locator);

        view.setIsLoaded(false);

        try {
            WebDriver secondSwitch = target.switchTo(locator);

            assertEquals(firstSwitch, secondSwitch);
        } catch (NotFoundException e) {
            fail("Second switch tried to find window again:\n" + e);
        }
    }


    @Test(expected = NoSuchWindowException.class)
    public void shouldThrowNoSuchWindowExceptionIfViewIsNotLoadedInAnyWindow() {
        View view = new NeverLoadedView();

        WebDriverTarget target = WebDriverTargets.withViewLoaded(view, context);

        target.switchTo(locator);
    }

    interface TestContext extends ParentContext, FindsById {
    }

    interface TestBrowser extends Browser, WrapsTargetedDriver {
    }

    private static class ControllableView implements View {
        boolean isLoaded = false;

        @Override
        public void setContext(ElementContext context) {

        }

        @Override
        public ElementContext getContext() {
            return null;
        }

        @Override
        public boolean isLoaded() {
            return isLoaded;
        }

        public void setIsLoaded(boolean isLoaded) {
            this.isLoaded = isLoaded;
        }
    }
}
