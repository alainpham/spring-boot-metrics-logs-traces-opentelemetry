# Spring Boot 3 Metrics Logs Traces with Opentelemetry

- [Spring Boot 3 Metrics Logs Traces with Opentelemetry](#spring-boot-3-metrics-logs-traces-with-opentelemetry)
  - [Demo Video](#demo-video)
  - [Steps to instrument Spring Boot 3 application](#steps-to-instrument-spring-boot-3-application)
  - [How to run the demo](#how-to-run-the-demo)
  - [Spring Boot Dashboard Credits](#spring-boot-dashboard-credits)

This project is an example to show how to use the grafana-opentelemetry-starter dependency. This Library will autoconfigure everything to send metrics (with micrometer naming convention), logs and traces to a otlp grpc endpoint. There are also instructions here to setup a sandbox environment of backends to store all the signales using Prometheus, Loki, Tempo & Grafana.

## Demo Video

This video highlights how to easily correlate between metrics logs and traces

![demo](media/output.gif)



## Steps to instrument Spring Boot 3 application

1. On your Spring boot 3.1.+ project add the following maven dependency : 

```
<dependency>
    <groupId>com.grafana</groupId>         
    <artifactId>grafana-opentelemetry-starter</artifactId>
    <version>1.3.2</version>
</dependency>
```

2. Add the following properties

```
grafana.otlp.onprem.endpoint=http://localhost:4317
grafana.otlp.onprem.protocol=grpc
grafana.otlp.globalAttributes.deployment.environment=dev
grafana.otlp.globalAttributes.service.namespace=spring-boot-metrics-logs-traces-opentelemetry-namespace
grafana.otlp.globalAttributes.service.instance.id=spring-boot-metrics-logs-traces-opentelemetry-01
grafana.otlp.globalAttributes.service.version=1.0-SNAPSHOT
```

to influence metric send interval on can set the following environment variable in milliseconds.
```
export OTEL_METRIC_EXPORT_INTERVAL=15000
```

## How to run the demo

```
cd observability
./start-o11y-dev-stack.sh
```

Run this project

```
mvn spring-boot:run
```

Launch continuons testing

```
while sleep 2; do curl localhost:8090/hello; done
```

Go to dashboard : 

http://localhost:3000/d/spring-boot-observability/spring-boot-observability?orgId=1&refresh=5s


For tearing everything down 

```
cd observability
./teardown-o11y-dev-stack.sh
```

## Spring Boot Dashboard Credits

Credits go to @Blueswen for providing the Initial Spring Boot Dashboard
https://github.com/blueswen/spring-boot-observability