/*
  Modified by Red Hat

  Copyright 2007-2009 Selenium committers

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  */

package com.redhat.darcy.webdriver;

import com.redhat.darcy.web.api.OutputType;

import java.io.File;

public interface ScreenshotOutputType<T> extends OutputType<T>, org.openqa.selenium.OutputType<T> {
    ScreenshotOutputType<String> BASE64 = new ScreenshotOutputType<String>() {
        @Override
        public String convertFromBase64Png(String base64Png) {
            return org.openqa.selenium.OutputType.BASE64.convertFromBase64Png(base64Png);
        }

        @Override
        public String convertFromPngBytes(byte[] png) {
            return org.openqa.selenium.OutputType.BASE64.convertFromPngBytes(png);
        }
    };

    /**
     * Obtain the screenshot as raw bytes.
     */
    ScreenshotOutputType<byte[]> BYTES = new ScreenshotOutputType<byte[]>() {
        @Override
        public byte[] convertFromBase64Png(String base64Png) {
            return org.openqa.selenium.OutputType.BYTES.convertFromBase64Png(base64Png);
        }

        @Override
        public byte[] convertFromPngBytes(byte[] png) {
            return org.openqa.selenium.OutputType.BYTES.convertFromPngBytes(png);
        }
    };

    /**
     * Obtain the screenshot into a temporary file that will be deleted once the JVM exits. It is up
     * to users to make a copy of this file.
     */
    ScreenshotOutputType<File> FILE = new ScreenshotOutputType<File>() {
        @Override
        public File convertFromBase64Png(String base64Png) {
            return org.openqa.selenium.OutputType.FILE.convertFromBase64Png(base64Png);
        }

        @Override
        public File convertFromPngBytes(byte[] png) {
            return org.openqa.selenium.OutputType.FILE.convertFromPngBytes(png);
        }
    };
}
