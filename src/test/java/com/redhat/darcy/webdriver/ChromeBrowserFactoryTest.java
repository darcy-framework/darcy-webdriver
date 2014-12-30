package com.redhat.darcy.webdriver;

import com.redhat.darcy.webdriver.internal.CachingTargetLocator;
import com.redhat.darcy.webdriver.internal.ForwardingTargetedWebDriver;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeNotNull;

public class ChromeBrowserFactoryTest {
    @Before
    public void checkForDriver() {
        assumeNotNull(System.getProperty("webdriver.chrome.driver"));
    }

    @Test
    public void shouldBeInstanceOfUntargetedChromeDriver() {
        WebDriverBrowserFactory browserFactory = new ChromeBrowserFactory();

        WebDriverBrowser browser = (WebDriverBrowser) browserFactory.newBrowser();
        ForwardingTargetedWebDriver webDriver = (ForwardingTargetedWebDriver) browser.getWrappedDriver();
        CachingTargetLocator targetLocator = (CachingTargetLocator) webDriver.getTargetLocator();

        assertThat(targetLocator.getUntargetedDriver(), instanceOf(ChromeDriver.class));
        browser.close();
    }
}
