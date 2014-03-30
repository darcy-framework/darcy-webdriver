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

package com.redhat.uiautotool.webdriver;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.redhat.uiautotool.web.Browser;
import com.redhat.uiautotool.web.BrowserFactory;

public class RemoteBrowserFactory implements BrowserFactory {
    private DesiredCapabilities desired;
    private DesiredCapabilities required;
    private URL hub;
    
    public RemoteBrowserFactory(URL hub, Capabilities desired) {
        this.hub = hub;
        
        desiring(desired);
    }
    
    public RemoteBrowserFactory(String hubUrl) throws MalformedURLException {
        this.hub = new URL(hubUrl);
    }
    
    @Override
    public Browser newBrowser() {
        RemoteWebDriver driver = new RemoteWebDriver(hub, desired, required);
        
        return WebDriverBrowserManager.newManagedBrowser(driver);
    }
    
    public RemoteBrowserFactory desiring(Capabilities capabilities) {
        desired = new DesiredCapabilities(capabilities, desired);
        return this;
    }
    
    public RemoteBrowserFactory requiring(Capabilities capabilities) {
        required = new DesiredCapabilities(capabilities, required);
        return this;
    }
    
}
