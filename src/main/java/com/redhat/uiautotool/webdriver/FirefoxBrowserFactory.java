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

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.redhat.uiautotool.web.Browser;
import com.redhat.uiautotool.web.BrowserFactory;

public class FirefoxBrowserFactory implements BrowserFactory {
    private DesiredCapabilities desired;
    private DesiredCapabilities required;
    private FirefoxProfile profile;
    private FirefoxBinary binary;
    
    @Override
    public Browser newBrowser() {
        FirefoxDriver driver;
        
        if (profile != null) {
            if (binary != null) {
                if (desired != null) {
                    driver = new FirefoxDriver(binary, profile, desired, required);
                } else {
                    driver = new FirefoxDriver(binary, profile);
                }
            } else {
                driver = new FirefoxDriver(profile);
            }
        } else if (desired != null) {
            driver = new FirefoxDriver(desired, required);
        } else {
            driver = new FirefoxDriver();
        }
        
        return WebDriverBrowserManager.newManagedBrowser(driver);
    }
    
    public FirefoxBrowserFactory desiring(Capabilities capabilities) {
        desired = new DesiredCapabilities(capabilities, desired);
        return this;
    }
    
    public FirefoxBrowserFactory requiring(Capabilities capabilities) {
        required = new DesiredCapabilities(capabilities, required);
        return this;
    }
    
    public FirefoxBrowserFactory usingProfile(FirefoxProfile fp) {
        profile = fp;
        return this;
    }
    
    public FirefoxBrowserFactory usingBinary(FirefoxBinary fb) {
        binary = fb;
        return this;
    }
}
