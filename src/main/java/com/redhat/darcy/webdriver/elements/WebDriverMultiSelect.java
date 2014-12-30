package com.redhat.darcy.webdriver.elements;

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.SelectOption;
import com.redhat.darcy.web.By;
import com.redhat.darcy.web.api.elements.HtmlMultiSelect;
import com.redhat.darcy.web.api.elements.HtmlSelectOption;
import com.redhat.darcy.webdriver.internal.ElementLookup;

import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class WebDriverMultiSelect extends WebDriverElement implements HtmlMultiSelect {
    public WebDriverMultiSelect(ElementLookup source, ElementContext context) {
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
    public List<HtmlSelectOption> getSelectedOptions() {
        return getOptions()
                .stream()
                .filter(SelectOption::isSelected)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isEnabled() {
        return attemptAndGet(WebElement::isEnabled);
    }

    @Override
    public String toString() {
        return "A WebDriverMultiSelect backed by, " + source;
    }
}
