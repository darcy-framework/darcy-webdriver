package com.redhat.darcy.webdriver;

import static org.junit.Assert.assertNotNull;

import com.redhat.darcy.web.Cookie;
import com.redhat.darcy.web.SimpleUrlView;
import com.redhat.darcy.web.api.Browser;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@RunWith(JUnit4.class)
public class CookieTest {

    @Test
    public void shouldBeACookie() {
        new Cookie("Fish", "cod", "", "", null, false);
    }

    @Test
    public void shouldValidateAValidCookie() {
        new Cookie("Fish", "cod", "", "", null, false)
                .validate();
    }

    @Test
    public void shouldFailValidationForACookieWithAnIllegalName() {
        new Cookie("Fish;", "cod", "", "", null, false)
                .validate();
    }

    @Test
    public void shouldPrintCookieToString() {
        System.out.println(new Cookie("Fish;", "cod", "", "", LocalDateTime.now(), false)
                .toString());
    }

    @Test
    public void shouldGetCookiesFromGoogle() {
        FirefoxBrowserFactory browserFactory = new FirefoxBrowserFactory();
        Browser browser = browserFactory.newBrowser();

        browser.open(new SimpleUrlView("http://www.google.com")
                .withMatcher(Matchers.containsString("google")::matches))
                .waitUpTo(Duration.of(1, ChronoUnit.MINUTES));

        Set cookies = browser.cookies().getAll();

        browser.close();

        assertNotNull(cookies);
    }
}
