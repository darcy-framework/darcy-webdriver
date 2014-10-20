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

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.security.Credentials;

public class ForwardingTargetedAlert implements TargetedAlert {
    private final WebDriver.TargetLocator locator;

    public ForwardingTargetedAlert(WebDriver.TargetLocator locator) {
        this.locator = locator;
    }

    @Override
    public boolean isPresent() {
        try {
            alert().getText();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    @Override
    public void dismiss() {
        alert().dismiss();
    }

    @Override
    public void accept() {
        alert().accept();
    }

    @Override
    public String getText() {
        return alert().getText();
    }

    @Override
    public void sendKeys(String keysToSend) {
        alert().sendKeys(keysToSend);
    }

    @Override
    public void authenticateUsing(Credentials credentials) {
        alert().authenticateUsing(credentials);
    }

    private Alert alert() {
        return locator.alert();
    }
}
