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

package com.redhat.darcy.webdriver.testing.doubles;

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.View;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;

public class ViewLoadedInDriver implements View {
    private final WebDriver hasView;
    private boolean isLoaded;
    private ElementContext context;

    public ViewLoadedInDriver(WebDriver hasView) {
        this.hasView = hasView;
        isLoaded = false;
    }

    @Override
    public void setContext(ElementContext context) {
        this.context = context;
        this.isLoaded = ((WrapsDriver) context).getWrappedDriver().equals(hasView);
    }

    @Override
    public ElementContext getContext() {
        return context;
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }
}
