# Spring Boot 3 Metrics Logs Traces with Opentelemetry

- [Spring Boot 3 Metrics Logs Traces with Opentelemetry](#spring-boot-3-metrics-logs-traces-with-opentelemetry)
  - [Demo Video](#demo-video)
  - [Steps to instrument Spring Boot 3 application](#steps-to-instrument-spring-boot-3-application)
  - [How to run the demo](#how-to-run-the-demo)
  - [Spring Boot Dashboard Credits](#spring-boot-dashboard-credits)

This project is an example to show how to use the grafana-opentelemetry-starter dependency. This Library will autoconfigure everything to send metrics (with micrometer naming convention), logs and traces to a otlp grpc endpoint. There are also instructions here to setup a sandbox environment of backends to store all the signales using Prometheus, Loki, Tempo & Grafana.

## Demo Video

This video highlights how to easily correlate between metrics logs and traces

[Video](media/demo.mp4)



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
docker network create --driver=bridge --subnet=172.19.0.0/16 --gateway=172.19.0.1 mltnet

docker run -d \
    --name mltprom \
    -h mltprom \
    --network=mltnet \
    -p 9090:9090 \
    prom/prometheus:v2.47.2  --config.file=/etc/prometheus/prometheus.yml --web.enable-remote-write-receiver --enable-feature=exemplar-storage

docker run -d \
    --name mltloki \
    -h mltloki \
    --network=mltnet \
    -p 3100:3100 \
    grafana/loki:2.9.2 -config.file=/etc/loki/local-config.yaml

curl localhost:3100/ready

docker run -d \
    --name mlttempo \
    -h mlttempo \
    --network=mltnet \
    -p 3200:3200 \
    -v $(pwd)/assets/tempo.yaml:/etc/tempo.yaml:ro \
    grafana/tempo:2.3.0 -config.file=/etc/tempo.yaml

curl localhost:3200/ready

docker run -d \
    --name mltotel \
    -h mltotel \
    --network=mltnet \
    -p 4317:4317 \
    -v $(pwd)/assets/otel-collector.yaml:/etc/otel-collector.yaml:ro \
    otel/opentelemetry-collector-contrib:0.88.0 --config=/etc/otel-collector.yaml

docker run -d \
    --name mltgrafana \
    -h mltgrafana \
    --network=mltnet \
    -p 3000:3000 \
    -e GF_AUTH_ANONYMOUS_ENABLED=true \
    -e GF_AUTH_ANONYMOUS_ORG_ROLE=Admin \
    -v $(pwd)/assets/grafana-datasources.yaml:/etc/grafana/provisioning/datasources/datasources.yaml:ro \
    -v $(pwd)/assets/dashboards/:/etc/grafana/provisioning/dashboards/:ro \
    grafana/grafana:10.2.0
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


For restarting everything

```
docker start mltprom  mltloki mlttempo mltotel  mltgrafana
```


## Spring Boot Dashboard Credits

Credits go to @Blueswen for providing the Initial Spring Boot Dashboard
https://github.com/blueswen/spring-boot-observability