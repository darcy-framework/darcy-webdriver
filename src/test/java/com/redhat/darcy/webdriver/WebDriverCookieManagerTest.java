package com.redhat.darcy.webdriver;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.redhat.darcy.web.Cookie;
import com.redhat.darcy.web.api.Browser;
import com.redhat.darcy.webdriver.internal.TargetedWebDriver;
import com.redhat.darcy.webdriver.internal.WebDriverCookieManager;
import com.redhat.darcy.webdriver.testing.doubles.StubWebDriverElementContext;
import com.redhat.darcy.webdriver.testing.doubles.StubWebDriverParentContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RunWith(JUnit4.class)
public class WebDriverCookieManagerTest {

    @Test
    public void shouldTransformDarcyCookieWith2ArgsToSeleniumProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToSeleniumCookie(new Cookie("chocolate4", "chip"));
    }

    @Test
    public void shouldTransformDarcyCookieWith3ArgsToSeleniumProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToSeleniumCookie(new Cookie("chocolate", "chip", ""));
    }

    @Test
    public void shouldTransformDarcyCookieWith4ArgsToSeleniumProperlyWhenExpiryIsNotNull() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToSeleniumCookie(new Cookie("chocolate", "chip", "",
                        LocalDateTime.now().plusDays(1)));
    }

    @Test
    public void shouldTransformDarcyCookieWith4ArgsToSeleniumProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToSeleniumCookie(new Cookie("chocolate", "chip", "", null));
    }

    @Test
    public void shouldTransformDarcyCookieWith5ArgsToSeleniumProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToSeleniumCookie(new Cookie("chocolate", "chip", null, "",
                        LocalDateTime.now().plusDays(1)));
    }

    @Test
    public void shouldTransformDarcyCookieWith6ArgsToSeleniumProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToSeleniumCookie(new Cookie("chocolate", "chip", null, "", null, false));
    }

    @Test
    public void shouldTransformDarcyCookieWith7ArgsToSeleniumProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToSeleniumCookie(new Cookie("chocolate", "chip", null, "", null, false,
                        false));
    }

    @Test
    public void shouldTransformSeleniumCookieWith2ArgsToDarcyProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToDarcyCookie(new org.openqa.selenium.Cookie("chocolate", "chip"));
    }

    @Test
    public void shouldTransformSeleniumCookieWith3ArgsToDarcyProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToDarcyCookie(new org.openqa.selenium.Cookie("chocolate", "chip", ""));
    }

    @Test
    public void shouldTransformSeleniumCookieWith4ArgsToDarcyProperlyWhenExpiryNotNull() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToDarcyCookie(new org.openqa.selenium.Cookie("chocolate", "chip", "",
                        Date.valueOf(LocalDate.now().plusDays(1))));
    }

    @Test
    public void shouldTransformSeleniumCookieWith4ArgsToDarcyProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToDarcyCookie(new org.openqa.selenium.Cookie("chocolate", "chip", "",
                        null));
    }

    @Test
    public void shouldTransformSeleniumCookieWith5ArgsToDarcyProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToDarcyCookie(new org.openqa.selenium.Cookie("chocolate", "chip", null,
                        "", null));
    }

    @Test
    public void shouldTransformSeleniumCookieWith6ArgsToDarcyProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToDarcyCookie(new org.openqa.selenium.Cookie("chocolate", "chip", null,
                        "", null, false));
    }

    @Test
    public void shouldTransformSeleniumCookieWith7ArgsToDarcyProperly() {
        new WebDriverCookieManager(mock(TargetedWebDriver.class))
                .transformToDarcyCookie(new org.openqa.selenium.Cookie("chocolate", "chip", null,
                        "", null, false, false));
    }
}
