package com.redhat.darcy.webdriver.elements;

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.RadioButton;
import com.redhat.darcy.ui.api.elements.RadioButtonGroup;
import com.redhat.darcy.web.By;
import com.redhat.darcy.webdriver.internal.ElementLookup;

import java.util.List;
import java.util.Optional;

public class WebDriverRadioButtonGroup extends WebDriverElement implements RadioButtonGroup {
    public WebDriverRadioButtonGroup(ElementLookup source, ElementContext context) {
        super(source, context);
    }

    @Override
    public void select(Locator locator) {
        By.nested(this, locator).find(RadioButton.class, getContext()).select();
    }

    @Override
    public List<RadioButton> getRadioButtons() {
        return By.nested(this, By.xpath("//input[@type='radio']")).findAll(RadioButton.class, getContext());
    }

    @Override
    public Optional<RadioButton> getSelectedRadioButton() {
        for (RadioButton radioButton : getRadioButtons()) {
            if (radioButton.isSelected()) {
                return Optional.of(radioButton);
            }
        }

        return Optional.empty();
    }

    @Override
    public String toString() {
        return "A WebDriverRadioButtonGroup backed by, " + source;
    }
}
