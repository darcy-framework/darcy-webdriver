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

import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.Transition;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.web.By;
import com.redhat.darcy.webdriver.WebDriverElementContext;

import java.util.List;

public class ChainedWebDriverElementContext implements WebDriverElementContext {
    private final Locator parent;
    private final WebDriverElementContext context;

    public ChainedWebDriverElementContext(Locator parent, WebDriverElementContext context) {
        this.parent = parent;
        this.context = context;
    }

    @Override
    public Transition transition() {
        return context.transition();
    }

    @Override
    public <T> List<T> findAllByChained(Class<T> type, Locator... locators) {
        return context.findAllByChained(type, parent, By.chained(locators));
    }

    @Override
    public <T> T findByChained(Class<T> type, Locator... locators) {
        return context.findByChained(type, parent, By.chained(locators));
    }

    @Override
    public WebDriverElementContext withRootLocator(Locator root) {
        return new ChainedWebDriverElementContext(root, this);
    }

    @Override
    public <T> List<T> findAllByNested(Class<T> type, Element parent, Locator child) {
        return context.findAllByChained(type, this.parent, By.nested(parent, child));
    }

    @Override
    public <T> T findByNested(Class<T> type, Element parent, Locator child) {
        return context.findByChained(type, this.parent, By.nested(parent, child));
    }

    @Override
    public WebDriverElementContext withRootElement(Element root) {
        return new NestedWebDriverElementContext(root, this);
    }

    @Override
    public <T> List<T> findAllByCss(Class<T> type, String css) {
        return context.findAllByChained(type, parent, By.css(css));
    }

    @Override
    public <T> T findByCss(Class<T> type, String css) {
        return context.findByChained(type, parent, By.css(css));
    }

    @Override
    public <T> List<T> findAllByHtmlTag(Class<T> type, String tag) {
        return context.findAllByChained(type, parent, By.htmlTag(tag));
    }

    @Override
    public <T> T findByHtmlTag(Class<T> type, String tag) {
        return context.findByChained(type, parent, By.htmlTag(tag));
    }

    @Override
    public <T> List<T> findAllById(Class<T> type, String id) {
        return context.findAllByChained(type, parent, By.id(id));
    }

    @Override
    public <T> T findById(Class<T> type, String id) {
        return context.findByChained(type, parent, By.id(id));
    }

    @Override
    public <T> List<T> findAllByLinkText(Class<T> type, String linkText) {
        return context.findAllByChained(type, parent, By.linkText(linkText));
    }

    @Override
    public <T> T findByLinkText(Class<T> type, String linkText) {
        return context.findByChained(type, parent, By.linkText(linkText));
    }

    @Override
    public <T> List<T> findAllByName(Class<T> type, String name) {
        return context.findAllByChained(type, parent, By.name(name));
    }

    @Override
    public <T> T findByName(Class<T> type, String name) {
        return context.findByChained(type, parent, By.name(name));
    }

    @Override
    public <T> List<T> findAllByPartialTextContent(Class<T> type, String partialTextContent) {
        return context.findAllByChained(type, parent, By.partialTextContent(partialTextContent));
    }

    @Override
    public <T> T findByPartialTextContent(Class<T> type, String partialTextContent) {
        return context.findByChained(type, parent, By.partialTextContent(partialTextContent));
    }

    @Override
    public <T> List<T> findAllByTextContent(Class<T> type, String textContent) {
        return context.findAllByChained(type, parent, By.textContent(textContent));
    }

    @Override
    public <T> T findByTextContent(Class<T> type, String textContent) {
        return context.findByChained(type, parent, By.textContent(textContent));
    }

    @Override
    public <T> List<T> findAllByXPath(Class<T> type, String xpath) {
        return context.findAllByChained(type, parent, By.xpath(xpath));
    }

    @Override
    public <T> T findByXPath(Class<T> type, String xpath) {
        return context.findByChained(type, parent, By.xpath(xpath));
    }

    @Override
    public <T> List<T> findAllByClassName(Class<T> type, String className) {
        return context.findAllByChained(type, parent, By.className(className));
    }

    @Override
    public <T> T findByClassName(Class<T> type, String className) {
        return context.findByChained(type, parent, By.className(className));
    }
}
