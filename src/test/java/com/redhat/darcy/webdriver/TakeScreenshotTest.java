package com.redhat.darcy.webdriver;

import static org.mockito.Mockito.verify;

import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.webdriver.internal.TargetedWebDriver;
import com.redhat.darcy.webdriver.testing.doubles.StubWebDriverElementContext;
import com.redhat.darcy.webdriver.testing.doubles.StubWebDriverParentContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.OutputType;

@RunWith(MockitoJUnitRunner.class)
public class TakeScreenshotTest {
    @Mock
    TargetedWebDriver mockedDriver;

    Browser browser;

    @Before
    public void setupBrowser() {
        if (browser == null) {
            browser = new WebDriverBrowser(mockedDriver,
                    new StubWebDriverParentContext(),
                    new StubWebDriverElementContext());
        }
    }

    @Test
    public void shouldTakeScreenshot() {
        browser.takeScreenshot();
        verify(mockedDriver).getScreenshotAs(OutputType.FILE);
    }
}