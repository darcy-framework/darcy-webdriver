package com.redhat.darcy.webdriver;

import com.opera.core.systems.OperaDriver;
import com.opera.core.systems.OperaProfile;
import com.opera.core.systems.OperaSettings;
import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.webdriver.elements.WebDriverElement;
import org.openqa.selenium.Capabilities;

/**
 * Fluently describes a {@link com.redhat.darcy.web.api.BrowserFactory} that creates specifically
 * configured {@link com.opera.core.systems.OperaDriver}.
 */
public class OperaBrowserFactory extends WebDriverBrowserFactory<OperaBrowserFactory> {
    private Capabilities capabilities;
    private OperaProfile profile;
    private OperaSettings settings;
    private ElementConstructorMap elementImpls = ElementConstructorMap.defaultMap();

    @Override
    public Browser newBrowser() {
        OperaDriver driver;

        if (profile != null) {
            driver = new OperaDriver(profile);
        } else if (capabilities != null) {
            driver = new OperaDriver(capabilities);
        } else if (settings != null) {
            driver = new OperaDriver(settings);
        } else {
            driver = new OperaDriver();
        }
        return makeBrowser(driver, elementImpls);
    }

    public OperaBrowserFactory capableOf(Capabilities cap) {
        capabilities = cap;
        return this;
    }

    public OperaBrowserFactory usingOptions(OperaProfile prf) {
        profile = prf;
        return this;
    }

    public OperaBrowserFactory usingService(OperaSettings set) {
        settings = set;
        return this;
    }

    @Override
    public <E extends WebDriverElement> OperaBrowserFactory withElementImplementation(
            Class<? super E> type, ElementConstructor<E> constructor) {
        elementImpls.put(type, constructor);
        return this;
    }
}
