# finicity-openapi
[![](https://prod-finweb-frontend.s3-us-west-2.amazonaws.com/wp-content/uploads/20210512010711/Finicity_mc-logo-400x162.png)](https://www.finicity.com/)

## Overview

The [OpenAPI](https://swagger.io/specification/) specification for [Finicity APIs](https://docs.finicity.com/), along with a suite of tests using the [Finicity Test Drive](https://signup.finicity.com/).

## Workflows

The following workflows ensure the API specification stays in good shape and can be used to consume the Finicity APIs:

[![](https://github.com/FY-Dev-Relations/finicity-openapi/actions/workflows/swagger-editor.yml/badge.svg)](https://github.com/FY-Dev-Relations/finicity-openapi/actions/workflows/swagger-editor.yml)
[![](https://github.com/FY-Dev-Relations/finicity-openapi/actions/workflows/openapi-generator.yml/badge.svg)](https://github.com/FY-Dev-Relations/finicity-openapi/actions/workflows/openapi-generator.yml)
[![](https://github.com/FY-Dev-Relations/finicity-openapi/actions/workflows/integration.yml/badge.svg)](https://github.com/FY-Dev-Relations/finicity-openapi/actions/workflows/integration.yml)
[![](https://github.com/FY-Dev-Relations/finicity-openapi/actions/workflows/prettier.yml/badge.svg)](https://github.com/FY-Dev-Relations/finicity-openapi/actions/workflows/prettier.yml)

If you see some red, please [contact us](https://www.finicity.com/contact/)!

## Download the Specification
[⭳ finicity.yaml](./finicity.yaml)

## About the Tests
#### Things to Know

* The [test project](./tests) use an API client library generated from the API specification using [OpenAPI Generator](https://openapi-generator.tech/)
* A free [Finicity account](https://signup.finicity.com/) is required to get your **Partner ID**, **Partner Secret** and **Finicity App Key**
* Before running the test, you must:
   * Call `addTestingCustomer` and note the **Customer ID** returned
   * Use `generateConnectUrlV2` to add to this customer all accounts from the test [`profile_09`](https://docs.finicity.com/test-the-apis/#test-the-apis-3)
   * Call `refreshCustomerAccounts`

#### Run Tests Locally

Simply run:
```sh
cd tests
```

And then:
```sh
mvn clean test -DpartnerId=*** -DpartnerSecret=*** -DappKey=*** -DcustomerId=***
```

#### Run Tests In GitHub

1. [Fork this repository](https://github.com/FY-Dev-Relations/finicity-openapi/fork)
2. Go to **Settings** > **Actions secrets**
3. Create new repository secrets: `PARTNER_ID`, `PARTNER_SECRET`, `APP_KEY` and `CUSTOMER_ID`
4. Enable workflows in the **Actions** tab

## Guidelines

When updating the API specification:
1. Ensure it can be rendered without errors in https://editor.swagger.io/
2. Ensure an API client can be generated using [OpenAPI Generator](https://openapi-generator.tech/)
3. Prettify the YAML using:
```sh
npx prettier --write --single-quote finicity.yaml
```
4. Update and/or add tests to the [test project](./tests)

## Support

Contact us here: https://www.finicity.com/contact/