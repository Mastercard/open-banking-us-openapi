package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.ConsumerAttributeAccountIDs;
import com.mastercard.finicity.client.test.BaseTest;
import com.mastercard.finicity.client.test.utils.AccountUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AnalyticsAndAttributesApiTest extends BaseTest {

    private static String existingAccountId;
    private final AnalyticsAndAttributesApi api = new AnalyticsAndAttributesApi(apiClient);

    @BeforeAll
    protected static void beforeAll() {
        try {
            // Load existing IDs to be used in the subsequent tests
            var existingAccount = AccountUtils.getCustomerAccounts(new AccountsApi(apiClient), CUSTOMER_ID).get(0);
            existingAccountId = existingAccount.getId();
        } catch (ApiException e) {
            fail();
            logApiException(e);
        }
    }

    @Test
    void generateConsumerAttributesTest() {
        try {
            var attributes = new ConsumerAttributeAccountIDs()
                    .addAccountIdsItem(existingAccountId);
            api.generateConsumerAttributes(CUSTOMER_ID, attributes);
        } catch (ApiException e) {
            // {"detail":"CA360 System Error"}
            logApiException(e);
        }
    }

    @Test
    public void generateFCRAConsumerAttributesTest() {
        try {
            var attributes = new ConsumerAttributeAccountIDs()
                    .addAccountIdsItem(existingAccountId);
            api.generateFCRAConsumerAttributes(CUSTOMER_ID, attributes);
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
