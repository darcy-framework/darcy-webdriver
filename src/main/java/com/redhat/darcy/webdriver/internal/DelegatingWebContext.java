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

package com.redhat.darcy.webdriver.internal;

import com.redhat.darcy.ui.api.Context;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.Transition;
import com.redhat.darcy.ui.api.View;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.internal.FindsByAttribute;
import com.redhat.darcy.ui.internal.FindsById;
import com.redhat.darcy.ui.internal.FindsByLinkText;
import com.redhat.darcy.ui.internal.FindsByName;
import com.redhat.darcy.ui.internal.FindsByNested;
import com.redhat.darcy.ui.internal.FindsByPartialTextContent;
import com.redhat.darcy.ui.internal.FindsByTextContent;
import com.redhat.darcy.ui.internal.FindsByView;
import com.redhat.darcy.ui.internal.FindsByXPath;
import com.redhat.darcy.web.api.Alert;
import com.redhat.darcy.web.api.WebSelection;
import com.redhat.darcy.web.internal.FindsByClassName;
import com.redhat.darcy.web.internal.FindsByCss;
import com.redhat.darcy.web.internal.FindsByHtmlTag;
import com.redhat.darcy.webdriver.WebDriverElementContext;
import com.redhat.darcy.webdriver.WebDriverParentContext;

import java.util.List;
import java.util.Objects;

/**
 * A {@link com.redhat.darcy.webdriver.internal.WebDriverWebContext} that delegates to a separate
 * ElementContext and ParentContext implementation based on the type of the element.
 */
public class DelegatingWebContext implements WebDriverWebContext {
    private final WebDriverElementContext elementContext;
    private final WebDriverParentContext parentContext;

    public DelegatingWebContext(WebDriverElementContext elementContext,
            WebDriverParentContext parentContext) {
        this.elementContext = Objects.requireNonNull(elementContext);
        this.parentContext = Objects.requireNonNull(parentContext);
    }

    @Override
    public Transition transition() {
        return elementContext.transition();
    }

    @Override
    public WebSelection find() {
        return new WebDriverWebSelection(this);
    }

    @Override
    public Alert alert() {
        return parentContext.alert();
    }

    @Override
    public <T> List<T> findAllByChained(Class<T> type, Locator... locators) {
        try {
            FindsByNested context = (FindsByNested) contextForType(type);

            return context.findAllByChained(type, locators);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("chained", type);
        }
    }

    @Override
    public <T> T findByChained(Class<T> type, Locator... locators) {
        try {
            FindsByNested context = (FindsByNested) contextForType(type);

            return context.findByChained(type, locators);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("chained", type);
        }
    }

    @Override
    public <T> List<T> findAllByCss(Class<T> type, String css) {
        try {
            FindsByCss context = (FindsByCss) contextForType(type);

            return context.findAllByCss(type, css);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("CSS", type);
        }
    }

    @Override
    public <T> T findByCss(Class<T> type, String css) {
        try {
            FindsByCss context = (FindsByCss) contextForType(type);

            return context.findByCss(type, css);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("CSS", type);
        }
    }

    @Override
    public <T> List<T> findAllByHtmlTag(Class<T> type, String tag) {
        try {
            FindsByHtmlTag context = (FindsByHtmlTag) contextForType(type);

            return context.findAllByHtmlTag(type, tag);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("HTML tag", type);
        }
    }

    @Override
    public <T> T findByHtmlTag(Class<T> type, String tag) {
        try {
            FindsByHtmlTag context = (FindsByHtmlTag) contextForType(type);

            return context.findByHtmlTag(type, tag);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("HTML tag", type);
        }
    }

    @Override
    public <T> List<T> findAllById(Class<T> type, String id) {
        try {
            FindsById context = (FindsById) contextForType(type);

            return context.findAllById(type, id);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("id", type);
        }
    }

    @Override
    public <T> T findById(Class<T> type, String id) {
        try {
            FindsById context = (FindsById) contextForType(type);

            return context.findById(type, id);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("id", type);
        }
    }

    @Override
    public <T> List<T> findAllByLinkText(Class<T> type, String linkText) {
        try {
            FindsByLinkText context = (FindsByLinkText) contextForType(type);

            return context.findAllByLinkText(type, linkText);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("link text", type);
        }
    }

    @Override
    public <T> T findByLinkText(Class<T> type, String linkText) {
        try {
            FindsByLinkText context = (FindsByLinkText) contextForType(type);

            return context.findByLinkText(type, linkText);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("link text", type);
        }
    }

    @Override
    public <T> List<T> findAllByName(Class<T> type, String name) {
        try {
            FindsByName context = (FindsByName) contextForType(type);

            return context.findAllByName(type, name);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("name", type);
        }
    }

