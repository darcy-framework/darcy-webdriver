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

import com.redhat.darcy.ui.api.elements.Findable;

import org.openqa.selenium.WebDriver;

/**
 * Interface for {@link WebDriver} proxies that are "targeted", that is, every method invocation on
 * the driver will be intercepted so that the driver may be switched to the appropriate
 * <em>target</em> before proceeding with the method. A target is a specific frame or window. In
 * this way, every instance of a TargetedWebDriver is specific to a target that a driver may point
 * to, as opposed to pointing to one of a number of possible targets.
 * <p>
 * Elements found by this driver should also be targeted. That is, elements are associated with a
 * driver and target and will ensure the driver is switched before interacting with the element.
 */
public interface TargetedWebDriver extends WebDriver, Findable {
    WebDriverTarget getWebDriverTarget();
}
