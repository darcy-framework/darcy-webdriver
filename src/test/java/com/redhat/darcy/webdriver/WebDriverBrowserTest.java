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

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.openqa.selenium.WebDriver.Navigation;

import com.redhat.darcy.ui.View;
import com.redhat.darcy.web.Browser;
import com.redhat.darcy.webdriver.internal.TargetedWebDriver;
import com.redhat.darcy.webdriver.testing.doubles.AlwaysLoadedView;
import com.redhat.darcy.webdriver.testing.doubles.StubWebDriverElementContext;
import com.redhat.darcy.webdriver.testing.doubles.StubWebDriverParentContext;
import com.redhat.darcy.webdriver.testing.rules.TraceTestName;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;

@RunWith(JUnit4.class)
public class WebDriverBrowserTest {
    @Rule
    public TraceTestName traceTestName = new TraceTestName();

    @Test
    public void shouldOpenUrlInDriver() {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);

        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        browser.open(AlwaysLoadedView.url());

        verify(mockedDriver).get(AlwaysLoadedView.url().url());
    }

    @Test
    public void shouldSetViewContextWhenOpeningUrl() {
        Browser browser = new WebDriverBrowser(mock(TargetedWebDriver.class),
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        AlwaysLoadedView view = browser.open(AlwaysLoadedView.url());

        assertNotNull(view.getContext());
        assertSame(browser, view.getContext());
    }

    @Test
    public void shouldReturnFromOpenOnlyWhenViewIsLoaded() {
        View mockedView = mock(View.class);

        // Only return true on 3rd invocation
        when(mockedView.isLoaded()).thenReturn(false, false, true);

        Browser browser = new WebDriverBrowser(mock(TargetedWebDriver.class),
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        browser.open("", mockedView);

        // open should keep calling isLoaded until it returns true
        verify(mockedView, times(3)).isLoaded();
    }

    @Test
    public void shouldReturnViewWhenOpeningUrl() {
        Browser browser = new WebDriverBrowser(mock(TargetedWebDriver.class),
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        AlwaysLoadedView view = new AlwaysLoadedView();

        AlwaysLoadedView returnedView = browser.open("", view);

        assertSame(view, returnedView);
    }

    @Test
    public void shouldGetTitleFromDriver() {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);

        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        browser.getTitle();

        verify(mockedDriver).getTitle();
    }

    @Test
    public void shouldGetCurrentUrlFromDriver() {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);

        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        browser.getCurrentUrl();

        verify(mockedDriver).getCurrentUrl();
    }

    @Test
    public void shouldGetPageSourceFromDriver() {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);

        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        browser.getSource();

        verify(mockedDriver).getPageSource();
    }

    @Test
    public void shouldNavigateBack() {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);
        Navigation mockedNavigation = mock(Navigation.class);

        when(mockedDriver.navigate()).thenReturn(mockedNavigation);

        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        browser.back(new AlwaysLoadedView());

        verify(mockedNavigation).back();
    }

    @Test
    public void shouldSetViewContextWhenNavigatingBack() {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);
        Navigation mockedNavigation = mock(Navigation.class);

        when(mockedDriver.navigate()).thenReturn(mockedNavigation);

        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        AlwaysLoadedView view = new AlwaysLoadedView();
        browser.back(view);

        assertSame(browser, view.getContext());
    }

    @Test
    public void shouldReturnFromBackOnlyWhenViewIsLoaded() {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);
        Navigation mockedNavigation = mock(Navigation.class);

        when(mockedDriver.navigate()).thenReturn(mockedNavigation);

        View mockedView = mock(View.class);

        when(mockedView.isLoaded()).thenReturn(false, false, true);

        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        browser.back(mockedView);

        verify(mockedView, times(3)).isLoaded();
    }

    @Test
    public void shouldReturnViewWhenNavigatingBack() {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);
        Navigation mockedNavigation = mock(Navigation.class);

        when(mockedDriver.navigate()).thenReturn(mockedNavigation);

        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        AlwaysLoadedView view = new AlwaysLoadedView();
        AlwaysLoadedView returnedView = browser.back(view);

        assertSame(view, returnedView);
    }

    @Test
    public void shouldNavigateForward() {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);
        Navigation mockedNavigation = mock(Navigation.class);

        when(mockedDriver.navigate()).thenReturn(mockedNavigation);

        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        browser.forward(new AlwaysLoadedView());

        verify(mockedNavigation).forward();
    }

    @Test
    public void shouldSetViewContextWhenNavigatingForward() {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);
        Navigation mockedNavigation = mock(Navigation.class);

        when(mockedDriver.navigate()).thenReturn(mockedNavigation);

        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        AlwaysLoadedView view = new AlwaysLoadedView();
        browser.forward(view);

        assertNotNull(view.getContext());
        assertSame(browser, view.getContext());
    }

    @Test
    public void shouldReturnFromForwardOnlyWhenViewIsLoaded() {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);
        Navigation mockedNavigation = mock(Navigation.class);

        when(mockedDriver.navigate()).thenReturn(mockedNavigation);

        View mockedView = mock(View.class);

        when(mockedView.isLoaded()).thenReturn(false, false, true);

        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        browser.forward(mockedView);

        verify(mockedView, times(3)).isLoaded();
    }

    @Test
    public void shouldReturnViewWhenNavigatingForward() {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);
        Navigation mockedNavigation = mock(Navigation.class);

        when(mockedDriver.navigate()).thenReturn(mockedNavigation);

        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        AlwaysLoadedView view = new AlwaysLoadedView();
        AlwaysLoadedView returnedView = browser.forward(view);

        assertSame(view, returnedView);
    }

    @Test
    public void shouldRefresh() {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);
        Navigation mockedNavigation = mock(Navigation.class);

        when(mockedDriver.navigate()).thenReturn(mockedNavigation);

        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        browser.refresh(new AlwaysLoadedView());

        verify(mockedNavigation).refresh();
    }

    @Test
    public void shouldSetViewContextWhenRefreshing() {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);
        Navigation mockedNavigation = mock(Navigation.class);

        when(mockedDriver.navigate()).thenReturn(mockedNavigation);

        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        AlwaysLoadedView view = new AlwaysLoadedView();
        browser.refresh(view);

        assertNotNull(view.getContext());
        assertSame(browser, view.getContext());
    }

    @Test
    public void shouldReturnFromRefreshOnlyWhenViewIsLoaded() {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);
        Navigation mockedNavigation = mock(Navigation.class);

        when(mockedDriver.navigate()).thenReturn(mockedNavigation);

        View mockedView = mock(View.class);

        when(mockedView.isLoaded()).thenReturn(false, false, true);

        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        browser.refresh(mockedView);

        verify(mockedView, times(3)).isLoaded();
    }

    @Test
    public void shouldReturnViewWhenRefreshing() {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);
        Navigation mockedNavigation = mock(Navigation.class);

        when(mockedDriver.navigate()).thenReturn(mockedNavigation);

        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        AlwaysLoadedView view = new AlwaysLoadedView();
        AlwaysLoadedView returnedView = browser.refresh(view);

        assertSame(view, returnedView);
    }

    @Test
    public void shouldClose() {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);

        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        browser.close();

        verify(mockedDriver).close();
    }

    @Test
    public void shouldCloseAll() {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);

        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        browser.closeAll();

        verify(mockedDriver).quit();
    }

    @Test
    public void shouldWrapDriver() {
        Browser browser = new WebDriverBrowser(mock(TargetedWebDriver.class),
                new StubWebDriverParentContext(), new StubWebDriverElementContext());

        assertThat(browser, instanceOf(WrapsDriver.class));
    }
}
