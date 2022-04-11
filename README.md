# finicity-openapi
[![](https://api-reference.finicity.com/custom/img/fin-developer-logo.png)](https://www.finicity.com/)

[![](https://github.com/jaaufauvre/finicity-openapi/actions/workflows/swagger-editor.yml/badge.svg)](https://github.com/jaaufauvre/finicity-openapi/actions/workflows/swagger-editor.yml)
[![](https://github.com/jaaufauvre/finicity-openapi/actions/workflows/openapi-generator.yml/badge.svg)](https://github.com/jaaufauvre/finicity-openapi/actions/workflows/openapi-generator.yml)
[![](https://github.com/jaaufauvre/finicity-openapi/actions/workflows/prettier.yml/badge.svg)](https://github.com/jaaufauvre/finicity-openapi/actions/workflows/prettier.yml)

## Overview
[finicity.yaml](./finicity.yaml) is the OpenAPI specification for Finicity’s APIs.

## Guidelines

When updated, the YAML content must be prettified using:
```sh
npx prettier --write --single-quote finicity.yaml
```
