package com.redhat.darcy.webdriver.internal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;

@RunWith(JUnit4.class)
public class TargetedWebElementTest {

    @Test
    public void shouldCallGetRectOnUnderlyingWebElement() {
        WebDriver.TargetLocator locator = mock(WebDriver.TargetLocator.class);
        WebDriverTarget target = mock(WebDriverTarget.class);
        TargetedWebElement element = mock(TargetedWebElement.class);
        when(element.getRect()).thenReturn(mock(Rectangle.class));
        TargetedWebElement targetedWebElement = new TargetedWebElement(locator, target, element);

        targetedWebElement.getRect();
        verify(element).getRect();
    }
}
