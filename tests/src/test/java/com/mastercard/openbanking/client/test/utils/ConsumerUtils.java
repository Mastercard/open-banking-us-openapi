package com.mastercard.openbanking.client.test.utils;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.api.ConsumersApi;
import com.mastercard.openbanking.client.test.ModelFactory;

public class ConsumerUtils {

    public static String getOrCreateDefaultConsumer(ConsumersApi api, String customerId) throws ApiException {
        try  {
            // Try to create a consumer for the given customer id
            var consumer = api.createConsumer(customerId, ModelFactory.newConsumer());
            return consumer.getId();
        } catch (ApiException e) {
            // {"code":11000,"message":"A consumer already exists for customer 5025024821"}
            return api.getConsumerForCustomer(customerId).getId();
        }
    }
}
