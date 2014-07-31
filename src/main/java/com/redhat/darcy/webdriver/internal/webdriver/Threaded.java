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

import com.redhat.synq.ThrowableUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public abstract class Threaded {
    protected final ExecutorService executor;

    protected Threaded(ExecutorService executor) {
        this.executor = executor;
    }

    /**
     * Submits a task and waits for it to be completed.
     */
    protected void submitAndWait(Runnable runnable) {
        try {
            executor.submit(runnable).get();
        } catch (InterruptedException e) {
            throw ThrowableUtil.throwUnchecked(e);
        } catch (ExecutionException e) {
            throw ThrowableUtil.throwUnchecked(e.getCause());
        }
    }

    /**
     * Submits a task and waits for it to be completed, returning the result.
     */
    protected <T> T submitAndGet(Callable<T> callable) {
        try {
            return executor.submit(callable).get();
        } catch (InterruptedException e) {
            throw ThrowableUtil.throwUnchecked(e);
        } catch (ExecutionException e) {
            throw ThrowableUtil.throwUnchecked(e.getCause());
        }
    }
}
