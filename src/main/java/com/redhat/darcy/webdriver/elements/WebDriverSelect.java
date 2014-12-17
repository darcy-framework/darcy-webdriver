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

package com.redhat.darcy.webdriver.elements;

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.SelectOption;
import com.redhat.darcy.web.By;
import com.redhat.darcy.web.api.elements.HtmlSelect;
import com.redhat.darcy.web.api.elements.HtmlSelectOption;
import com.redhat.darcy.webdriver.internal.ElementLookup;

import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Optional;

public class WebDriverSelect extends WebDriverElement implements HtmlSelect {

    public WebDriverSelect(ElementLookup source, ElementContext context) {
        super(source, context);
    }

    @Override
    public void select(Locator locator) {
        By.nested(this, locator).find(SelectOption.class, getContext()).select();
    }

    @Override
    public List<HtmlSelectOption> getOptions() {
        return By.nested(this, By.htmlTag("option")).findAll(HtmlSelectOption.class, getContext());
    }

    @Override
    public Optional<HtmlSelectOption> getSelectedOption() {
        for (HtmlSelectOption option : getOptions()) {
            if (option.isSelected()) {
                return Optional.of(option);
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean isEnabled() {
        return attemptAndGet(WebElement::isEnabled);
    }

    @Override
    public String toString() {
        return "A WebDriverSelect backed by, " + source;
    }
}
