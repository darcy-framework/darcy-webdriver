package com.redhat.darcy.webdriver.elements;

import org.openqa.selenium.WebElement;

import com.redhat.darcy.ui.elements.SelectOption;
import com.redhat.darcy.webdriver.ElementFactoryMap;

public class WebDriverSelectOption extends WebDriverElement implements SelectOption {
    
    public WebDriverSelectOption(WebElement source, ElementFactoryMap elements) {
        super(source, elements);
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
