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

import static org.openqa.selenium.WebDriver.Navigation;

import com.redhat.synq.ThrowableUtil;

import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class ThreadedNavigation implements Navigation {
    private final Navigation navigation;
    private final ExecutorService executor;

    public ThreadedNavigation(Navigation navigation, ExecutorService executor) {
        this.navigation = navigation;
        this.executor = executor;
    }

    /**
     * Submits a task and waits for it to be completed.
     */
    private void submitAndWait(Runnable runnable) {
        try {
            executor.submit(runnable).get();
        } catch (InterruptedException e) {
            throw ThrowableUtil.throwUnchecked(e);
        } catch (ExecutionException e) {
            throw ThrowableUtil.throwUnchecked(e.getCause());
        }
    }

    @Override
    public void back() {
        submitAndWait(navigation::back);
    }

    @Override
    public void forward() {
        submitAndWait(navigation::forward);
    }

    @Override
    public void to(String url) {
        submitAndWait(() -> navigation.to(url));
    }

    @Override
    public void to(URL url) {
        submitAndWait(() -> navigation.to(url));
    }

    @Override
    public void refresh() {
        submitAndWait(navigation::refresh);
    }
}
