package com.redhat.darcy.webdriver;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.redhat.darcy.ui.DarcyException;
import com.redhat.darcy.ui.FindableNotPresentException;
import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.webdriver.internal.TargetedWebDriver;
import com.redhat.darcy.webdriver.testing.doubles.StubWebDriverElementContext;
import com.redhat.darcy.webdriver.testing.doubles.StubWebDriverParentContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InOrder;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@RunWith(JUnit4.class)
public class TakeScreenshotTest {
    @Test
    public void shouldTakeScreenshotAndWriteToOutputStream() throws IOException {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(),
                new StubWebDriverElementContext());

        byte[] data = new byte[] { 1, 2, 3 };

        when(mockedDriver.getScreenshotAs(OutputType.BYTES))
                .thenReturn(data);

        browser.takeScreenshot(baos);

        assertThat(baos.toByteArray(), equalTo(data));
    }

    @Test
    public void shouldTakeScreenshotAndWriteToOutputStreamThenFlushAndCloseInOrder() throws IOException {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);
        OutputStream outputStream = mock(OutputStream.class);
        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(),
                new StubWebDriverElementContext());

        byte[] data = new byte[] { 1, 2, 3 };

        when(mockedDriver.getScreenshotAs(OutputType.BYTES))
                .thenReturn(data);
        InOrder inOrder = inOrder(outputStream);

        browser.takeScreenshot(outputStream);

        inOrder.verify(outputStream).write(data);
        inOrder.verify(outputStream).flush();
        inOrder.verify(outputStream).close();
    }

    @SuppressWarnings("unchecked")
    @Test(expected = FindableNotPresentException.class)
    public void shouldThrowFindableNotPresentExceptionIfDriverIsNotPresent() {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);
        OutputStream mockedOutputStream = mock(OutputStream.class);
        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(),
                new StubWebDriverElementContext());

        when(mockedDriver.getScreenshotAs(OutputType.BYTES))
                .thenThrow(NoSuchWindowException.class);
        browser.takeScreenshot(mockedOutputStream);
    }

    @Test(expected = DarcyException.class)
    public void shouldThrowDarcyExceptionWhenAnIOExceptionOccurs() throws IOException {
        TargetedWebDriver mockedDriver = mock(TargetedWebDriver.class);
        Browser browser = new WebDriverBrowser(mockedDriver,
                new StubWebDriverParentContext(),
                new StubWebDriverElementContext());

        OutputStream outputStream = mock(OutputStream.class);
        doThrow(new IOException()).when(outputStream).close();

        browser.takeScreenshot(outputStream);
    }
}
