package com.redhat.darcy.webdriver;

import com.redhat.darcy.webdriver.internal.CachingTargetLocator;
import com.redhat.darcy.webdriver.internal.ForwardingTargetedWebDriver;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeTrue;

public class FirefoxBrowserFactoryTest {
    @Before
    public void checkForDriver() {
        assumeTrue(System.getProperty("java.class.path").contains("firefox-driver"));
    }

    @Test
    public void shouldBeInstanceOfUntargetedFirefoxDriver() {
        WebDriverBrowserFactory browserFactory = new FirefoxBrowserFactory();

        WebDriverBrowser browser = (WebDriverBrowser) browserFactory.newBrowser();
        ForwardingTargetedWebDriver webDriver = (ForwardingTargetedWebDriver) browser.getWrappedDriver();
        CachingTargetLocator targetLocator = (CachingTargetLocator) webDriver.getTargetLocator();

        assertThat(targetLocator.getUntargetedDriver(), instanceOf(FirefoxDriver.class));
        browser.close();
    }
}
