[![Stories in Ready](https://badge.waffle.io/darcy-framework/darcy-webdriver.png?label=ready&title=Ready)](https://waffle.io/darcy-framework/darcy-webdriver)
darcy-webdriver
===============
[![Build Status](https://travis-ci.org/darcy-framework/darcy-webdriver.svg?branch=master)](https://travis-ci.org/darcy-framework/darcy-webdriver) [![Coverage Status](https://coveralls.io/repos/darcy-framework/darcy-webdriver/badge.png?branch=master)](https://coveralls.io/r/darcy-framework/darcy-webdriver?branch=master)

An implementation of [darcy-ui][1] and [darcy-web][2] that uses [Selenium WebDriver][3] as the automation library backend.

maven
=====

```xml
<dependency>
    <groupId>com.redhat.darcy</groupId>
    <artifactId>darcy-webdriver</artifactId>
    <version>0.1-SNAPSHOT</version>
</dependency>
```

To use snapshot versions, you'll need Sonatype's snapshot repo in your pom or settings.xml.

```xml
<repositories>
    <repository>
        <id>central-snapshots</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        <releases><enabled>false</enabled></releases>
        <snapshots><enabled>true</enabled></snapshots>
    </repository>
</repositories>
```

getting started
===============

Check out the [darcy tutorials][4], which use darcy-webdriver as the implementation.

 [1]: https://github.com/darcy-framework/darcy-ui
 [2]: https://github.com/darcy-framework/darcy-web
 [3]: http://docs.seleniumhq.org/
 [4]: https://www.gitbook.io/book/alechenninger/automating-applications-with-darcy
