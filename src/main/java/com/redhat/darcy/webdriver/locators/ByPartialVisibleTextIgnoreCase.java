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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ByPartialVisibleTextIgnoreCase other = (ByPartialVisibleTextIgnoreCase) obj;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
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
