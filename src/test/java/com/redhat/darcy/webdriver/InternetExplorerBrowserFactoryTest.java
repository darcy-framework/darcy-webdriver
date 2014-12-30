package com.redhat.darcy.webdriver;

import com.redhat.darcy.webdriver.internal.CachingTargetLocator;
import com.redhat.darcy.webdriver.internal.ForwardingTargetedWebDriver;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.ie.InternetExplorerDriver;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeNotNull;

public class InternetExplorerBrowserFactoryTest {
    @Before
    public void checkForDriver() {
        assumeNotNull(System.getProperty("webdriver.ie.driver"));
    }

    @Test
    public void shouldBeInstanceOfUntargetedInternetExplorerDriver() {
        WebDriverBrowserFactory browserFactory = new InternetExplorerBrowserFactory();

        WebDriverBrowser browser = (WebDriverBrowser) browserFactory.newBrowser();
        ForwardingTargetedWebDriver webDriver = (ForwardingTargetedWebDriver) browser.getWrappedDriver();
        CachingTargetLocator targetLocator = (CachingTargetLocator) webDriver.getTargetLocator();

        assertThat(targetLocator.getUntargetedDriver(), instanceOf(InternetExplorerDriver.class));
        browser.close();
    }
}
