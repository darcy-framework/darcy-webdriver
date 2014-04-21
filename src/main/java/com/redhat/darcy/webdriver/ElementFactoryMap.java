package com.redhat.darcy.webdriver;

import org.openqa.selenium.WebElement;

import com.redhat.darcy.ui.elements.Element;

public interface ElementFactoryMap {
    <T extends Element> T getElement(Class<T> type, WebElement source);
    <T extends Element> void registerFactory(Class<T> type, ElementFactory<T> factory);
}
