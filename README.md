# open-banking-us-openapi
[![](./res/logo.png)](https://developer.mastercard.com/product/open-banking/#gh-light-mode-only)
[![](./res/logo-dark.png)](https://developer.mastercard.com/product/open-banking/#gh-dark-mode-only)

## Overview

The OpenAPI specification for [Open Banking APIs](https://developer.mastercard.com/open-banking-us/documentation) (🇺🇸), along with a suite of [tests](./tests/src/test/java/com/mastercard/openbanking/client/api) using a generated API client and the Test Drive plan.

## Workflows

The following workflows ensure the API specification stays in good shape and can be used to consume the Open Banking APIs from your application:

[![](https://github.com/Mastercard/open-banking-us-openapi/actions/workflows/prettier.yml/badge.svg)](https://github.com/Mastercard/open-banking-us-openapi/actions/workflows/prettier.yml)
[![](https://github.com/Mastercard/open-banking-us-openapi/actions/workflows/swagger-editor.yml/badge.svg)](https://github.com/Mastercard/open-banking-us-openapi/actions/workflows/swagger-editor.yml)
[![](https://github.com/Mastercard/open-banking-us-openapi/actions/workflows/redoc.yml/badge.svg)](https://github.com/Mastercard/open-banking-us-openapi/actions/workflows/redoc.yml)
[![](https://github.com/Mastercard/open-banking-us-openapi/actions/workflows/openapi-generator.yml/badge.svg)](https://github.com/Mastercard/open-banking-us-openapi/actions/workflows/openapi-generator.yml)
[![](https://github.com/Mastercard/open-banking-us-openapi/actions/workflows/tests.yml/badge.svg)](https://github.com/Mastercard/open-banking-us-openapi/actions/workflows/tests.yml)

## Specification
[YAML ⤓](./openbanking-us.yaml) | [Swagger Editor ⬈](https://editor.swagger.io/?url=https%3A%2F%2Fraw.githubusercontent.com%2FMastercard%2Fopen-banking-us-openapi%2Fmain%2Fopenbanking-us.yaml) | [Redoc ⬈](https://redocly.github.io/redoc/?url=https://raw.githubusercontent.com/Mastercard/open-banking-us-openapi/main/openbanking-us.yaml&nocors)

## Tests
### Things to Know :point_down:

* The [test project](./tests) generates an API client library from the API specification like you would do in a real application (OpenAPI Generator is used for that)
* A [free Mastercard Developers account](https://developer.mastercard.com/product/open-banking) is required to obtain your **Partner ID**, **Partner Secret** and **App Key**:

![project](https://user-images.githubusercontent.com/3964455/221236073-5661d083-0a04-4d46-9710-3c0c8c9e9a6a.gif)

* Before running the tests, you need a **Customer ID**. For that, follow [Welcome Your First Customer](https://mstr.cd/3Z5de0Q) or run the [setup script](./bin/). This script will call:
  * `addTestingCustomer`
  * `generateConnectUrl` ([Connect Application](https://developer.mastercard.com/open-banking-us/documentation/connect/)). Simply open the URL, search for "FinBank Profiles - A" and add to your test customer all accounts from [`profile_03`](https://developer.mastercard.com/open-banking-us/documentation/test-the-apis/#bank-account-profiles).
  * `refreshCustomerAccounts`

<p align="center">
<img src="https://github.com/Mastercard/open-banking-us-openapi/assets/13854193/11235128-67d8-47a6-8b1b-c93e35e590da.gif" width="300px"/>
</p>


### Run Tests Locally

1. Clone this repository
2. Run `cd tests && mvn clean test -DpartnerId=*** -DpartnerSecret=*** -DappKey=*** -DcustomerId=***`

![](https://user-images.githubusercontent.com/3964455/194875163-af06b1a2-f2a2-44fe-a62e-73eb8fa78b35.gif)

### Run Tests in GitHub

1. [Fork this repository](https://github.com/Mastercard/open-banking-us-openapi/fork)
2. Go to _Settings_ > _Secrets_ > _Actions_
3. Create new repository secrets: `PARTNER_ID`, `PARTNER_SECRET` and `APP_KEY`
4. Enable workflows in the _Actions_ tab
5. Click _Run workflow_ under _API Client Tests_. Expected result: :heavy_check_mark:

## What's Next?

The [Open Banking API specification](./openbanking-us.yaml) and a [generated API client](./tests) are the only things you need to call Open Banking APIs from your application. 

* For other software development frameworks and languages, see: OpenAPI Generator > [Generators List](https://openapi-generator.tech/docs/generators).
* You may also be interested in trying our [Postman collection](https://github.com/Mastercard/open-banking-us-postman).

## Guidelines

When updating the Open Banking API specification:
1. Ensure it can be rendered without errors in [Swagger Editor](https://editor.swagger.io/?url=https%3A%2F%2Fraw.githubusercontent.com%2FMastercard%2Fopen-banking-us-openapi%2Fmain%2Fopenbanking-us.yaml) or [Redoc](https://redocly.github.io/redoc/?url=https://raw.githubusercontent.com/Mastercard/open-banking-us-openapi/main/openbanking-us.yaml&nocors)
2. Ensure an API client can be generated using [OpenAPI Generator](https://openapi-generator.tech/)
3. Prettify the YAML using `npx prettier --write --single-quote --prose-wrap always openbanking-us.yaml`
4. Update and/or add tests to the [test project](./tests)
   * Generate new tests by using `true` for `generateApiTests` in the [POM file](./tests/pom.xml)
   * Move the generated classes from `/target/generated-sources/openapi/src/test/` to [`/src`](./tests/src/test/java/com/mastercard/openbanking/client/api)
   * Update the generated test methods
