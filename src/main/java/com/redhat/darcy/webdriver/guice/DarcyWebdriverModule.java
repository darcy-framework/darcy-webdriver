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

package com.redhat.darcy.webdriver.guice;

import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.web.api.BrowserFactory;
import com.redhat.darcy.webdriver.RemoteBrowserFactory;

import com.google.guiceberry.GuiceBerryModule;
import com.google.guiceberry.TestScoped;
import com.google.inject.AbstractModule;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * An extendable module that injects {@link com.redhat.darcy.web.api.Browser} and {@link
 * com.redhat.darcy.web.api.BrowserFactory} instances.
 *
 * <p>Uses {@link com.redhat.darcy.webdriver.guice.BrowserProvider} for browsers, which will close
 * them on teardown.
 *
 * <p>By default, environment variables are used to choose the browser ("darcy.browser" as String
 * like "firefox" or "chrome"), whether or not to run remotely on a Selenium grid
 * ("darcy.webdriver.remote" as boolean), and what grid hub url to use in that case
 * ("darcy.webdriver.hub"). If you wish you can override this behavior by extending this module and
 * overriding {@link #getHubUrl()}, {@link #useRemoteDriver()}, and {@link #getBrowserType()}.
 *
 * <p>You may also wish to add or override element implementations. You can do this by overriding
 * {@link #getBrowserFactory()} and returning {@code super()} with whatever modifications you wish.
 */
public class DarcyWebdriverModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new GuiceBerryModule());

        bind(BrowserFactory.class).toInstance(getBrowserFactory());
        bind(Browser.class).toProvider(BrowserProvider.class).in(TestScoped.class);
    }

    protected BrowserFactory getBrowserFactory() {
        if (useRemoteDriver()) {
            try {
                return new RemoteBrowserFactory(getHubUrl(), getBrowserType().asCapability());
            } catch (MalformedURLException e) {
                addError(e);
                return null;
            }
        } else {
            return getBrowserType().asBrowserFactory();
        }
    }

    protected BrowserType getBrowserType() {
        return Browsers.getBrowserFromEnv();
    }

    protected boolean useRemoteDriver() {
        return Boolean.getBoolean("darcy.webdriver.remote");
    }

    protected URL getHubUrl() throws MalformedURLException {
        return new URL(System.getProperty("darcy.webdriver.hub"));
    }
}
