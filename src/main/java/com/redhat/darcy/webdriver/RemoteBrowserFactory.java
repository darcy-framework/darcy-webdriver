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

import com.redhat.darcy.web.Browser;
import com.redhat.darcy.web.BrowserFactory;
import com.redhat.darcy.webdriver.elements.WebDriverElement;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Fluently describes a {@link BrowserFactory} that creates specifically configured 
 * {@link RemoteWebDriver}s.
 */
public class RemoteBrowserFactory extends WebDriverBrowserFactory<RemoteBrowserFactory> {
    private DesiredCapabilities desired;
    private DesiredCapabilities required;
    private URL hub;
    private ElementConstructorMap elementImpls = ElementConstructorMap
            .newElementConstructorMapWithDefaults();
    
    public RemoteBrowserFactory(URL hub, Capabilities desired) {
        this.hub = hub;
        
        desiring(desired);
    }
    
    public RemoteBrowserFactory(String hubUrl, Capabilities desired) throws MalformedURLException {
        this(new URL(hubUrl), desired);
    }
    
    @Override
    public Browser newBrowser() {
        RemoteWebDriver driver = new RemoteWebDriver(hub, desired, required);
        
        return makeBrowser(driver, elementImpls);
    }
    
    public RemoteBrowserFactory desiring(Capabilities capabilities) {
        desired = new DesiredCapabilities(capabilities, desired);
        return this;
    }
    
    public RemoteBrowserFactory requiring(Capabilities capabilities) {
        required = new DesiredCapabilities(capabilities, required);
        return this;
    }

    @Override
    public <E extends WebDriverElement> RemoteBrowserFactory withElementImplementation(
            Class<? super E> type, ElementConstructor<E> constructor) {
        elementImpls.put(type, constructor);
        return this;
    }
}
