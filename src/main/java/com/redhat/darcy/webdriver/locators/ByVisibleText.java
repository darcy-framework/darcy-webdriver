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

import java.util.ArrayList;
import java.util.List;

/**
 * {@link By} implementation that finds elements based on whether or not their
 * text node exactly matches some text, after that text node has been trimmed of 
 * any leading or trailing whitespace.
 * 
 * @author ahenning
 */
public class ByVisibleText extends By {
    protected final String text;
    private final ByPartialVisibleText byPartialVisibleText;
    
    public static By ignoringCase(String text) {
        return new ByVisibleTextIgnoreCase(text);
    }
    
    public ByVisibleText(String text) {
        this.text = text.trim();
        this.byPartialVisibleText = new ByPartialVisibleText(text);
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        List<WebElement> result = new ArrayList<>();
        
        // First find any elements that *contain* this text.
        List<WebElement> elems = byPartialVisibleText.findElements(context);
        
        // Loop through those elements and only return what exactly matches the
        // text we want, after trimming leading and trailing whitespace.
        for (WebElement e : elems) {
            // getText() is supposed to trim leading and trailing whitespace,
            // but it doesn't always.
            if (e.getText().trim().equals(text)) {
                result.add(e);
            }
        }
        
        return result;
    }
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    @Override
    public String toString() {
        return "ByVisibleText: " + text;
    }
}
