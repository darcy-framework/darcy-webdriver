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

import com.redhat.darcy.ui.DarcyException;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.Transition;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.internal.SimpleTransition;
import com.redhat.darcy.webdriver.WebDriverElementContext;
import com.redhat.darcy.webdriver.locators.ByPartialVisibleText;
import com.redhat.darcy.webdriver.locators.ByVisibleText;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class DefaultWebDriverElementContext implements WebDriverElementContext {
    private final WebElementContext context;
    private final ElementFactory elementFactory;
    private final Transition transition;

    public DefaultWebDriverElementContext(SearchContext context, ElementFactory elementFactory) {
        this.context = new WebElementContext(context);
        this.elementFactory = elementFactory;
        this.transition = new SimpleTransition(this);
    }

    public DefaultWebDriverElementContext(SearchContext context, ElementFactory elementFactory,
            Transition transition) {
        this.context = new WebElementContext(context);
        this.elementFactory = elementFactory;
        this.transition = transition;
    }

    @Override
    public Transition transition() {
        return transition;
    }

    @Override
    public <T> List<T> findAllById(Class<T> type, String id) {
        return newElementList(type, () -> context.findAllById(WebElement.class, id));
    }

    @Override
    public <T> T findById(Class<T> type, String id) {
        return newElement(type, () -> context.findById(WebElement.class, id));
    }

    @Override
    public <T> List<T> findAllByName(Class<T> type, String name) {
        return newElementList(type, () -> context.findAllByName(WebElement.class, name));
    }

    @Override
    public <T> T findByName(Class<T> type, String name) {
        return newElement(type, () -> context.findByName(WebElement.class, name));
    }

    @Override
    public <T> List<T> findAllByLinkText(Class<T> type, String linkText) {
        return newElementList(type, () -> context.findAllByLinkText(WebElement.class, linkText));
    }

    @Override
    public <T> T findByLinkText(Class<T> type, String linkText) {
        return newElement(type, () -> context.findByLinkText(WebElement.class, linkText));
    }

    @Override
    public <T> List<T> findAllByTextContent(Class<T> type, String textContent) {
        return newElementList(type,
                () -> context.findAllByTextContent(WebElement.class, textContent));
    }

    @Override
    public <T> T findByTextContent(Class<T> type, String textContent) {
        return newElement(type, () -> context.findByTextContent(WebElement.class, textContent));
    }

    @Override
    public <T> List<T> findAllByPartialTextContent(Class<T> type, String partialTextContent) {
        return newElementList(type,
                () -> context.findAllByPartialTextContent(WebElement.class, partialTextContent));
    }

    @Override
    public <T> T findByPartialTextContent(Class<T> type, String partialTextContent) {
        return newElement(type,
                () -> context.findByPartialTextContent(WebElement.class, partialTextContent));
    }

    @Override
    public <T> List<T> findAllByXPath(Class<T> type, String xpath) {
        return newElementList(type, () -> context.findAllByXPath(WebElement.class, xpath));
    }

    @Override
    public <T> T findByXPath(Class<T> type, String xpath) {
        return newElement(type, () -> context.findByXPath(WebElement.class, xpath));
    }

    @Override
    public <T> List<T> findAllByCss(Class<T> type, String css) {
        return newElementList(type, () -> context.findAllByCss(WebElement.class, css));
    }

    @Override
    public <T> T findByCss(Class<T> type, String css) {
        return newElement(type, () -> context.findByCss(WebElement.class, css));
    }

    @Override
    public <T> List<T> findAllByHtmlTag(Class<T> type, String tag) {
        return newElementList(type, () -> context.findAllByHtmlTag(WebElement.class, tag));
    }

    @Override
    public <T> T findByHtmlTag(Class<T> type, String tag) {
        return newElement(type, () -> context.findByHtmlTag(WebElement.class, tag));
    }

    @Override
    public <T> List<T> findAllByChained(Class<T> type, Locator... locators) {
        return newElementList(type, () -> context.findAllByChained(WebElement.class, locators));
    }

    @Override
    public <T> T findByChained(Class<T> type, Locator... locators) {
        return newElement(type, () -> context.findByChained(WebElement.class, locators));
    }

    @Override
    public <T> List<T> findAllByNested(Class<T> type, Element parent, Locator child) {
        return newElementList(type, () -> context.findAllByNested(WebElement.class, parent, child));
    }

    @Override
    public <T> T findByNested(Class<T> type, Element parent, Locator child) {
        return newElement(type, () -> context.findByNested(WebElement.class, parent, child));
    }

    @Override
    public WebDriverElementContext withRootLocator(Locator root) {
        return new ChainedWebDriverElementContext(root, this);
    }

    @Override
    public WebDriverElementContext withRootElement(Element root) {
        // Reuse the original transition to prevent the view transitioned to from having a context
        // also nested under some root element
        return new NestedWebDriverElementContext(root, this);
    }

    @SuppressWarnings("unchecked")
    private <T> T newElement(Class<T> type, Supplier<WebElement> source) {
        if (!Element.class.isAssignableFrom(type)) {
            throw new DarcyException("An ElementContext can only locate Element types: "
                    + type.toString());
        }

        return (T) elementFactory.newElement((Class<Element>) type, source);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> newElementList(Class<T> type, Supplier<List<WebElement>> source) {
        if (!Element.class.isAssignableFrom(type)) {
            throw new DarcyException("An ElementContext can only locate Element types: "
                    + type.toString());
        }

        return (List<T>) elementFactory.newElementList((Class<Element>) type, source);
    }

    /**
     * Simply converts from Locators to WebElements, or Lists thereof. Meant to be lazily called
     * when actually needing to find an element to work with.
     * <p>
     * <strong>This class only finds WebElements by design. Trying to find other types will throw a
     * class cast exception.</strong>
     * <p>
     * This class implements WebDriverElementContext so that it can be used with chained locators.
     * See {@link #findAllByChained(Class, com.redhat.darcy.ui.api.Locator...)}.
     */
    private class WebElementContext implements WebDriverElementContext {
        private final SearchContext sc;

        WebElementContext(SearchContext sc) {
            this.sc = sc;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> List<T> findAllByChained(Class<T> type, Locator... locators) {
            List<WebElement> elements = null;
            List<WebElement> subElements = new LinkedList<>();

            for (Locator locator : locators) {
                if (elements == null) {
                    elements = locator.findAll(WebElement.class, this);
                } else {
                    for (WebElement element : elements) {
                        subElements.addAll(
                                locator.findAll(WebElement.class, new WebElementContext(element)));
                    }

                    elements.clear();
                    elements.addAll(subElements);
                    subElements.clear();
                }
            }

            return (List<T>) elements;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T findByChained(Class<T> type, Locator... locators) {
            List<WebElement> elements = findAllByChained(WebElement.class, locators);

            if (elements.isEmpty()) {
                throw new NoSuchElementException("No elements found. SearchContext: '" + sc
                        + "', Locators: " + Arrays.toString(locators));
            }

            return (T) elements.get(0);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> List<T> findAllByCss(Class<T> type, String css) {
            return (List<T>) By.cssSelector(css).findElements(sc);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T findByCss(Class<T> type, String css) {
            return (T) By.cssSelector(css).findElement(sc);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> List<T> findAllByHtmlTag(Class<T> type, String tag) {
            return (List<T>) By.tagName(tag).findElements(sc);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T findByHtmlTag(Class<T> type, String tag) {
            return (T) By.tagName(tag).findElement(sc);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> List<T> findAllById(Class<T> type, String id) {
            return (List<T>) By.id(id).findElements(sc);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T findById(Class<T> type, String id) {
            return (T) By.id(id).findElement(sc);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> List<T> findAllByLinkText(Class<T> type, String linkText) {
            return (List<T>) By.linkText(linkText).findElements(sc);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T findByLinkText(Class<T> type, String linkText) {
            return (T) By.linkText(linkText).findElement(sc);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> List<T> findAllByName(Class<T> type, String name) {
            return (List<T>) By.name(name).findElements(sc);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T findByName(Class<T> type, String name) {
            return (T) By.name(name).findElement(sc);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> List<T> findAllByNested(Class<T> type, Element parent, Locator child) {
            if (!(parent instanceof WrapsElement)) {
                throw new DarcyException("Parent element does not wrap a WebElement. Is this "
                        + "element from another automation library?");
            }

            return (List<T>) child.findAll(WebElement.class,
                    new WebElementContext(((WrapsElement) parent).getWrappedElement()));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T findByNested(Class<T> type, Element parent, Locator child) {
            if (!(parent instanceof WrapsElement)) {
                throw new DarcyException("Parent element does not wrap a WebElement. Is this "
                        + "element from another automation library?");
            }

            return (T) child.find(WebElement.class,
                    new WebElementContext(((WrapsElement) parent).getWrappedElement()));
        }

        @Override
        public WebDriverElementContext withRootLocator(Locator root) {
            // TODO
            throw new UnsupportedOperationException();
        }

        @Override
        public WebDriverElementContext withRootElement(Element root) {
            // TODO
            throw new UnsupportedOperationException();
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> List<T> findAllByPartialTextContent(Class<T> type, String partialTextContent) {
            return (List<T>) new ByPartialVisibleText(partialTextContent).findElements(sc);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T findByPartialTextContent(Class<T> type, String partialTextContent) {
            return (T) new ByPartialVisibleText(partialTextContent).findElement(sc);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> List<T> findAllByTextContent(Class<T> type, String textContent) {
            return (List<T>) new ByVisibleText(textContent).findElements(sc);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T findByTextContent(Class<T> type, String textContent) {
            return (T) new ByVisibleText(textContent).findElement(sc);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> List<T> findAllByXPath(Class<T> type, String xpath) {
            return (List<T>) By.xpath(xpath).findElements(sc);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T findByXPath(Class<T> type, String xpath) {
            return (T) By.xpath(xpath).findElement(sc);
        }
    }
}
