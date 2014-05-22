package com.redhat.darcy.webdriver.internal;

import com.redhat.darcy.util.ReflectionUtil;

import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TargetedWebDriverInvocationHandler implements InvocationHandler {
    private final TargetLocator locator;
    private final WebDriverTarget target;
    
    public TargetedWebDriverInvocationHandler(TargetLocator locator, WebDriverTarget target) {
        this.locator = locator;
        this.target = target;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        switch (method.getName()) {
        case "switchTo":
            return locator;
        case "getWebDriverTarget":
            return target;
        case "createTargetedElement":
            return (WebElement) Proxy.newProxyInstance(
                    TargetedWebDriverInvocationHandler.class.getClassLoader(), 
                    ReflectionUtil.getAllInterfaces(args[0]).toArray(new Class[]{}), 
                    new TargetedWebElementInvocationHandler((WebElement) args[0], locator, target));
        case "isPresent":
            try {
                target.switchTo(locator);
                return true;
            } catch (NoSuchWindowException | NoSuchFrameException e) {
                return false;
            }
        }
        
        return method.invoke(target.switchTo(locator), args);
    }
    
}
