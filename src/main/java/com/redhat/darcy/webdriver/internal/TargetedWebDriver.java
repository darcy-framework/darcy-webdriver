package com.redhat.darcy.webdriver.internal;

import org.openqa.selenium.WebElement;

import org.openqa.selenium.WebDriver;

public interface TargetedWebDriver extends WebDriver {
    WebDriverTarget getWebDriverTarget();
    
    /**
     * Given a source element that was found within this target, wrap it in a proxy that will always
     * switch to this targeted driver's target before interacting with the element.
     * <P>
     * Note this is implemented in a Proxy by way of {@link TargetedWebDriverInvocationHandler}, so
     * if the signature is changed, that InvocationHandler will need to be updated.
     * @param source
     * @return
     */
    WebElement createTargetedElement(WebElement source);
    
    /**
     * 
     */
    boolean isPresent();
}
