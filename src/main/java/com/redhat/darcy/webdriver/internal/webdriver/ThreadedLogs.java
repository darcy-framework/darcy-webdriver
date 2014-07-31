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

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.Logs;

import java.util.Set;
import java.util.concurrent.ExecutorService;

public class ThreadedLogs extends Threaded implements Logs {
    private final Logs logs;

    public ThreadedLogs(Logs logs, ExecutorService executor) {
        super(executor);

        this.logs = logs;
    }

    @Override
    public LogEntries get(String logType) {
        return submitAndGet(() -> logs.get(logType));
    }

    @Override
    public Set<String> getAvailableLogTypes() {
        return submitAndGet(logs::getAvailableLogTypes);
    }
}
