package com.mastercard.finicity.client.test;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.api.ConsumersApi;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsumerUtils {

    public static String getOrCreateDefaultConsumer(ConsumersApi api, String customerId) throws ApiException {
        try  {
            // Try to create a consumer for the given customer id
            BaseAppKeyAppTokenTest.beforeAll();
            var response = api.createConsumer(customerId, ModelFactory.newConsumer());
            assertEquals(customerId, response.getCustomerId());
            return response.getId();
        } catch (ApiException e) {
            // {"code":11000,"message":"A consumer already exists for customer 5025024821"}
            try {
                return api.getConsumerForCustomer(customerId).getId();
            } catch (ApiException ex) {
                throw ex;
            }
        }
    }
}
