package com.mastercard.openbanking.client.api;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.MicroDepositVerification;
import com.mastercard.openbanking.client.test.BaseTest;
import com.mastercard.openbanking.client.test.ModelFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class AccountValidationAssistanceApiTest extends BaseTest {

    private static final AccountValidationAssistanceApi api = new AccountValidationAssistanceApi(apiClient);
    private static String customerId;
    private static String accountId;

    @BeforeAll
    static void beforeAll() throws ApiException {
        customerId = customersApi.addTestingCustomer(ModelFactory.newCustomer()).getId();
        createdCustomerIds.add(customerId);
        accountId = api.initiateMicroAmountDeposits(customerId, ModelFactory.newMicroDepositInitiation()).getAccountId();
    }

    @Test
    public void initiateMicroAmountDepositsTest() {
        assertNotNull(accountId);
    }

    @Test
    public void getMicroDepositsDetailsTest() {
        try {
            var details = api.getMicroDepositsDetails(customerId, accountId);
            assertNotNull(details);
            assertNotNull(details.getStatus());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    public void verifyMicroAmountDepositsTest() {
        try {
            String status;
            do {
                var details = api.getMicroDepositsDetails(customerId, accountId);
                status = details.getStatus();
            } while (!"Completed".equals(status));

            var verification = new MicroDepositVerification().amounts(Arrays.asList(0.01F, 0.02F));
            var verifyResponse = api.verifyMicroAmountDeposits(customerId, accountId, verification);

            assertNotNull(verifyResponse);
            assertNotNull(verifyResponse.getStatus());
        } catch (ApiException e) {
            fail(e);
        }
    }
}
