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

import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.web.api.Alert;
import com.redhat.darcy.webdriver.WebDriverParentContext;

import java.util.List;

public class StubWebDriverParentContext implements WebDriverParentContext {
    @Override
    public <T> List<T> findAllById(Class<T> type, String id) {
        throw new UnsupportedOperationException("findAllById");
    }

    @Override
    public <T> T findById(Class<T> type, String id) {
        throw new UnsupportedOperationException("findById");
    }

    @Override
    public Alert alert() {
        throw new UnsupportedOperationException("alert");
    }

    @Override
    public <T> List<T> findAllByName(Class<T> type, String name) {
        throw new UnsupportedOperationException("findAllByName");
    }

    @Override
    public <T> T findByName(Class<T> type, String name) {
        throw new UnsupportedOperationException("findByName");
    }

    @Override
    public <T> List<T> findAllByView(Class<T> type, View view) {
        throw new UnsupportedOperationException("findAllByView");
    }

    @Override
    public <T> T findByView(Class<T> type, View view) {
        throw new UnsupportedOperationException("findByView");
    }

    @Override
    public <T> List<T> findAllByTitle(Class<T> type, String title) {
        throw new UnsupportedOperationException("findAllByTitle");
    }

    @Override
    public <T> T findByTitle(Class<T> type, String title) {
        throw new UnsupportedOperationException("findByTitle");
    }
}
