package com.redhat.darcy.webdriver.elements;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.redhat.darcy.ui.Locator;
import com.redhat.darcy.ui.elements.Select;
import com.redhat.darcy.ui.elements.SelectOption;
import com.redhat.darcy.webdriver.ElementFactoryMap;

public class WebDriverSelect extends WebDriverElement implements Select {
    
    public WebDriverSelect(WebElement source, ElementFactoryMap elements) {
        super(source, elements);
    }

    @Override
    public void select(Locator locator) {
        locator.find(SelectOption.class, this).select();
    }
    
    @Override
    public List<SelectOption> getOptions() {
        List<WebElement> sources = me.findElements(By.tagName("option"));
        List<SelectOption> options = new ArrayList<>(sources.size());
        
        for (WebElement source : sources) {
            options.add(elements.getElement(SelectOption.class, source));
        }
        
        return options;
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
