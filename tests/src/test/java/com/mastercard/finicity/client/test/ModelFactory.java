package com.mastercard.finicity.client.test;

import com.mastercard.finicity.client.model.*;
import org.apache.commons.lang3.RandomStringUtils;

public final class ModelFactory {

    public static Borrower newBorrower(BorrowerType primary, String consumerId, String customerId) {
        return new Borrower()
            .consumerId(consumerId)
            .customerId(customerId)
            .type(primary)
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

    public static NewConsumer newConsumer() {
        return new NewConsumer()
                .firstName("John_" + randomStr())
                .lastName("Smith_" + randomStr())
                .address("434 W Ascension Way")
                .city("Murray")
                .state("UT")
                .zip("84123")
                .phone("6786786786")
                .ssn("111222333")
                .email("finicity@test.com")
                .birthday(new Birthday().year(1989).month(8).dayOfMonth(13));
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

    public static String randomStr() {
        return RandomStringUtils.randomAlphabetic(10);
    }
}
