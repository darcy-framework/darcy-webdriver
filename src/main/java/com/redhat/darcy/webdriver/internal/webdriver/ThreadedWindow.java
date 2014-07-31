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

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.ExecutorService;

public class ThreadedWindow extends Threaded implements WebDriver.Window {
    private final WebDriver.Window window;

    protected ThreadedWindow(WebDriver.Window window, ExecutorService executor) {
        super(executor);

        this.window = window;
    }

    @Override
    public void setSize(Dimension targetSize) {
        submitAndWait(() -> window.setSize(targetSize));
    }

    @Override
    public void setPosition(Point targetPosition) {
        submitAndWait(() -> window.setPosition(targetPosition));
    }

    @Override
    public Dimension getSize() {
        return submitAndGet(window::getSize);
    }

    @Override
    public Point getPosition() {
        return submitAndGet(window::getPosition);
    }

    @Override
    public void maximize() {
        submitAndWait(window::maximize);
    }
}
