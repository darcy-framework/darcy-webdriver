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

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.internal.NestedViewFactory;
import com.redhat.darcy.webdriver.ElementConstructor;
import com.redhat.darcy.webdriver.elements.WebDriverElement;

public class ViewElementConstructor<T extends Element & View> implements ElementConstructor<T> {
    private final NestedViewFactory<T> viewElementCtor;

    public ViewElementConstructor(NestedViewFactory<T> viewElementCtor) {
        this.viewElementCtor = viewElementCtor;
    }

    @Override
    public T newElement(ElementLookup source, ElementContext context) {
        return viewElementCtor.newElement(new WebDriverElement(source, context));
    }
}
