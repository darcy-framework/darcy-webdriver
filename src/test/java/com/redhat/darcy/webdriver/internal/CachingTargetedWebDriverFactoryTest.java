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

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.redhat.darcy.webdriver.testing.rules.TraceTestName;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.WebDriver;

@RunWith(JUnit4.class)
public class CachingTargetedWebDriverFactoryTest {
    @Rule
    public TraceTestName traceTestName = new TraceTestName();

    @Test
    public void shouldCreateTargetedWebDriversThatImplementAllInterfacesOfUntargetedDriver() {
        TestDriver mockTestDriver = mock(TestDriver.class);
        when(mockTestDriver.doSomething()).thenReturn("did something");

        TargetedDriverFactory targetedDriverFactory = new CachingTargetedWebDriverFactory(
                mockTestDriver, WebDriverTargets.window("test"));

        TargetedWebDriver targetedWebDriver = targetedDriverFactory
                .getTargetedDriver(WebDriverTargets.window("test"));

        assertThat(targetedWebDriver, instanceOf(TestDriver.class));
        assertEquals("did something", ((TestDriver) targetedWebDriver).doSomething());
    }

    interface TestDriver extends WebDriver {
        String doSomething();
    }
}
