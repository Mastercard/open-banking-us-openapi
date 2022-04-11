package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.Consumer;
import com.mastercard.finicity.client.model.CreateConsumerRequest;
import com.mastercard.finicity.client.model.CreateConsumerResponse;
import com.mastercard.finicity.client.model.ModifyConsumerRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class ConsumerApiTest {

    private final ConsumerApi api = new ConsumerApi();

    @Test
    public void createConsumerTest() throws ApiException {
        String customerId = null;
        CreateConsumerRequest createConsumerRequest = null;
                CreateConsumerResponse response = api.createConsumer(customerId, createConsumerRequest);
        // TODO: test validations
    }

    @Test
    public void getConsumerTest() throws ApiException {
        String consumerId = null;
                Consumer response = api.getConsumer(consumerId);
        // TODO: test validations
    }

    @Test
    public void getConsumerForCustomerTest() throws ApiException {
        String customerId = null;
                Consumer response = api.getConsumerForCustomer(customerId);
        // TODO: test validations
    }

    @Test
    public void modifyConsumerTest() throws ApiException {
        String consumerId = null;
        ModifyConsumerRequest modifyConsumerRequest = null;
                api.modifyConsumer(consumerId, modifyConsumerRequest);
        // TODO: test validations
    }
}
