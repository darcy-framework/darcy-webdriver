package com.redhat.darcy.webdriver.internal;

import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TargetedWebElementInvocationHandler implements InvocationHandler {
    private final WebElement element;
    private final TargetLocator locator;
    private final WebDriverTarget target;
    
    public TargetedWebElementInvocationHandler(WebElement element, TargetLocator locator, 
            WebDriverTarget target) {
        this.element = element;
        this.locator = locator;
        this.target = target;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        target.switchTo(locator);
        
        return method.invoke(element, args);
    }
}
