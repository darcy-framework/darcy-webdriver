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

import static java.util.Arrays.asList;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.web.By;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByTagName;

@RunWith(JUnit4.class)
public class ByChainedTest {
    @Test
    public void shouldFindByChained() {
        TestContext mockContext = mock(TestContext.class);
        ParentWebElement parentOfTest = mock(ParentWebElement.class, "parentOfTest");
        ParentWebElement parentOfDiv = mock(ParentWebElement.class, "parentOfDiv");
        WebElement childDiv = mock(WebElement.class, "childDiv");

        when(mockContext.findElementsById("anId")).thenReturn(asList(parentOfTest));
        when(parentOfTest.findElementsByClassName("test")).thenReturn(asList(parentOfDiv));
        when(parentOfDiv.findElementsByTagName("div")).thenReturn(asList(childDiv));

        ByChained byChained = new ByChained(By.id("anId"), By.className("test"), By.htmlTag("div"));

        WebElement found = byChained.findElement(mockContext);

        assertSame(childDiv, found);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfPassedEmptyArray() {
        new ByChained();
    }

    interface ParentWebElement extends WebElement, FindsByClassName, FindsByTagName {}
    interface TestContext extends SearchContext, FindsById {}
}
