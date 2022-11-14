package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.ConsumerAttributeAccountIDs;
import com.mastercard.finicity.client.test.BaseTest;
import com.mastercard.finicity.client.test.utils.AccountUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnalyticsAndAttributesApiTest extends BaseTest {

    private static String existingSavingAccountId;

    private static final AnalyticsAndAttributesApi api = new AnalyticsAndAttributesApi(apiClient);

    @BeforeAll
    protected static void beforeAll() {
        try {
            // Load existing IDs to be used in the subsequent tests
            var existingAccount = AccountUtils.getCustomerAccounts(new AccountsApi(apiClient), CUSTOMER_ID, "savings").get(0);
            existingSavingAccountId = existingAccount.getId();
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void generateConsumerAttributesTest() {
        try {
            var attributes = new ConsumerAttributeAccountIDs()
                    .addAccountIdsItem(existingSavingAccountId);
            api.generateConsumerAttributes(CUSTOMER_ID, attributes);
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    public void generateFCRAConsumerAttributesTest() {
        try {
            var attributes = new ConsumerAttributeAccountIDs()
                    .addAccountIdsItem(existingSavingAccountId);
            api.generateFCRAConsumerAttributes(CUSTOMER_ID, attributes);
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getConsumerAttributesByIDTest() {
        try {
            var attributes = new ConsumerAttributeAccountIDs()
                    .addAccountIdsItem(existingSavingAccountId);
            var response = api.generateConsumerAttributes(CUSTOMER_ID, attributes);
            api.getConsumerAttributesByID(response.getAnalyticId(), CUSTOMER_ID);
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getFCRAConsumerAttributesByIDTest() {
        try {
            var attributes = new ConsumerAttributeAccountIDs()
                    .addAccountIdsItem(existingSavingAccountId);
            var response = api.generateFCRAConsumerAttributes(CUSTOMER_ID, attributes);
            api.getFCRAConsumerAttributesByID(response.getAnalyticId(), CUSTOMER_ID, null);
        } catch (ApiException e) {
            // Status code: 404 - Reason: {"detail":""}
            logApiException(e);
            assertTrue(e.getResponseBody().contains("{\"detail\":\"\"}"));
        }
    }

    @Test
    void listConsumerAttributesTest() {
        try {
            var attributeList = api.listConsumerAttributes(CUSTOMER_ID);
            assertNotNull(attributeList);
        } catch (ApiException e) {
            fail(e);
        }
    }
}
