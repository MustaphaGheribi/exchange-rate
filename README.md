# Exchange Rate Service

## Overview
This service exposes a REST API for currencies exchange rates / conversion based on the reference exchange rate published by https://exchangeratesapi.io/

### List of supported currencies

```
GET /currency
```



### Convert
Converts a given amount from a currency to another: 15 USD = ??? GBP  

```
GET /currency/convert?from={from}&to={to}&amount={amount}
```
* `from` [required] : base currency
* `to` [optional: defaults to `EUR`] : target currency
* `amount` [required] : amount to convert

Response:

```
{
  "label": "USD/EUR",
  "publishedAt": "2020-12-18",
  "amount": 1,
  "result": 0.8157
}
```


## Build

Running the following maven command builds the spring boot application and generates an uber JAR

```
./mvnw clean package
```
Builds a cloud native docker image using `buildpacks`

```
./mvnw spring-boot:build-image
```
Builds a docker image using `docker engine`

```
docker build -t mustapha.io/microservices/exchange-rate-service:0.0.1-SNAPSHOT .
```

## Run
* Using Maven
  
```
./mvnw spring-boot:run
```


* Running docker image

```
docker run -p 8080:8080 --name exchange-rate-service-container mustapha.io/microservices/exchange-rate-service:0.0.1-SNAPSHOT
```
