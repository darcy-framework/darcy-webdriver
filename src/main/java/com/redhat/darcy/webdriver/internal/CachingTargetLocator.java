/*
 Copyright 2014 Red Hat, Inc. and/or its affiliates.

 This file is part of darcy-webdriver.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.redhat.darcy.webdriver.internal;

import com.redhat.darcy.util.Caching;
import com.redhat.darcy.webdriver.internal.WebDriverTarget;
import com.redhat.darcy.webdriver.internal.WebDriverTargets;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * An implementation of {@link org.openqa.selenium.WebDriver.TargetLocator} that caches the current
 * target, so that attempting to switch to the same target multiple times in a row will make no call
 * to the underlying driver.
 */
public class CachingTargetLocator implements TargetLocator {
    private WebDriverTarget currentTarget;
    private WebDriver driver;

    /**
     * An Alert, if one is currently switched-to. Otherwise, it should be `null`. Which means that
     * if switching to a new target from an Alert, this should be reset to `null`.
     *
     * <p>When Alert is non-null, this is used as a flag to tell the target locator that the current
     * WebDriver target is not actually the current target&mdash;the Alert is instead. The WebDriver
     * target must remain non-null however, as if new targets are switched to while after the alert,
     * they will be switched to within the context of the previous WebDriver target. For example:
     *
     * <pre><code>
     *     driver.switchTo().frame("frame1");
     *     driver.findElement(By.id("alertButton")).click();
     *     driver.switchTo().alert();                   // Previous "frame1" context tracked
     *     driver.switchTo().frame("innerframe");       // Switches to "innerframe" within "frame1"
     *     driver.switchTo().alert().accept();
     *     driver.findElement(By.id("innerButton")); // Searches within "innerframe"
     * </code></pre>
     */
    private Alert alert;

    /**
     * @param driver The original, untargeted driver.
     */
    public CachingTargetLocator(WebDriverTarget currentTarget, WebDriver driver) {
        this.currentTarget = Objects.requireNonNull(currentTarget, "currentTarget");
        this.driver = Objects.requireNonNull(driver, "driver");
    }

    public WebDriver frame(WebDriverTarget parent, int index) {
        return switchTo(WebDriverTargets.frame(parent, index));
    }

    public WebDriver frame(WebDriverTarget parent, String nameOrId) {
        return switchTo(WebDriverTargets.frame(parent, nameOrId));
    }

    public WebDriver frame(WebDriverTarget parent, WebElement frameElement) {
        return switchTo(WebDriverTargets.frame(parent, frameElement));
    }

    @Override
    public WebDriver frame(int index) {
        return frame(currentTarget, index);
    }

    @Override
    public WebDriver frame(String nameOrId) {
        return frame(currentTarget, nameOrId);
    }

    @Override
    public WebDriver frame(WebElement frameElement) {
        return frame(currentTarget, frameElement);
    }

    @Override
    public WebDriver parentFrame() {
        return switchTo(WebDriverTargets.parentOf(currentTarget));
    }

    @Override
    public WebDriver window(String nameOrHandle) {
        return switchTo(WebDriverTargets.window(nameOrHandle));
    }

    @Override
    public WebDriver defaultContent() {
        return switchTo(WebDriverTargets.defaultContent());
    }

    @Override
    public WebElement activeElement() {
        // This doesn't actually affect the driver, and may return different elements upon repeat
        // invocations, so nothing to cache.
        return driver.switchTo().activeElement();
    }

    @Override
    public Alert alert() {
        if (alert == null) {
            // Cache the alert to avoid repeat switches.
            // If alert is switched _from_, it must be nulled out.
            alert = driver.switchTo().alert();
        }

        return alert;
    }

    public WebDriverTarget getCurrentTarget() {
        return currentTarget;
    }

    private WebDriver switchTo(WebDriverTarget newTarget) {
        if (alert == null && newTarget.equals(currentTarget)) {
            // do nothing
        } else {
            newTarget.switchTo(driver.switchTo());
            currentTarget = newTarget;

            // Clear alert cache, if any
            alert = null;
        }

        return driver;
    }
}