    @Override
    public <T> T findByName(Class<T> type, String name) {
        try {
            FindsByName context = (FindsByName) contextForType(type);

            return context.findByName(type, name);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("name", type);
        }
    }

    @Override
    public <T> List<T> findAllByNested(Class<T> type, Element parent, Locator child) {
        try {
            FindsByNested context = (FindsByNested) contextForType(type);

            return context.findAllByNested(type, parent, child);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("nested", type);
        }
    }

    @Override
    public <T> T findByNested(Class<T> type, Element parent, Locator child) {
        try {
            FindsByNested context = (FindsByNested) contextForType(type);

            return context.findByNested(type, parent, child);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("nested", type);
        }
    }

    @Override
    public <T> List<T> findAllByPartialTextContent(Class<T> type, String partialTextContent) {
        try {
            FindsByPartialTextContent context = (FindsByPartialTextContent) contextForType(type);

            return context.findAllByPartialTextContent(type, partialTextContent);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("partial text content", type);
        }
    }

    @Override
    public <T> T findByPartialTextContent(Class<T> type, String partialTextContent) {
        try {
            FindsByPartialTextContent context = (FindsByPartialTextContent) contextForType(type);

            return context.findByPartialTextContent(type, partialTextContent);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("partial text content", type);
        }
    }

    @Override
    public <T> List<T> findAllByTextContent(Class<T> type, String textContent) {
        try {
            FindsByTextContent context = (FindsByTextContent) contextForType(type);

            return context.findAllByTextContent(type, textContent);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("text content", type);
        }
    }

    @Override
    public <T> T findByTextContent(Class<T> type, String textContent) {
        try {
            FindsByTextContent context = (FindsByTextContent) contextForType(type);

            return context.findByTextContent(type, textContent);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("text content", type);
        }
    }

    @Override
    public <T> List<T> findAllByXPath(Class<T> type, String xpath) {
        try {
            FindsByXPath context = (FindsByXPath) contextForType(type);

            return context.findAllByXPath(type, xpath);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("XPath", type);
        }
    }

    @Override
    public <T> T findByXPath(Class<T> type, String xpath) {
        try {
            FindsByXPath context = (FindsByXPath) contextForType(type);

            return context.findByXPath(type, xpath);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("XPath", type);
        }
    }

    @Override
    public <T> List<T> findAllByClassName(Class<T> type, String className) {
        try {
            FindsByClassName context = (FindsByClassName) contextForType(type);

            return context.findAllByClassName(type, className);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("HTML class name", type);
        }
    }

    @Override
    public <T> T findByClassName(Class<T> type, String className) {
        try {
            FindsByClassName context = (FindsByClassName) contextForType(type);

            return context.findByClassName(type, className);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("HTML class name", type);
        }
    }

    @Override
    public <T> List<T> findAllByAttribute(Class<T> type, String attribute, String value) {
        try {
            FindsByAttribute context = (FindsByAttribute) contextForType(type);

            return context.findAllByAttribute(type, attribute, value);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("HTML attribute, " + attribute + "'", type);
        }
    }

    @Override
    public <T> T findByAttribute(Class<T> type, String attribute, String value) {
        try {
            FindsByAttribute context = (FindsByAttribute) contextForType(type);

            return context.findByAttribute(type, attribute, value);
        } catch (ClassCastException cce) {
            throw unsupportedLocatorForType("HTML attribute, " + attribute + "'", type);
        }
    }

    @Override
    public <T> List<T> findAllByView(Class<T> type, View view) {
        try {
            FindsByView context = (FindsByView) contextForType(type);

            return context.findAllByView(type, view);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("view, " + view, type);
        }
    }

    @Override
    public <T> T findByView(Class<T> type, View view) {
        try {
            FindsByView context = (FindsByView) contextForType(type);

            return context.findByView(type, view);
        } catch (ClassCastException e) {
            throw unsupportedLocatorForType("view, " + view, type);
        }
    }

    /**
     * Returns the ElementContext or PraentContext depending on if the type of thing we're looking
     * for is an element or a context.
     *
     * @param type The type of thing we're looking for.
     */
    private Context contextForType(Class<?> type) {
        if (Element.class.isAssignableFrom(type)) {
            return elementContext;
        }

        if (Context.class.isAssignableFrom(type)) {
            return parentContext;
        }

        throw new UnsupportedOperationException("WebContexts can only find elements and other "
                + "contexts. Unsupported find type: " + type);
    }

    private RuntimeException unsupportedLocatorForType(String locator, Class<?> type) {
        return new UnsupportedOperationException("Unsupported locator '" + locator + "' for type: "
                + type);
    }
}
