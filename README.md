# spring-batch-housekeeping

[![CI](https://github.com/pekam-software/spring-batch-housekeeping/actions/workflows/gradle.yml/badge.svg)](https://github.com/pekam-software/spring-batch-housekeeping/actions/workflows/gradle.yml)
[![CodeQL](https://github.com/pekam-software/spring-batch-housekeeping/actions/workflows/codeql.yml/badge.svg)](https://github.com/pekam-software/spring-batch-housekeeping/actions/workflows/codeql.yml)
[![Publish](https://github.com/pekam-software/spring-batch-housekeeping/actions/workflows/publish.yml/badge.svg)](https://github.com/pekam-software/spring-batch-housekeeping/actions/workflows/publish.yml)

A small library containing useful code for Spring Batch housekeeping making its performance better.

## Spring Batch Metadata Tables Housekeeping

### Summary

This part of the library contains a code that can be used for the Spring Batch metadata tables cleaning. Spring Batch is 
not doing this by default so the tables are constantly growing in size having eventually a significant impact for the 
performance of the app or microservice. In order to deal with this situation this library is providing a configurable 
service that can be used to clean the metadata tables as well as the annotation that enables a scheduler that will be 
cleaning the mentioned tables according to the configurable CRON expression. The scheduler is safe to be used in 
multi-instance environment as it is using a shared lock mechanism to prevent being run by the multiple instances of the 
app or microservice.

### Prerequisites

To make the cleaning functionality run smoothly and without problems the following prerequisites have to be fulfilled:

- Spring Batch has to be in the version 5,
- you have to add to your app or microservice a db migration to create a shedlock (shared lock) table (example in 
  postgreSQL):
  ```sql
  CREATE TABLE IF NOT EXISTS shedlock (
    name       VARCHAR(64),
    lock_until TIMESTAMP(3) NULL,
    locked_at  TIMESTAMP(3) NULL,
    locked_by  VARCHAR(255),
    PRIMARY KEY(name)
  );
  ```