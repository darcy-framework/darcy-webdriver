package com.redhat.darcy.webdriver;

import com.redhat.darcy.ui.elements.Element;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds a WebElement and gives back an implementation of the Element interface requested.
 */
public class ElementFinder {
    private final ElementFactoryMap elements;
    
    public ElementFinder(ElementFactoryMap elements) {
        this.elements = elements;
    }
    
    public <T extends Element> T findElement(Class<T> type, By locator, SearchContext context) {
        WebElement source = context.findElement(locator);
        
        return elements.getFactory(type).newElement(source, this);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Element> List<T> findElements(Class<T> type, By locator, SearchContext context) {
        List<WebElement> sources = context.findElements(locator);
        List<T> impls = new ArrayList<>(sources.size());
        
        for (WebElement source : sources) {
            impls.add((T) elements.getFactory((Class<Element>) type).newElement(source, this));
        }
        
        return impls;
    }
}
