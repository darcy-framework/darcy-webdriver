package com.redhat.darcy.webdriver;

import com.opera.core.systems.OperaDriver;
import com.redhat.darcy.webdriver.internal.CachingTargetLocator;
import com.redhat.darcy.webdriver.internal.ForwardingTargetedWebDriver;
import com.redhat.darcy.webdriver.testing.rules.TraceTestName;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

public class OperaBrowserFactoryTest {
    @Rule
    public TraceTestName traceTestName = new TraceTestName();

    @Test
    public void shouldBeInstanceOfUntargetedChromeDriver() {
        WebDriverBrowserFactory browserFactory = new OperaBrowserFactory();

        WebDriverBrowser browser = (WebDriverBrowser) browserFactory.newBrowser();
        ForwardingTargetedWebDriver webDriver = (ForwardingTargetedWebDriver) browser.getWrappedDriver();
        CachingTargetLocator targetLocator = (CachingTargetLocator) webDriver.getTargetLocator();

        assertThat(targetLocator.getUntargetedDriver(), instanceOf(OperaDriver.class));
        browser.close();
    }
}
