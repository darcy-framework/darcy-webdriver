package com.redhat.darcy.webdriver.internal;

import com.redhat.darcy.webdriver.ElementConstructorMap;

public class DefaultTargetedElementFactoryFactory implements TargetedElementFactoryFactory {
    private final ElementConstructorMap factoryMap;
    
    public DefaultTargetedElementFactoryFactory(ElementConstructorMap factoryMap) {
        this.factoryMap = factoryMap;
    }
    
    @Override
    public TargetedElementFactory newTargetedElementFactory(TargetedWebDriver driver) {
        return new TargetedElementFactory(driver, factoryMap);
    }
    
}
