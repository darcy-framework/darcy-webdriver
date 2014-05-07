package com.redhat.darcy.webdriver;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByXPath;

/**
 * {@link By} implementation that finds elements based on whether or not their
 * text node partially matches some text.
 * 
 * @author ahenning
 */
public class ByPartialVisibleText extends By {
    private final String text;
    
    public static By ignoringCase(String text) {
        return new ByPartialVisibleTextIgnoreCase(text);
    }
    
    public ByPartialVisibleText(String text) {
        this.text = text;
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        return ((FindsByXPath) context).findElementsByXPath(".//*["
                + textContains(text) + "]");
    }

    @Override
    public WebElement findElement(SearchContext context) {
        return ((FindsByXPath) context).findElementByXPath(".//*["
                + textContains(text) + "]");
    }
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    @Override
    public String toString() {
        return "ByPartialVisibleText: " + text;
    }
    
    /**
     * Generates a partial XPath expression that matches an element whose text
     * node contains some text.
     * 
     * @param attribute name
     * @param text name
     * @return String Partial XPath expression
     */
    private String textContains(String text) {
        return "contains(text(),'" + text + "')";
    }
}
