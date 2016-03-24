/*
 Copyright 2014 Red Hat, Inc. and/or its affiliates.

 This file is part of darcy-webdriver.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.redhat.darcy.webdriver.locators;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByXPath;

import java.util.List;

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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ByPartialAttribute other = (ByPartialAttribute) obj;
        if (attribute == null) {
            if (other.attribute != null)
                return false;
        } else if (!attribute.equals(other.attribute))
            return false;
        if (word == null) {
            if (other.word != null)
                return false;
        } else if (!word.equals(other.word))
            return false;
        return true;
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
