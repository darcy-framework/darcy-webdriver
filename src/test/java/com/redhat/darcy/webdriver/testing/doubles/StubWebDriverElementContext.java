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

package com.redhat.darcy.webdriver.testing.doubles;

import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.webdriver.WebDriverElementContext;

import java.util.List;

public class StubWebDriverElementContext implements WebDriverElementContext {
    @Override
    public <T> List<T> findAllByChained(Class<T> type, Locator... locators) {
        throw new UnsupportedOperationException("findAllByChained");
    }

    @Override
    public <T> T findByChained(Class<T> type, Locator... locators) {
        throw new UnsupportedOperationException("findByChained");
    }

    @Override
    public <T> List<T> findAllByCss(Class<T> type, String css) {
        throw new UnsupportedOperationException("findAllByCss");
    }

    @Override
    public <T> T findByCss(Class<T> type, String css) {
        throw new UnsupportedOperationException("findByCss");
    }

    @Override
    public <T> List<T> findAllByHtmlTag(Class<T> type, String tag) {
        throw new UnsupportedOperationException("findAllByHtmlTag");
    }

    @Override
    public <T> T findByHtmlTag(Class<T> type, String tag) {
        throw new UnsupportedOperationException("findByHtmlTag");
    }

    @Override
    public <T> List<T> findAllById(Class<T> type, String id) {
        throw new UnsupportedOperationException("findAllById");
    }

    @Override
    public <T> T findById(Class<T> type, String id) {
        throw new UnsupportedOperationException("findById");
    }

    @Override
    public <T> List<T> findAllByLinkText(Class<T> type, String linkText) {
        throw new UnsupportedOperationException("findAllByLinkText");
    }

    @Override
    public <T> T findByLinkText(Class<T> type, String linkText) {
        throw new UnsupportedOperationException("findByLinkText");
    }

    @Override
    public <T> List<T> findAllByName(Class<T> type, String name) {
        throw new UnsupportedOperationException("findAllByName");
    }

    @Override
    public <T> T findByName(Class<T> type, String name) {
        throw new UnsupportedOperationException("findByName");
    }

    @Override
    public <T> List<T> findAllByNested(Class<T> type, Element parent, Locator child) {
        throw new UnsupportedOperationException("findAllByNested");
    }

    @Override
    public <T> T findByNested(Class<T> type, Element parent, Locator child) {
        throw new UnsupportedOperationException("findByNested");
    }

    @Override
    public <T> List<T> findAllByPartialTextContent(Class<T> type, String partialTextContent) {
        throw new UnsupportedOperationException("findAllByPartialTextContent");
    }

    @Override
    public <T> T findByPartialTextContent(Class<T> type, String partialTextContent) {
        throw new UnsupportedOperationException("findByPartialTextContent");
    }

    @Override
    public <T> List<T> findAllByTextContent(Class<T> type, String textContent) {
        throw new UnsupportedOperationException("findAllByTextContent");
    }

    @Override
    public <T> T findByTextContent(Class<T> type, String textContent) {
        throw new UnsupportedOperationException("findByTextContent");
    }

    @Override
    public <T> List<T> findAllByXPath(Class<T> type, String xpath) {
        throw new UnsupportedOperationException("findAllByXPath");
    }

    @Override
    public <T> T findByXPath(Class<T> type, String xpath) {
        throw new UnsupportedOperationException("findByXPath");
    }

    @Override
    public <T> List<T> findAllByClassName(Class<T> type, String className) {
        throw new UnsupportedOperationException("findAllByClassName");
    }

    @Override
    public <T> T findByClassName(Class<T> type, String className) {
        throw new UnsupportedOperationException("findByClassName");
    }

    @Override
    public <T> List<T> findAllByAttribute(Class<T> type, String attribute, String value) {
        throw new UnsupportedOperationException("findAllByAttribute");
    }

    @Override
    public <T> T findByAttribute(Class<T> type, String attribute, String value) {
        throw new UnsupportedOperationException("findByAttribute");
    }
}
