package com.redhat.darcy.webdriver.locators;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByXPath;

/**
 * {@link By} implementation that finds elements based on whether or not their
 * text node partially matches some text, ignoring case.
 * 
 * @author ahenning
 */
public class ByPartialVisibleTextIgnoreCase extends By {
    private final String text;
    
    public ByPartialVisibleTextIgnoreCase(String text) {
        this.text = text;
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        return ((FindsByXPath) context).findElementsByXPath(".//*["
                + textContainsIgnoringCase(text) + "]");
    }

    @Override
    public WebElement findElement(SearchContext context) {
        return ((FindsByXPath) context).findElementByXPath(".//*["
                + textContainsIgnoringCase(text) + "]");
    }
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    @Override
    public String toString() {
        return "ByPartialVisibleTextIgnoreCase: " + text;
    }
    
    /**
     * Generates a partial XPath expression that matches an element whose text
     * node contains some text.
     * 
     * @param attribute name
     * @param text name
     * @return String Partial XPath expression
     */
    private String textContainsIgnoringCase(String text) {
        return String.format("contains(translate(text(),'%s','%s'),'%s')",
                // Take any upper case letters in the text
                text.toUpperCase(),
                // Translate them to lower case
                text.toLowerCase(),
                // Compare this with the text, lower-cased
                text.toLowerCase());
    }
}
