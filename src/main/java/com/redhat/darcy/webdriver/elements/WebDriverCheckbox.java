package com.redhat.darcy.webdriver.elements;

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.api.elements.Checkbox;
import com.redhat.darcy.webdriver.internal.ElementLookup;
import org.openqa.selenium.WebElement;

public class WebDriverCheckbox extends WebDriverElement implements Checkbox {
    public WebDriverCheckbox(ElementLookup source, ElementContext context) {
        super(source, context);
    }

    @Override
    public void check() {
        if (!isChecked()) {
            attempt(WebElement::click);
        }
    }

    @Override
    public void uncheck() {
        if (isChecked()) {
            attempt(WebElement::click);
        }
    }

    @Override
    public boolean isChecked() {
        return attemptAndGet(WebElement::isSelected);
    }

    @Override
    public void click() {
        attempt(WebElement::click);
    }

    @Override
    public boolean isEnabled() {
        return attemptAndGet(WebElement::isEnabled);
    }

    @Override
    public String toString() {
        return "A WebDriverCheckbox backed by, " + source;
    }
}
