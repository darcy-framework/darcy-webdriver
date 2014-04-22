package com.redhat.darcy.webdriver.elements;

import com.redhat.darcy.ui.Locator;
import com.redhat.darcy.ui.elements.Select;
import com.redhat.darcy.ui.elements.SelectOption;
import com.redhat.darcy.webdriver.ElementFinder;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class WebDriverSelect extends WebDriverElement implements Select {
    
    public WebDriverSelect(WebElement source, ElementFinder finder) {
        super(source, finder);
    }

    @Override
    public void select(Locator locator) {
        locator.find(SelectOption.class, this).select();
    }
    
    @Override
    public List<SelectOption> getOptions() {
        return finder.findElements(SelectOption.class, By.tagName("option"), me);
    }
    
    @Override
    public SelectOption getCurrentlySelectedOption() {
        for (SelectOption option : getOptions()) {
            if (option.isSelected()) {
                return option;
            }
        }
        
        // Should probably throw an exception instead of return null?
        return null;
    }
    
}
