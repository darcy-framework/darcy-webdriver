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

import com.redhat.darcy.web.api.Alert;
import com.redhat.darcy.webdriver.internal.TargetedAlert;

/**
 * Implements the darcy-web Alert interface by forwarding calls to WebDriver's
 * {@link org.openqa.selenium.Alert} interface.
 */
public class WebDriverAlert implements Alert {
    private final TargetedAlert alert;

    public WebDriverAlert(TargetedAlert alert) {
        this.alert = alert;
    }
    
    @Override
    public boolean isPresent() {
        return alert.isPresent();
    }
    
    @Override
    public void accept() {
        alert.accept();
    }
    
    @Override
    public void dismiss() {
        alert.dismiss();
    }
    
    @Override
    public void sendKeys(CharSequence keysToSend) {
        alert.sendKeys(keysToSend.toString());
    }
    
    @Override
    public String getText() {
        return alert.getText();
    }
}
