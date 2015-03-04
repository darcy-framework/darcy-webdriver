package com.redhat.darcy.webdriver.internal;

import com.redhat.darcy.web.Cookie;
import com.redhat.darcy.web.api.CookieManager;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

public class WebDriverCookieManager implements CookieManager{
    private TargetedWebDriver driver;

    public WebDriverCookieManager(TargetedWebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void add(Cookie cookie) {
        cookie.validate();

        driver.manage().addCookie(transformToSeleniumCookie(cookie));
    }

    @Override
    public void delete(Cookie cookie) {
        driver.manage().deleteCookie(transformToSeleniumCookie(cookie));
    }

    @Override
    public void deleteAll() {
        driver.manage().deleteAllCookies();
    }

    @Override
    public Set<Cookie> getAll() {
        return driver.manage().getCookies().stream()
                .map(this::transformToDarcyCookie).collect(Collectors.toSet());
    }

    @Override
    public Cookie get(Cookie cookie) {
        return getNamed(cookie.getName());
    }

    @Override
    public Cookie getNamed(String name) {
        return transformToDarcyCookie(driver.manage().getCookieNamed(name));
    }

    private org.openqa.selenium.Cookie transformToSeleniumCookie(Cookie cookie) {
        // From java.time.LocalDateTime to java.util.Date
        Date legacyDate = Date.from(cookie.getExpiry().atZone(ZoneId.systemDefault()).toInstant());

        return new org.openqa.selenium.Cookie(cookie.getName(),
                cookie.getValue(), cookie.getDomain(), cookie.getPath(), legacyDate,
                cookie.isSecure(), cookie.isHttpOnly());
    }

    private Cookie transformToDarcyCookie(org.openqa.selenium.Cookie cookie) {
        // From java.util.Date to java.time.LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.ofInstant(cookie.getExpiry().toInstant(),
                ZoneId.systemDefault());

        return new Cookie(cookie.getName(), cookie.getValue(), cookie.getDomain(),
                cookie.getPath(), localDateTime, cookie.isSecure(), cookie.isHttpOnly());
    }
}
