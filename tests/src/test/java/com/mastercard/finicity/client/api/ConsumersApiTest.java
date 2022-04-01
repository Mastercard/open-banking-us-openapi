package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.Consumer;
import com.mastercard.finicity.client.model.CreateConsumerRequest;
import com.mastercard.finicity.client.model.CreateConsumerResponse;
import com.mastercard.finicity.client.model.ModifyConsumerRequest;
import com.mastercard.finicity.client.test.BaseAppKeyAppTokenTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ConsumersApiTest extends BaseAppKeyAppTokenTest {

    private final ConsumersApi api = new ConsumersApi();

    @Test
    @Disabled
    void createConsumerTest() throws ApiException {
        String customerId = null;
        CreateConsumerRequest createConsumerRequest = null;
                CreateConsumerResponse response = api.createConsumer(customerId, createConsumerRequest);
        // TODO: test validations
    }

    @Test
    @Disabled
    void getConsumerTest() throws ApiException {
        String consumerId = null;
                Consumer response = api.getConsumer(consumerId);
        // TODO: test validations
    }

    @Test
    @Disabled
    void getConsumerForCustomerTest() throws ApiException {
        String customerId = null;
                Consumer response = api.getConsumerForCustomer(customerId);
        // TODO: test validations
    }

    @Test
    @Disabled
    void modifyConsumerTest() throws ApiException {
        String consumerId = null;
        ModifyConsumerRequest modifyConsumerRequest = null;
                api.modifyConsumer(consumerId, modifyConsumerRequest);
        // TODO: test validations
    }
}
