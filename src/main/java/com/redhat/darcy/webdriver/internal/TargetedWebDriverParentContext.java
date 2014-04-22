package com.redhat.darcy.webdriver.internal;

import com.redhat.darcy.DarcyException;
import com.redhat.darcy.web.Browser;
import com.redhat.darcy.web.FrameContext;
import com.redhat.darcy.webdriver.WebDriverBrowserContext;
import com.redhat.darcy.webdriver.WebDriverParentContext;

import java.util.ArrayList;
import java.util.List;

public class TargetedWebDriverParentContext implements WebDriverParentContext {
    private final TargetedWebDriver driver;
    private final TargetedDriverFactory targetedWdFactory;
    private final TargetedElementFactoryFactory elementFactoryFactory;
    
    public TargetedWebDriverParentContext(TargetedWebDriver driver, 
            TargetedDriverFactory targetedWdFactory, 
            TargetedElementFactoryFactory elementFactoryFactory) {
        this.driver = driver;
        this.targetedWdFactory = targetedWdFactory;
        this.elementFactoryFactory = elementFactoryFactory;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllById(Class<T> type, String name) {
        List<T> found = new ArrayList<>(1);
        WebDriverTarget target;
        
        if (Browser.class.isAssignableFrom(type)) {
            target = WebDriverTargets.window(name);
        } else if (FrameContext.class.isAssignableFrom(type)) {
            target = WebDriverTargets.frame(driver.getWebDriverTarget(), name);
        } else {
            // TODO: check if viewcontext
            throw new DarcyException("Cannot find Contexts of type: " + type);
        }
        
        TargetedWebDriver targetedDriver = targetedWdFactory.getTargetedDriver(target);
        Browser newBrowser = new WebDriverBrowserContext(targetedDriver, 
                new TargetedWebDriverParentContext(targetedWdFactory.getTargetedDriver(target), 
                        targetedWdFactory, elementFactoryFactory),
                new DefaultWebDriverElementContext(targetedDriver, 
                        elementFactoryFactory.newTargetedElementFactory(targetedDriver)));
        
        found.add((T) newBrowser);
        
        return found;
    }
}
