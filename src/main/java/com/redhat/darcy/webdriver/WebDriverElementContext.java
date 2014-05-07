package com.redhat.darcy.webdriver;

import com.redhat.darcy.ui.ElementContext;
import com.redhat.darcy.ui.FindsByChained;
import com.redhat.darcy.ui.FindsById;
import com.redhat.darcy.ui.FindsByLinkText;
import com.redhat.darcy.ui.FindsByName;
import com.redhat.darcy.ui.FindsByNested;
import com.redhat.darcy.ui.FindsByPartialTextContent;
import com.redhat.darcy.ui.FindsByTextContent;
import com.redhat.darcy.ui.FindsByXPath;
import com.redhat.darcy.ui.Locator;
import com.redhat.darcy.ui.elements.Element;
import com.redhat.darcy.web.FindsByCssSelector;
import com.redhat.darcy.web.FindsByHtmlTag;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public interface WebDriverElementContext extends ElementContext, FindsById, FindsByName,
        FindsByLinkText, FindsByTextContent, FindsByPartialTextContent, FindsByXPath,
        FindsByCssSelector, FindsByHtmlTag, FindsByChained, FindsByNested {
    ElementFinder finder();
    SearchContext searchContext();
    
    @SuppressWarnings("unchecked")
    @Override
    default <T> T findById(Class<T> type, String id) {
        return (T) finder().findElement((Class<Element>) type, By.id(id), searchContext());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    default <T> List<T> findAllById(Class<T> type, String id) {
        return (List<T>) finder().findElements((Class<Element>) type, By.id(id), searchContext());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    default <T> List<T> findAllByName(Class<T> type, String name) {
        return (List<T>) finder().findElements((Class<Element>) type, By.name(name),
                searchContext());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    default <T> T findByLinkText(Class<T> type, String linkText) {
        return (T) finder().findElement((Class<Element>) type, By.linkText(linkText),
                searchContext());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    default <T> List<T> findAllByLinkText(Class<T> type, String linkText) {
        return (List<T>) finder().findElements((Class<Element>) type, By.linkText(linkText),
                searchContext());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    default <T> T findByTextContent(Class<T> type, String textContent) {
        return (T) finder().findElement((Class<Element>) type, new ByVisibleText(textContent),
                searchContext());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    default <T> List<T> findAllByTextContent(Class<T> type, String partialTextContent) {
        return (List<T>) finder().findElements((Class<Element>) type,
                new ByPartialVisibleText(partialTextContent), searchContext());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    default <T> T findByPartialTextContent(Class<T> type, String partialTextContent) {
        return (T) finder().findElement((Class<Element>) type,
                new ByPartialVisibleText(partialTextContent), searchContext());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    default <T> List<T> findAllByPartialTextContent(Class<T> type, String textContent) {
        return (List<T>) finder().findElements((Class<Element>) type,
                new ByVisibleText(textContent), searchContext());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    default <T> T findByXPath(Class<T> type, String xpath) {
        return (T) finder().findElement((Class<Element>) type, By.xpath(xpath), searchContext());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    default <T> List<T> findAllByXPath(Class<T> type, String xpath) {
        return (List<T>) finder().findElements((Class<Element>) type, By.xpath(xpath),
                searchContext());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    default <T> List<T> findAllByHtmlTag(Class<T> type, String tag) {
        return (List<T>) finder().findElements((Class<Element>) type, By.tagName(tag),
                searchContext());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    default <T> List<T> findAllByCssSelector(Class<T> type, String css) {
        return (List<T>) finder().findElements((Class<Element>) type, By.cssSelector(css),
                searchContext());
    }
    
    // TODO: If one of the elements along the way is a frame, switch to it
    @Override
    default <T> List<T> findAllByChained(Class<T> type, Locator... locators) {
        if (locators.length == 0) {
            return new ArrayList<T>(0);
        }
        
        List<T> elements = null;
        List<T> subElements = new LinkedList<>();
        
        for (Locator locator : locators) {
            if (elements == null) {
                elements = locator.findAll(type, this);
            } else {
                for (T element : elements) {
                    subElements.addAll(locator.findAll(type, (ElementContext) element));
                }
                
                elements = subElements;
                subElements.clear();
            }
        }
        
        return elements;
    }
    
    @Override
    default <T> List<T> findAllByNested(Class<T> type, Element parent, Locator child) {
        return child.findAll(type, (ElementContext) parent);
    }
}
