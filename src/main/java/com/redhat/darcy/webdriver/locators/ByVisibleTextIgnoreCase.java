package com.redhat.darcy.webdriver.locators;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * {@link By} implementation that finds elements based on whether or not their
 * text node exactly matches some text, after that text node has been trimmed of 
 * any leading or trailing whitespace.
 * 
 * @author ahenning
 */
public class ByVisibleTextIgnoreCase extends By {
    protected final String text;
    private final By byPartialVisibleTextIgnoreCase;
    
    public ByVisibleTextIgnoreCase(String text) {
        this.text = text.trim();
        this.byPartialVisibleTextIgnoreCase = ByPartialVisibleText.ignoringCase(text);
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        List<WebElement> result = new ArrayList<WebElement>();
        
        // First find any elements that *contain* this text.
        List<WebElement> elems = byPartialVisibleTextIgnoreCase.findElements(context);
        
        // Loop through those elements and only return what exactly matches the
        // text we want, after trimming leading and trailing whitespace.
        for (WebElement e : elems) {
            // getText() is supposed to trim leading and trailing whitespace,
            // but it doesn't always.
            if (e.getText().trim().equalsIgnoreCase(text)) {
                result.add(e);
            }
        }
        
        if (result.size() == 0) {
            throw new NoSuchElementException("Cannot locate an element using "
                    + toString());
        }
        
        return result;
    }
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    @Override
    public String toString() {
        return "ByVisibleTextIgnoreCase: " + text;
    }
}
