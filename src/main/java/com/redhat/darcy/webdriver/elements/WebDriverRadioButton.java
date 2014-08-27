package com.redhat.darcy.webdriver.elements;

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.elements.RadioButton;
import com.redhat.darcy.webdriver.internal.ElementLookup;
import org.openqa.selenium.WebElement;

public class WebDriverRadioButton extends WebDriverElement implements RadioButton {
    public WebDriverRadioButton(ElementLookup source, ElementContext context) {
        super(source, context);
    }

    @Override
    public void select() {
        attempt(WebElement::click);
    }

    @Override
    public boolean isSelected() {
        return attemptAndGet(WebElement::isSelected);
    }

    @Override
    public boolean isEnabled() {
        return attemptAndGet(WebElement::isEnabled);
    }

    @Override
    public String toString() {
        return "A WebDriverRadioButton backed by, " + source;
    }
}
