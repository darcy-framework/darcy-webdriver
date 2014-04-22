package com.redhat.darcy.webdriver.elements;

import com.redhat.darcy.ui.elements.SelectOption;
import com.redhat.darcy.webdriver.ElementFinder;

import org.openqa.selenium.WebElement;

public class WebDriverSelectOption extends WebDriverElement implements SelectOption {
    
    public WebDriverSelectOption(WebElement source, ElementFinder finder) {
        super(source, finder);
    }

    @Override
    public String readText() {
        return me.getText();
    }
    
    @Override
    public void select() {
        me.click();
    }
    
    @Override
    public boolean isSelected() {
        return me.isSelected();
    }
    
}
