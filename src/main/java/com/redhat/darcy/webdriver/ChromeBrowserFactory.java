package com.redhat.darcy.webdriver;

import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.webdriver.elements.WebDriverElement;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;


/**
 * Fluently describes a {@link com.redhat.darcy.web.api.BrowserFactory} that creates specifically
 * configured {@link org.openqa.selenium.chrome.ChromeDriver}.
 */
public class ChromeBrowserFactory extends WebDriverBrowserFactory<ChromeBrowserFactory> {
    private DesiredCapabilities capabilities;
    private ChromeOptions options;
    private ChromeDriverService service;
    private ElementConstructorMap elementImpls = ElementConstructorMap.defaultMap();

    @Override
    public Browser newBrowser() {
        ChromeDriver driver;

        if (service != null) {
            if (options != null) {
                driver = new ChromeDriver(service, options);
            } else if (capabilities != null) {
                driver = new ChromeDriver(service, capabilities);
            } else {
                driver = new ChromeDriver(service);
            }
        } else if (capabilities != null) {
            driver = new ChromeDriver(capabilities);
        } else if (options != null) {
            driver = new ChromeDriver(options);
        } else {
            driver = new ChromeDriver();
        }
        return makeBrowser(driver, elementImpls);
    }

    public ChromeBrowserFactory capableOf(Capabilities cap) {
        capabilities = new DesiredCapabilities(cap, capabilities);
        return this;
    }

    public ChromeBrowserFactory usingOptions(ChromeOptions co) {
        options = co;
        return this;
    }

    public ChromeBrowserFactory usingService(ChromeDriverService cds) {
        service = cds;
        return this;
    }

    @Override
    public <E extends WebDriverElement> ChromeBrowserFactory withElementImplementation(
            Class<? super E> type, ElementConstructor<E> constructor) {
        elementImpls.put(type, constructor);
        return this;
    }
}
