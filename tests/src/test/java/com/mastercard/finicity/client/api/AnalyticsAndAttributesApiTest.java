package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.test.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AnalyticsAndAttributesApiTest extends BaseTest {

    private final AnalyticsAndAttributesApi api = new AnalyticsAndAttributesApi(apiClient);


    @Test
    void generateConsumerAttributesTest() {
        try {
            api.generateConsumerAttributes(CUSTOMER_ID, new Object());
        } catch (ApiException e) {
            // {"detail":"CA360 System Error"}
            logApiException(e);
        }
    }

    @Test
    void getConsumerAttributesByIDTest() {
        try {
            var attributeList = api.listConsumerAttributes(CUSTOMER_ID);
            var list = attributeList.getAnalyticIds();
            if (list.size() > 0) {
                api.getConsumerAttributesByID(list.get(0).getAnalyticId(), CUSTOMER_ID);
            }
        } catch (ApiException e) {
            // {"detail":"CA360 System Error"}
            logApiException(e);
        }
    }

    @Test
    void listConsumerAttributesTest() {
        try {
            var attributeList = api.listConsumerAttributes(CUSTOMER_ID);
            assertNotNull(attributeList);
        } catch (ApiException e) {
            // {"detail":"CA360 System Error"}
            logApiException(e);
        }
    }
}
