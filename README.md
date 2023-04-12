# [Fineely Config](http://www.fineely.com/)


A Lightweight System Configuration Framework

<!---
[![build (2.x)](https://img.shields.io/github/actions/workflow/status/apache/logging-log4j2/build.yml?branch=2.x&label=build%20%282.x%29)](https://github.com/apache/logging-log4j2/actions/workflows/build.yml)
[![build (3.x)](https://img.shields.io/github/actions/workflow/status/apache/logging-log4j2/build.yml?branch=main&label=build%20%283.x%29)](https://github.com/apache/logging-log4j2/actions/workflows/build.yml)
![CodeQL](https://github.com/apache/logging-log4j2/actions/workflows/codeql-analysis.yml/badge.svg)
-->
<!---([![Maven Central]&#40;https://img.shields.io/static/v1?label=KeplerLei&message=CSDN&color=red&#41;]&#40;https://blog.csdn.net/leichengjun_510/article/details/129882941&#41;)-->
[![Maven Central](https://img.shields.io/static/v1?label=maven-central&message=v1.0.3&color=blue)](https://mavenlibs.com/maven/dependency/com.fineely/fineely-config)
![Libraries.io dependency status for GitHub repo](https://img.shields.io/static/v1?label=dependencies&message=update&color=g)

## Pull Requests on Github

By sending a pull request, you grant KeplerLei sufficient permissions to use and publish the work submitted under the KeplerLei license.

## Getting Started

`fineely-config` is available at the Central `Maven` Repository. Maven users add this to your `POM`.

```xml
<dependency>
    <groupId>com.fineely</groupId>
    <artifactId>fineely-config</artifactId>
    <version>1.0.0</version>
</dependency>
```
Gradle users add this to your `build.gradle`.
```groovy
implementation 'com.fineely:fineely-config:1.0.0'
```

## Usage

Basic usage of the `fineely-config` :

add annotations `@EnableAutoConfigScan`

```java
@EnableAutoConfigScan({"com.example"}) // configure the package path of the class
@SpringBootApplication(scanBasePackages = {"com.example"})
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
```

Implementing the `ConfigSupport` interface.
Will automatically generate `get[class name]` and `update[class name]`.

```java
package com.example;

import com.fineelyframework.config.core.entity.ConfigSupport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SystemConfig implements ConfigSupport {

    private String code = "example";
    
    // Currently, only basic data types are supported

}
```

And an `jpa` example `application.yml` configuration file:
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
fineely:
  config:
    datasource: jpa
```

And an `mybatis` example `application.yml` configuration file:
```yaml
fineely:
  config:
    datasource: mybatis
```

But `mybatis` does not automatically generate tables, you can use third-party tools or execute SQL

```sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
CREATE TABLE IF
    NOT EXISTS `config` (
    `CONFIG_ID` INT ( 11 ) NOT NULL AUTO_INCREMENT COMMENT 'CONFIG_ID',
    `CONFIG_CODE` VARCHAR ( 64 ) CHARACTER
    SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Configuration encoding',
    `LAST_MODIFY_TIME` datetime DEFAULT NULL COMMENT 'Modification time',
    `CONFIG_VALUE` text CHARACTER
    SET utf8 COLLATE utf8_general_ci COMMENT 'Configuration values',
    `CONFIG_CATEGORY` VARCHAR ( 32 ) CHARACTER
    SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Configuration Category',
    PRIMARY KEY ( `CONFIG_ID` ) USING BTREE
    ) ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 ROW_FORMAT = DYNAMIC COMMENT = 'Basic Configuration Table';
SET FOREIGN_KEY_CHECKS = 1;
```

Access after project launch

```text
GET  http://localhost:port/rest/config/getSystemConfig
POST http://localhost:port/rest/config/updateSystemConfig
```

## Senior

If you don't want to use `/rest/config/` as a prefix 

```java
// Can modify requestMapping 
@EnableConfigScan(basePackage = "***", requestMapping = "/rest/config/")
```

## Issue Tracking

Issues, bugs, and feature requests should be submitted to [the issue tracker](https://github.com/Big-billed-shark/fineely-config/issues).

Pull requests on GitHub are welcome, but please open a ticket in the issue tracker first, and mention the issue in the pull request.