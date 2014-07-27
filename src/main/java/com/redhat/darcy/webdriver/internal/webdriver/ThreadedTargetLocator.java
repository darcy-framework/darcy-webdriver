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

package com.redhat.darcy.webdriver.internal.webdriver;

import static org.openqa.selenium.WebDriver.TargetLocator;

import com.redhat.synq.ThrowableUtil;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class ThreadedTargetLocator implements TargetLocator {
    private final ExecutorService executor;
    private final TargetLocator locator;

    public ThreadedTargetLocator(TargetLocator locator, ExecutorService executor) {
        this.locator = locator;
        this.executor = executor;
    }

    /**
     * Submits a task and waits for it to be completed, returning the result.
     */
    private <T> T submitAndGet(Callable<T> callable) {
        try {
            return executor.submit(callable).get();
        } catch (InterruptedException e) {
            throw ThrowableUtil.throwUnchecked(e);
        } catch (ExecutionException e) {
            throw ThrowableUtil.throwUnchecked(e.getCause());
        }
    }

    @Override
    public WebDriver frame(int index) {
        return submitAndGet(() -> locator.frame(index));
    }

    @Override
    public WebDriver frame(String nameOrId) {
        return submitAndGet(() -> locator.frame(nameOrId));
    }

    @Override
    public WebDriver frame(WebElement frameElement) {
        return submitAndGet(() -> locator.frame(frameElement));
    }

    @Override
    public WebDriver window(String nameOrHandle) {
        return submitAndGet(() -> locator.window(nameOrHandle));
    }

    @Override
    public WebDriver defaultContent() {
        return submitAndGet(locator::defaultContent);
    }

    @Override
    public WebElement activeElement() {
        return submitAndGet(locator::activeElement);
    }

    @Override
    public Alert alert() {
        return submitAndGet(locator::alert);
    }
}
