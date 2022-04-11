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

    public static String randomStr() {
        return RandomStringUtils.randomAlphabetic(10);
    }
}
