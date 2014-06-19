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

import com.redhat.darcy.webdriver.ElementConstructorMap;

/**
 * Creates {@link TargetedElementFactory TargetedElementFactories} for a given
 * {@link TargetedWebDriver}.
 *
 * <blockquote>"And a factory in Chicago, that makes miniature models... of factories."</blockquote>
 */
public class TargetedElementFactoryFactory {
    private final ElementConstructorMap factoryMap;
    
    public TargetedElementFactoryFactory(ElementConstructorMap factoryMap) {
        this.factoryMap = factoryMap;
    }

    public TargetedElementFactory newTargetedElementFactory(TargetedWebDriver driver) {
        return new TargetedElementFactory(driver, factoryMap);
    }
    
}
