# [Fineely Config](http://www.fineely.com/)


The rest interface log collection based on spring aop implementation supports kakfa and openFeign


<!---
[![build (2.x)](https://img.shields.io/github/actions/workflow/status/apache/logging-log4j2/build.yml?branch=2.x&label=build%20%282.x%29)](https://github.com/apache/logging-log4j2/actions/workflows/build.yml)
[![build (3.x)](https://img.shields.io/github/actions/workflow/status/apache/logging-log4j2/build.yml?branch=main&label=build%20%283.x%29)](https://github.com/apache/logging-log4j2/actions/workflows/build.yml)
![CodeQL](https://github.com/apache/logging-log4j2/actions/workflows/codeql-analysis.yml/badge.svg)
-->
[![Maven Central](https://img.shields.io/static/v1?label=maven-central&message=v1.0.0&color=blue)](https://central.sonatype.com/artifact/com.fineely/fineely-log/1.0.1)
[![Maven Central](https://img.shields.io/static/v1?label=KeplerLei&message=CSDN&color=red)](https://blog.csdn.net/leichengjun_510/article/details/129882941)
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

```java
package com.example;

import com.fineelyframework.log.annotation.FineelyLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
public class Example {

    @GetMapping("/hello")
    @FineelyLog(method = RequestMethod.GET, module = "example", url = "/example/hello")
    public String hello(String name) {
        return "Hello: " + name;
    }

    @GetMapping("/name")
    @FineelyLog(method = RequestMethod.GET, module = "example", desc = "${name}")
    public String name(String name) {
        return name;
    }
}
```

In FineelyLog, annotations have a high-level description configuration

```java
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FineelyLog {

    /**
     * Request Method
     */
    RequestMethod[] method() default {};

    /**
     * Module
     */
    String module() default "";

    /**
     * Description
     * Parameter Name ${name} or ${class.name}
     * Common parameters are as follows:
     * 
     * Method returns a result: ${result.**}
     * Method name: ${methodName}
     * Method execution start time: ${startTime}
     * Method execution end time: ${endTime}
     * Courtship parameter: ${request.**}
     * Array matching: ${result.data[0].name}
     */
    String desc() default "";

    /**
     * Request Address
     */
    String url() default "";
}
```

And an `queue` example `application.yml` configuration file:
```yaml
fineely:
  log:
    storage-mode: queue
```

Processing method of the `queue` :

```java
package com.example;

/**
 * fineely.log.storageMode = queue
 */
@Slf4j
@Component
public class LogQueueTask {

    @Scheduled(fixedRate = 5000)
    public void monitorQueueLog(){
        LinkedTransferQueue<MethodLogEntity> oplogQueue = QueueOperator.INSTANCE.getOplogQueue();
        if (!oplogQueue.isEmpty()) {
            // do something
            List<MethodLogEntity> oplogs = new ArrayList<>();
            oplogQueue.drainTo(oplogs);
            for (MethodLogEntity oplog : oplogs) {
                log.info("::::::: log：[{}]", oplog.toString());
            }
        } else {
            log.info("::::::: No log temporarily");
        }
    }
}
```

And an `feign` example `application.yml` configuration file:
```yaml
fineely:
  log:
    storage-mode: feign
    # Choose between the name and url
    # Without eureka, Please use path
    feign:
      # Application Name registered in eureka
      name: example
      url: http://localhost:8895
      path: /test
# If you need eureka, you can add configurations, set fineely.log.feign.name = target spring.application.name
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:1112/eureka/
  instance:
    prefer-ip-address: true
```

Processing method of the `feign` :

```java
public class LogEntity {
    private String[] method;
    private String methodName;
    private String module;
    private String url;
    private String desc;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double timeConsuming;
    private String allParams;
    private String result;
    private String ipAddress;
    private String exceptionInfo;
    private String operator;
    private LocalDateTime createTime;
}

@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * feign mode mapping url is `save`
     */
    @PostMapping("save")
    public boolean saveLog(@RequestBody LogEntity logEntity) {
        System.out.println(myEntity.toString());
        // do something
        return true;
    }
}
```

And an `kafka` example `application.yml` configuration file:
```yaml
fineely:
  log:
    storage-mode: kafka
    kafka:
      kafka-brokers: 192.168.3.190:9092
      topic: test-server
      group-id: e27121ee40c6c6f45f91ab52101b1122
```
Processing method of the `kafka` :

```java
@Configuration
public class KafkaConfig {

    @Value("${kafka_brokers}")
    private String KAFKA_BROKERS;

    @Bean
    @ConditionalOnMissingBean(
            name = {"messageReceiveListener"}
    )
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> messageReceiveListener() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory(this.consumerConfigs()));
        factory.setBatchListener(true);
        factory.getContainerProperties().setPollTimeout(1500L);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConcurrency(4);
        return factory;
    }

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.KAFKA_BROKERS);
        // Other configurations ...
        return configs;
    }

}

/**
 * Kafka listener, listening for log notifications
 */
@Slf4j
@Component
public class KafkaMessageHandler {

    @KafkaListener(
            containerFactory = "messageReceiveListener",
            topics = {"${topic}"},
            groupId = "${group-id}"
    )
    public void consumerCommonMessageNotify(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        try {
            records.forEach(content -> {
                log.info("=====:::::: Start processing message=====");
                // do something
                String message = content.value().toString();
                log.info(String.format(":::::: Receive message content => %s", message));
                log.info("=====:::::: Processing Message End=====");
            });
        } catch (Exception e) {
            log.error(":::::: Message processing error => ", e);
        } finally {
            ack.acknowledge();
        }
    }
}
```

## Senior

Custom Implementation Storage

```java
import com.fineelyframework.log.entity.MethodLogEntity;

/**
 * CustomLogDaoImpl
 * Implement the MethodLogDao interface
 * Yml does not need to add configuration
 */
@Component
public class CustomLogDaoImpl implements MethodLogDao {


    @Override
    public boolean saveLog(MethodLogEntity methodLogEntity) {
        // do something
        return true;
    }

}
```

## Issue Tracking

Issues, bugs, and feature requests should be submitted to [the issue tracker](https://github.com/Big-billed-shark/fineely-log/issues).

Pull requests on GitHub are welcome, but please open a ticket in the issue tracker first, and mention the issue in the pull request.

<!---
## Contributing

We love contributions!
Take a look at [our contributing page](CONTRIBUTING.md).
-->#   f i n e e l y - c o n f i g  
 