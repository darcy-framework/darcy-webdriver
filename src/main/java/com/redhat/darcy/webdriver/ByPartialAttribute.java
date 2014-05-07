/*
Copyright 2007-2011 Selenium committers

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.redhat.darcy.webdriver;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByXPath;

/**
 * {@link By} implementation that finds elements based on whether or not a 
 * specified attributes contains some text.
 * 
 * @author ahenning
 */
public class ByPartialAttribute extends By {
    private final String attribute;
    private final String word;
    
    public ByPartialAttribute(String attribute, String word) {
        this.attribute = attribute;
        this.word = word;
    }
    
    @Override
    public List<WebElement> findElements(SearchContext context) {
      return ((FindsByXPath) context).findElementsByXPath(".//*["
          + attributeContains(attribute, word) + "]");
    }

    @Override
    public WebElement findElement(SearchContext context) {
      return ((FindsByXPath) context).findElementByXPath(".//*["
          + attributeContains(attribute, word) + "]");
    }
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    @Override
    public String toString() {
      return "ByPartialAttribute(\"" + attribute + "\"): " + word;
    }
    
    /**
     * Generates a partial xpath expression that matches an element whose specified attribute
     * contains the given CSS word. So to match &lt;div class='foo bar'&gt; you would say "//div[" +
     * containsWord("class", "foo") + "]".
     * 
     * @param attribute name
     * @param word name
     * @return XPath fragment
     */
    private String attributeContains(String attribute, String word) {
      return "contains(concat(' ',normalize-space(@" + attribute + "),' '),"
              + "' " + word + " ')";
    }
}
