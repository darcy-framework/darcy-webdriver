package com.redhat.darcy.webdriver;

import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.webdriver.elements.WebDriverElement;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;

/**
 * Fluently describes a {@link com.redhat.darcy.web.api.BrowserFactory} that creates specifically
 * configured {@link org.openqa.selenium.ie.InternetExplorerDriver}.
 */
public class InternetExplorerBrowserFactory extends WebDriverBrowserFactory<InternetExplorerBrowserFactory> {
    private InternetExplorerDriverService service;
    private Capabilities capabilities;
    private Integer port;
    private ElementConstructorMap elementImpls = ElementConstructorMap.defaultMap();

    @Override
    public Browser newBrowser() {
        InternetExplorerDriver driver;
        if (service != null) {
            if (capabilities != null) {
                if (port != null) {
                    driver = new InternetExplorerDriver(service, capabilities, port);
                }
                else {
                    driver = new InternetExplorerDriver(service, capabilities);
                }
            }
            else {
                driver = new InternetExplorerDriver(service);
            }
        }
        else if (capabilities != null) {
            driver = new InternetExplorerDriver(capabilities);
        }
        else {
            driver = new InternetExplorerDriver();
        }
        return makeBrowser(driver, elementImpls);
    }

    public InternetExplorerBrowserFactory capableOf(Capabilities cap) {
        capabilities = cap;
        return this;
    }

    public InternetExplorerBrowserFactory usingService(InternetExplorerDriverService ieds) {
        service = ieds;
        return this;
    }

    public InternetExplorerBrowserFactory usingPort(Integer port) {
        this.port = port;
        return this;
    }

    @Override
    public <E extends WebDriverElement> InternetExplorerBrowserFactory withElementImplementation
            (Class<? super E> type, ElementConstructor<E> constructor) {
        elementImpls.put(type, constructor);
        return this;
    }
}
