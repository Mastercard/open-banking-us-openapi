# finicity-openapi
[![](./res/logo.png)](https://www.finicity.com/#gh-light-mode-only)
[![](./res/logo-dark.png)](https://www.finicity.com/#gh-dark-mode-only)

## Overview

The OpenAPI specification for [Finicity APIs](https://docs.finicity.com/) (🇺🇸), along with a suite of [tests](./tests/src/test/java/com/mastercard/finicity/client/api) using a generated API client and the [Finicity Test Drive](https://signup.finicity.com/).

## Workflows

The following workflows ensure the API specification stays in good shape and can be used to consume the Finicity APIs from your application:

[![](https://github.com/Mastercard/finicity-openapi/actions/workflows/prettier.yml/badge.svg)](https://github.com/Mastercard/finicity-openapi/actions/workflows/prettier.yml)
[![](https://github.com/Mastercard/finicity-openapi/actions/workflows/swagger-editor.yml/badge.svg)](https://github.com/Mastercard/finicity-openapi/actions/workflows/swagger-editor.yml)
[![](https://github.com/Mastercard/finicity-openapi/actions/workflows/redoc.yml/badge.svg)](https://github.com/Mastercard/finicity-openapi/actions/workflows/redoc.yml)
[![](https://github.com/Mastercard/finicity-openapi/actions/workflows/openapi-generator.yml/badge.svg)](https://github.com/Mastercard/finicity-openapi/actions/workflows/openapi-generator.yml)
[![](https://github.com/Mastercard/finicity-openapi/actions/workflows/tests.yml/badge.svg)](https://github.com/Mastercard/finicity-openapi/actions/workflows/tests.yml)

## Specification
[YAML ⤓](./finicity.yaml) | [Swagger Editor ⬈](https://editor.swagger.io/?url=https%3A%2F%2Fraw.githubusercontent.com%2FFY-Dev-Relations%2Ffinicity-openapi%2Fmain%2Ffinicity.yaml) | [Redoc ⬈](https://redocly.github.io/redoc/?url=https://raw.githubusercontent.com/Mastercard/finicity-openapi/main/finicity.yaml&nocors)

## Tests
### Things to Know :point_down:

* The [test project](./tests) generates an API client library from the API specification like you would do in a real application (OpenAPI Generator is used for that)
* A [free Open Banking account](https://signup.finicity.com/) is required to obtain your **Partner ID**, **Partner Secret** and **App Key**:

![project](https://user-images.githubusercontent.com/3964455/221236073-5661d083-0a04-4d46-9710-3c0c8c9e9a6a.gif)

* Before running the tests, you need a **Customer ID**. For that, run [setup.sh](./bin/setup.sh) and use the output of the script in the next sections. This script will call:
  * `addTestingCustomer`
  * `generateConnectUrl` ([Finicity Connect](https://docs.finicity.com/)). Simply open the URL, search for "FinBank Profiles - A" and add to your test customer all accounts from [`profile_03`](https://docs.finicity.com/test-the-apis/#bank-account-profiles).
  * `refreshCustomerAccounts`

<p align="center">
<img src="https://user-images.githubusercontent.com/3964455/195069664-638aa443-d87d-48ea-94fc-167f2ebfff57.gif" width="300px"/>
</p>

### Run Tests Locally

1. Clone this repository
2. Run `cd tests && mvn clean test -DpartnerId=*** -DpartnerSecret=*** -DappKey=*** -DcustomerId=***`

![](https://user-images.githubusercontent.com/3964455/194875163-af06b1a2-f2a2-44fe-a62e-73eb8fa78b35.gif)

### Run Tests in GitHub

1. [Fork this repository](https://github.com/Mastercard/finicity-openapi/fork)
2. Go to _Settings_ > _Secrets_ > _Actions_
3. Create new repository secrets: `PARTNER_ID`, `PARTNER_SECRET`, `APP_KEY` and `CUSTOMER_ID`
4. Enable workflows in the _Actions_ tab
5. Click _Run workflow_ under _API Client Tests_. Expected result: :heavy_check_mark:

## What's Next?

The [Finicity API specification](./finicity.yaml) and a [generated API client](./tests) are the only things you need to call Finicity from your application. 

* For other software development frameworks and languages, see: OpenAPI Generator > [Generators List](https://openapi-generator.tech/docs/generators).
* You may also be interested in trying our [Postman collection](https://github.com/Mastercard/finicity-postman).

## Guidelines

When updating the Finicity API specification:
1. Ensure it can be rendered without errors in [Swagger Editor](https://editor.swagger.io/?url=https%3A%2F%2Fraw.githubusercontent.com%2FFY-Dev-Relations%2Ffinicity-openapi%2Fmain%2Ffinicity.yaml) or [Redoc](https://redocly.github.io/redoc/?url=https://raw.githubusercontent.com/Mastercard/finicity-openapi/main/finicity.yaml&nocors)
2. Ensure an API client can be generated using [OpenAPI Generator](https://openapi-generator.tech/)
3. Prettify the YAML using `npx prettier --write --single-quote --prose-wrap always finicity.yaml`
4. Update and/or add tests to the [test project](./tests)
   * Generate new tests by using `true` for `generateApiTests` in the [POM file](./tests/pom.xml)
   * Move the generated classes from `/target/generated-sources/openapi/src/test/` to [`/src`](./tests/src/test/java/com/mastercard/finicity/client/api)
   * Update the generated test methods
