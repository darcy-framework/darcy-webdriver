package com.redhat.darcy.webdriver.internal;

import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.webdriver.ElementConstructorMap;
import com.redhat.darcy.webdriver.ElementFactory;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class TargetedElementFactory implements ElementFactory {
    private final TargetedWebDriver driver;
    private final ElementConstructorMap elementMap;
    
    public TargetedElementFactory(TargetedWebDriver driver, ElementConstructorMap elementMap) {
        this.driver = driver;
        this.elementMap = elementMap;
    }
    
    @Override
    public <T extends Element> T newElement(Class<T> type, WebElement source) {
        return elementMap.getConstructor(type).newElement(
                driver.createTargetedElement(source),
                driver,
                new DefaultWebDriverElementContext(source, this));
    }
    
    @Override
    public <T extends Element> List<T> newElementList(Class<T> type, List<WebElement> sources) {
        List<T> impls = new ArrayList<>(sources.size());
        
        for (WebElement source : sources) {
            impls.add(newElement(type, source));
        }
        
        return impls;
    }
    
}
