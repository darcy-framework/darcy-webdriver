package com.redhat.darcy.webdriver;

import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.webdriver.elements.WebDriverElement;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

/**
 * Fluently describes a {@link com.redhat.darcy.web.api.BrowserFactory} that creates specifically
 * configured {@link org.openqa.selenium.safari.SafariDriver}.
 */
public class SafariBrowserFactory extends WebDriverBrowserFactory<SafariBrowserFactory> {
    private Capabilities capabilities;
    private SafariOptions safariOptions;
    private ElementConstructorMap elementImpls = ElementConstructorMap
            .newElementConstructorMapWithDefaults();

    @Override
    public Browser newBrowser() {
        SafariDriver driver;
        if (capabilities != null) {
            driver = new SafariDriver(capabilities);
        }
        else if (safariOptions != null) {
            driver = new SafariDriver(safariOptions);
        }
        else {
            driver = new SafariDriver();
        }
        return makeBrowser(driver, elementImpls);
    }

    public SafariBrowserFactory capableOf(Capabilities cap) {
        capabilities = cap;
        return this;
    }

    public SafariBrowserFactory usingOptions(SafariOptions options) {
        safariOptions = options;
        return this;
    }

    @Override
    public <E extends WebDriverElement> SafariBrowserFactory withElementImplementation(Class<?
            super E> type, ElementConstructor<E> constructor) {
        elementImpls.put(type, constructor);
        return this;
    }
}
