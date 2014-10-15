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

import com.redhat.darcy.util.Caching;
import com.redhat.darcy.web.api.Alert;
import com.redhat.darcy.webdriver.internal.TargetedWebDriver;

import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;

/**
 * Implements the darcy-web Alert interface by forwarding calls to WebDriver's
 * {@link org.openqa.selenium.Alert} interface.
 */
public class WebDriverAlert implements Alert, Caching {
    private final WebDriver driver;
    private org.openqa.selenium.Alert cachedAlert;
    
    /**
     * 
     * @param driver May be a {@link TargetedWebDriver}.
     */
    public WebDriverAlert(WebDriver driver) {
        this.driver = driver;
    }
    
    @Override
    public boolean isPresent() {
        try {
            invalidateCache();
            alert();
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }
    
    @Override
    public void accept() {
        alert().accept();
    }
    
    @Override
    public void dismiss() {
        alert().dismiss();
    }
    
    @Override
    public void sendKeys(CharSequence keysToSend) {
        alert().sendKeys(keysToSend.toString());
    }
    
    @Override
    public String getText() {
        return alert().getText();
    }

    @Override
    public void invalidateCache() {
        cachedAlert = null;
    }
    
    private org.openqa.selenium.Alert alert() {
        if (cachedAlert == null) {
            cachedAlert = driver.switchTo().alert();
        }
        
        // TODO: What happens if alert gets cached, user switches to something else, then cached
        // alert is used?
        return cachedAlert;
    }
}
