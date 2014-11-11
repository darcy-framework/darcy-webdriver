package com.redhat.darcy.webdriver;

import com.redhat.darcy.webdriver.internal.CachingTargetLocator;
import com.redhat.darcy.webdriver.internal.ForwardingTargetedWebDriver;
import com.redhat.darcy.webdriver.testing.rules.TraceTestName;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.safari.SafariDriver;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

public class SafariBrowserFactoryTest {
    @Rule
    public TraceTestName traceTestName = new TraceTestName();

    @Test
    public void shouldBeInstanceOfUntargetedSafariDriver() {
        WebDriverBrowserFactory browserFactory = new SafariBrowserFactory();

        WebDriverBrowser browser = (WebDriverBrowser) browserFactory.newBrowser();
        ForwardingTargetedWebDriver webDriver = (ForwardingTargetedWebDriver) browser.getWrappedDriver();
        CachingTargetLocator targetLocator = (CachingTargetLocator) webDriver.getTargetLocator();

        assertThat(targetLocator.getUntargetedDriver(), instanceOf(SafariDriver.class));
        browser.close();
    }
}
