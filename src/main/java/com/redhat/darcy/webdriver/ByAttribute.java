package com.redhat.darcy.webdriver;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByXPath;

/**
 * {@link By} implementation that finds elements based on whether or not a 
 * specified attributes exactly matches some text.
 * 
 * @author ahenning
 */
public class ByAttribute extends By {
    protected final String attribute;
    protected final String word;
    
    /**
     * Returns instance of ByAttribute that will search under the "value" 
     * attribute.
     * @param value
     * @return
     */
    public static By value(String value) {
        return new ByAttribute("value", value);
    }
    
    public static By index(int index) {
        return new ByAttribute("index", String.valueOf(index));
    }
    
    public static By labelFor(String what) {
        return new ByAttribute("for", what);
    }
    
    public ByAttribute(String attribute, String word) {
        this.attribute = attribute;
        this.word = word;
    }
    
    @Override
    public List<WebElement> findElements(SearchContext context) {
      return ((FindsByXPath) context).findElementsByXPath(".//*["
          + attributeEquals(attribute, word) + "]");
    }

    @Override
    public WebElement findElement(SearchContext context) {
      return ((FindsByXPath) context).findElementByXPath(".//*["
          + attributeEquals(attribute, word) + "]");
    }
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    @Override
    public String toString() {
      return "ByAttribute(\"" + attribute + "\"): " + word;
    }
    
    private String attributeEquals(String attribute, String word) {
        return "@" + attribute + "='" + word + "'";
    }
}
