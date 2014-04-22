package com.redhat.darcy.webdriver;

import com.redhat.darcy.web.Browser;
import com.redhat.darcy.web.BrowserFactory;
import com.redhat.darcy.webdriver.internal.CachingTargetedWebDriverFactory;
import com.redhat.darcy.webdriver.internal.DefaultTargetedElementFactoryFactory;
import com.redhat.darcy.webdriver.internal.DefaultWebDriverElementContext;
import com.redhat.darcy.webdriver.internal.TargetedDriverFactory;
import com.redhat.darcy.webdriver.internal.TargetedElementFactory;
import com.redhat.darcy.webdriver.internal.TargetedElementFactoryFactory;
import com.redhat.darcy.webdriver.internal.TargetedWebDriver;
import com.redhat.darcy.webdriver.internal.TargetedWebDriverParentContext;
import com.redhat.darcy.webdriver.internal.WebDriverTarget;
import com.redhat.darcy.webdriver.internal.WebDriverTargets;

import org.openqa.selenium.WebDriver;

public interface WebDriverBrowserFactory extends BrowserFactory {
    /**
     * Boiler plate code to take a freshly minted driver and spit out a Browser.
     * @param driver
     * @return
     */
    default Browser makeBrowserContext(WebDriver driver, ElementConstructorMap constructorMap) {
        WebDriverTarget target = WebDriverTargets.window(driver.getWindowHandle());
        
        TargetedDriverFactory targetedWdFactory = new CachingTargetedWebDriverFactory(driver, target);
        TargetedWebDriver targetedDriver = targetedWdFactory.getTargetedDriver(target);
        
        TargetedElementFactoryFactory elementFactoryFactory =
                new DefaultTargetedElementFactoryFactory(constructorMap);
        TargetedElementFactory elementFactory = 
                elementFactoryFactory.newTargetedElementFactory(targetedDriver);
        
        return new WebDriverBrowserContext(targetedDriver, 
                new TargetedWebDriverParentContext(targetedDriver, targetedWdFactory, elementFactoryFactory),
                new DefaultWebDriverElementContext(targetedDriver, elementFactory));
    }
}
