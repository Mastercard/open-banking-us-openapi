package com.mastercard.openbanking.client.api;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.ConnectGenerateInstantOnboarding;
import com.mastercard.openbanking.client.test.BaseTest;
import com.mastercard.openbanking.client.test.ModelFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InstantOnboardingApiTest extends BaseTest {

    private final InstantOnboardingApi api = new InstantOnboardingApi(apiClient);

    @Test
    void generateConnectInstantOnboardingUrlTest() {
        try {
            String customerId = createTestCustomer();
            var params = new ConnectGenerateInstantOnboarding()
                    .customerId(customerId)
                    .partnerId(PARTNER_ID);
            var response = api.generateInstantOnboardingConnectUrl(params);
            var link = response.getLink();
            assertNotNull(link);
            assertTrue(link.toString().contains("flowId="));
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void generateConnectInstantOnboardingUrlTest_UnknownCustomerId() {
        try {
            var params = new ConnectGenerateInstantOnboarding()
                    .customerId("1234")
                    .partnerId(PARTNER_ID);
            api.generateInstantOnboardingConnectUrl(params);
            fail();
        } catch (ApiException e) {
            logApiException(e);
            assertErrorCodeEquals("10010", e);
        }
    }

    @Test
    void generateConnectInstantOnboardingUrlTest_UnknownPartnerId() {
        try {
            String customerId = createTestCustomer();
            var params = new ConnectGenerateInstantOnboarding()
                    .customerId(customerId)
                    .partnerId("1234");
            api.generateInstantOnboardingConnectUrl(params);
            fail();
        } catch (ApiException e) {
            logApiException(e);
            assertErrorCodeEquals("10010", e);
        }
    }

    private static String createTestCustomer() throws ApiException {
        var customer = customersApi.addTestingCustomer(ModelFactory.newCustomer());
        var customerId = customer.getId();
        createdCustomerIds.add(customerId);
        return customerId;
    }
}
