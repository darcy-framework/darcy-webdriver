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

import com.redhat.darcy.util.ReflectionUtil;

import org.openqa.selenium.WebDriver;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Creates {@link TargetedWebDriver}s for a given vanilla {@link WebDriver} implementation. These
 * {@link TargetedWebDriver}s point to a specific {@link WebDriverTarget}; that is, methods on the
 * {@link TargetedWebDriver} will only interact with the window or frame that driver is targeting.
 * <P>
 * This implementation uses a {@link CachingTargetLocator}. That is, when a targeted driver is 
 * asking the parent {@link WebDriver} to switch to its target, the {@link CachingTargetLocator} 
 * will only ask the parent driver to switch if it is not already known that the driver is pointing
 * to the correct target already.
 */
public class CachingTargetedWebDriverFactory implements TargetedWebDriverFactory {
    private final CachingTargetLocator cachingTargetLocator;
    private final Class<?>[] interfaces;
    
    public CachingTargetedWebDriverFactory(WebDriver untargetedDriver,
            WebDriverTarget currentTarget) {
        cachingTargetLocator = new CachingTargetLocator(currentTarget, untargetedDriver);
        
        List<Class<?>> interfaces = ReflectionUtil.getAllInterfaces(untargetedDriver);
        interfaces.add(TargetedWebDriver.class);
        
        this.interfaces = interfaces.toArray(new Class[]{});
    }
    
    @Override
    public TargetedWebDriver getTargetedWebDriver(WebDriverTarget target) {
        return (TargetedWebDriver) Proxy.newProxyInstance(
                CachingTargetedWebDriverFactory.class.getClassLoader(),
                interfaces,
                new TargetedWebDriverInvocationHandler(cachingTargetLocator, target));
    }
}
