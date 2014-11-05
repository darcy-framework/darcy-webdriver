package com.redhat.darcy.webdriver;

import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.webdriver.elements.WebDriverElement;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Fluently describes a {@link com.redhat.darcy.web.api.BrowserFactory} that creates specifically
 * configured {@link org.openqa.selenium.htmlunit.HtmlUnitDriver}.
 */
public class HtmlUnitBrowserFactory extends WebDriverBrowserFactory<HtmlUnitBrowserFactory> {
    private Capabilities capabilities;
    private BrowserVersion version;
    private Boolean enableJavascript;
    private ElementConstructorMap elementImpls = ElementConstructorMap
            .newElementConstructorMapWithDefaults();

    @Override
    public Browser newBrowser() {
        HtmlUnitDriver driver;

        if (capabilities != null) {
            driver = new HtmlUnitDriver(capabilities);
        }
        else if (version != null){
            driver = new HtmlUnitDriver(version);
        }
        else if (enableJavascript != null) {
            driver = new HtmlUnitDriver(enableJavascript);
        }
        else {
            driver = new HtmlUnitDriver();
        }
        return makeBrowser(driver, elementImpls);
    }

    public HtmlUnitBrowserFactory capableOf(Capabilities cap){
        capabilities = cap;
        return this;
    }

    public HtmlUnitBrowserFactory usingVersion(BrowserVersion ver){
        version = ver;
        return this;
    }

    public HtmlUnitBrowserFactory enablingJavascript(Boolean enableJavascript) {
        this.enableJavascript = enableJavascript;
        return this;
    }

    @Override
    public <E extends WebDriverElement> HtmlUnitBrowserFactory withElementImplementation(Class<?
            super E> type, ElementConstructor<E> constructor) {
        elementImpls.put(type, constructor);
        return this;
    }
}
