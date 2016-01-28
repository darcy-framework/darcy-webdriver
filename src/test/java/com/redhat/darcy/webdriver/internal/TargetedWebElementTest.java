package com.redhat.darcy.webdriver.internal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TargetedWebElementTest {

    @Test
    public void shouldCallGetRectOnUnderlyingWebElement() {
        WebDriver.TargetLocator targetLocator = mock(WebDriver.TargetLocator.class);
        WebDriverTarget webDriverTarget = mock(WebDriverTarget.class);
        WebElement webElement = mock(WebElement.class);
        when(webElement.getRect()).thenReturn(mock(Rectangle.class));
        TargetedWebElement targetedWebElement = new TargetedWebElement(targetLocator, webDriverTarget, webElement);

        targetedWebElement.getRect();
        verify(webElement.getRect());
    }

}
