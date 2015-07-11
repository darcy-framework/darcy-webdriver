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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.DarcyException;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.web.api.Frame;
import com.redhat.darcy.webdriver.ElementConstructorMap;
import com.redhat.darcy.webdriver.WebDriverBrowser;
import com.redhat.darcy.webdriver.testing.doubles.AlwaysLoadedView;
import com.redhat.darcy.webdriver.testing.doubles.NeverLoadedView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.internal.WrapsDriver;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(JUnit4.class)
public class TargetedWebDriverParentContextTest {
    private TargetedWebDriver mockTargetedDriver = mock(TargetedWebDriver.class);
    private TargetedTargetLocator mockTargetedLocator = mock(TargetedTargetLocator.class);
    private WebDriverTarget contextTarget = WebDriverTargets.window("self");

    private TargetedWebDriverParentContext context =
            new TargetedWebDriverParentContext(contextTarget, mockTargetedLocator,
                    mock(ElementConstructorMap.class));

    @Before
    public void stubMocks() {
        when(mockTargetedDriver.switchTo()).thenReturn(mockTargetedLocator);
        when(mockTargetedLocator.defaultContent()).thenReturn(mockTargetedDriver);
    }

    @Test
    public void shouldFindBrowsersById() {
        Browser browser = context.findById(Browser.class, "test");

        TargetedWebDriver driver = ((WebDriverBrowser) browser).getWrappedDriver();
        assertThat(driver.getWebDriverTarget(), is(WebDriverTargets.window("test")));
    }

    @Test
    public void shouldByFramesByIdUnderneathCurrentTarget() {
        Frame frame = context.findById(Frame.class, "test");

        TargetedWebDriver driver = ((WebDriverBrowser) frame).getWrappedDriver();
        assertThat(driver.getWebDriverTarget(), is(WebDriverTargets.frame(contextTarget, "test")));
    }

    @Test
    public void shouldFindBrowserByView() {
        View view = new AlwaysLoadedView();

        Browser browser = context.findByView(Browser.class, view);

        TargetedWebDriver driver = ((WebDriverBrowser) browser).getWrappedDriver();
        assertThat(driver.getWebDriverTarget(), is(WebDriverTargets.withViewLoaded(view, context)));
    }

    @Test
    public void shouldFindAllBrowsersByView() {
        TargetedWebDriver hasView = mock(TargetedWebDriver.class);
        TargetedWebDriver alsoHasView = mock(TargetedWebDriver.class);

        View view = new AlwaysLoadedView();

        when(mockTargetedDriver.getWindowHandles())
                .thenReturn(new HashSet<String>(Arrays.asList("hasView", "alsoHasView")));
        when(mockTargetedLocator.window("hasView")).thenReturn(hasView);
        when(mockTargetedLocator.window("alsoHasView")).thenReturn(alsoHasView);

        List<Browser> browser = context.findAllByView(Browser.class, view);

        assertThat(browser.stream()
                .map(b -> (WebDriverBrowser) b)
                .map(WebDriverBrowser::getWrappedDriver)
                .map(TargetedWebDriver::getWebDriverTarget)
                .collect(Collectors.toList()),
                containsInAnyOrder(
                        WebDriverTargets.window("hasView"),
                        WebDriverTargets.window("alsoHasView")));
    }

    @Test
    public void shouldNotFailImmediatelyIfFindingBrowserByViewAndNoneMatch() {
        TargetedWebDriver doesNotHaveView = mock(TargetedWebDriver.class);
        TargetedWebDriver alsoDoesNotHaveView = mock(TargetedWebDriver.class);

        View view = new NeverLoadedView();

        when(mockTargetedDriver.getWindowHandles())
                .thenReturn(new HashSet<String>(Arrays.asList("doesNotHaveView", "alsoDoesNotHaveView")));
        when(mockTargetedLocator.window("doesNotHaveView")).thenReturn(doesNotHaveView);
        when(mockTargetedLocator.window("alsoDoesNotHaveView")).thenReturn(alsoDoesNotHaveView);

        Browser browser = context.findByView(Browser.class, view);

        assertFalse(browser.isPresent());
    }

    @Test
    public void shouldFindBrowsersByTitle() {
        TargetedWebDriver fooWindow = mock(TargetedWebDriver.class);
        TargetedWebDriver barWindow = mock(TargetedWebDriver.class);

        when(fooWindow.getTitle()).thenReturn("foo");
        when(barWindow.getTitle()).thenReturn("bar");

        when(mockTargetedDriver.getWindowHandles())
                .thenReturn(new HashSet<>(Arrays.asList("fooWindow", "barWindow")));
        when(mockTargetedLocator.window("fooWindow")).thenReturn(fooWindow);
        when(mockTargetedLocator.window("barWindow")).thenReturn(barWindow);

        Browser browser = context.findByTitle(Browser.class, "awesome window");

        assertFalse(browser.isPresent());

        when(fooWindow.getTitle()).thenReturn("awesome window");

        assertTrue(browser.isPresent());
    }

    @Test
    public void shouldFindAllBrowsersByTitle() {
        TargetedWebDriver window1 = mock(TargetedWebDriver.class);
        TargetedWebDriver window2 = mock(TargetedWebDriver.class);

        when(window1.getTitle()).thenReturn("shared title");
        when(window2.getTitle()).thenReturn("shared title");

        when(mockTargetedDriver.getWindowHandles())
                .thenReturn(new HashSet<>(Arrays.asList("window1", "window2")));
        when(mockTargetedLocator.window("window1")).thenReturn(window1);
        when(mockTargetedLocator.window("window2")).thenReturn(window2);

        List<Browser> browsers = context.findAllByTitle(Browser.class, "shared title");

        assertThat(browsers.stream()
                .map(b -> (WebDriverBrowser) b)
                .map(WebDriverBrowser::getWrappedDriver)
                .map(TargetedWebDriver::getWebDriverTarget)
                .collect(Collectors.toList()),
                containsInAnyOrder(WebDriverTargets.window("window1"),
                        WebDriverTargets.window("window2")));
    }

    @Test(expected = DarcyException.class)
    public void shouldNotCreateTargetedDriversForFramesByView() {
        context.findByView(Frame.class, new AlwaysLoadedView());
    }

}
