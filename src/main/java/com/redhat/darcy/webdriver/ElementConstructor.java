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

import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.webdriver.elements.WebDriverElement;
import com.redhat.darcy.webdriver.internal.ElementFactory;

import org.openqa.selenium.WebElement;

import java.util.function.Supplier;

/**
 * A {@link FunctionalInterface} to represent {@link WebDriverElement}s' and subclasses'
 * constructors.
 * 
 * @param <T>
 */
@FunctionalInterface
public interface ElementConstructor<T extends Element> {
    T newElement(WebElement source, ElementFactory converter);
}
