package com.redhat.darcy.webdriver.internal;

import com.redhat.darcy.util.ReflectionUtil;

import org.openqa.selenium.WebDriver;

import java.lang.reflect.Proxy;
import java.util.List;

public class CachingTargetedWebDriverFactory implements TargetedDriverFactory {
    private final CachingTargetLocator cachingTargetLocator;
    private final Class<?>[] interfaces;
    
    public CachingTargetedWebDriverFactory(WebDriver untargetedDriver, 
            WebDriverTarget currentTarget) {
        cachingTargetLocator = new CachingTargetLocator(currentTarget, untargetedDriver);
        
        List<Class<?>> interfaces = ReflectionUtil.getAllInterfaces(untargetedDriver);
        interfaces.add(TargetedWebDriver.class);
        
        this.interfaces = interfaces.toArray(new Class[]{});
    }
    
    @Override
    public TargetedWebDriver getTargetedDriver(WebDriverTarget target) {
        return (TargetedWebDriver) Proxy.newProxyInstance(
                CachingTargetedWebDriverFactory.class.getClassLoader(), 
                interfaces,
                new TargetedWebDriverInvocationHandler(cachingTargetLocator, target));
    }
}
