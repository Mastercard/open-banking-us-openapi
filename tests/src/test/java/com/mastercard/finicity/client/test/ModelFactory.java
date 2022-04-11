package com.mastercard.finicity.client.test;

import com.mastercard.finicity.client.model.Borrower;
import com.mastercard.finicity.client.model.BorrowerType;
import com.mastercard.finicity.client.model.ConsumerInfo;
import com.mastercard.finicity.client.model.NewCustomer;
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

    public static String randomStr() {
        return RandomStringUtils.randomAlphabetic(10);
    }
}
