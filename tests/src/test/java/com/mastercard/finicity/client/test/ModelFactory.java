package com.mastercard.finicity.client.test;

import com.mastercard.finicity.client.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import java.time.OffsetDateTime;

public final class ModelFactory {

    public static Borrower newBorrower(String borrower, String consumerId, String customerId) {
        return new Borrower()
            .consumerId(consumerId)
            .customerId(customerId)
            .type(borrower)
            .optionalConsumerInfo(new ConsumerInfo()
                    .ssn("999999999")
                    .dob(470275200L)
            );
    }

    public static NewCustomer newCustomer() {
        var username = "customer_" + randomStr();
        return new NewCustomer()
                .firstName("John_" + randomStr())
                .lastName("Smith_" + randomStr())
                .username(username);
    }

    // See: https://docs.finicity.com/test-the-apis/#test-the-apis-5
    public static NewConsumer newConsumer() {
        return new NewConsumer()
                .firstName("Homer")
                .lastName("Loanseeke")
                .address("434 W Ascension Way")
                .city("Murray")
                .state("UT")
                .zip("84123")
                .phone("6786786786")
                .ssn("999601111")
                .email("finicity@test.com")
                .birthday(new Birthday().year(1970).month(7).dayOfMonth(4));
    }

    public static Application newApplication() {
        return new Application()
                .appDescription("The app that makes your budgeting experience awesome")
                .appName("AwesomeBudgetApp_" + randomStr())
                .appUrl("https://www.finicity.com/")
                .ownerAddressLine1("434 W Ascension Way")
                .ownerAddressLine2("Suite #200")
                .ownerName("Finicity")
                .ownerPostalCode("84123")
                .ownerState("UT")
                .image("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+CjxzdmcgICAKICAgeG1sbnM6c3ZnPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIKICAgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIgogICB2ZXJzaW9uPSIxLjEiCiAgIHZpZXdCb3g9IjAgMCAwIDAiCiAgIGhlaWdodD0iMCIKICAgd2lkdGg9IjAiPgogICAgPGcvPgo8L3N2Zz4K")
                .ownerCity("Murray")
                .ownerCountry("USA");
    }

    public static PayrollData newPayrollData() {
        return new PayrollData()
                .dob(15983999L) // 1970-07-04
                .ssn("999601111");
    }

    public static PayStatement newPayStatement() {
        return new PayStatement()
                .label("lastPayPeriod")
                .statement("VGhpcyBtdXN0IGJlIGFuIGltYWdl");
    }

    public static String randomStr() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    public static ThirdPartyAccessKeyData newThirdPartyAccessKeyData(String type, String accountId, String customerId, String partnerId) {
        var data = new ThirdPartyAccessKeyData();
        var product = new ThirdPartyAccessProduct();
        var accessPeriod = new ThirdPartyAccessPeriod();

        accessPeriod.setType(type);
        accessPeriod.setStartTime(OffsetDateTime.now());
        accessPeriod.setEndTime(OffsetDateTime.now().plusDays(1));

        product.setProduct("moneyTransferDetails");
        product.setMaxCalls(100);
        product.setAccountId(accountId);
        product.setAccessPeriod(accessPeriod);

        data.setPartnerId(partnerId);
        data.setThirdPartyPartnerId(partnerId);
        data.setCustomerId(customerId);
        data.addProductsItem(product);
        return data;
    }
}
