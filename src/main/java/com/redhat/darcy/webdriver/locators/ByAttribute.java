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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ByAttribute other = (ByAttribute) obj;
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
      return "ByAttribute(\"" + attribute + "\"): " + word;
    }
    
    private String attributeEquals(String attribute, String word) {
        return "@" + attribute + "='" + word + "'";
    }
}
