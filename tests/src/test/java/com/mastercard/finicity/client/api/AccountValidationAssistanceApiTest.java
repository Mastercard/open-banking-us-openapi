package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.MicroDepositVerification;
import com.mastercard.finicity.client.test.BaseTest;
import com.mastercard.finicity.client.test.ModelFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class AccountValidationAssistanceApiTest extends BaseTest {

    private static final AccountValidationAssistanceApi api = new AccountValidationAssistanceApi(apiClient);

    @Test
    public void initiateMicroAmountDepositsTest() {
        try {
            // GIVEN
            String customerId = createTestCustomer();

            // WHEN
            var response = api.initiateMicroAmountDeposits(customerId, ModelFactory.newMicroDepositInitiation());

            // THEN
            assertNotNull(response);
            assertNotNull(response.getAccountId());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    public void getMicroDepositsDetailsTest() {
        try {
            // GIVEN
            String customerId = createTestCustomer();
            var response = api.initiateMicroAmountDeposits(customerId, ModelFactory.newMicroDepositInitiation());
            var accountId = response.getAccountId();

            // WHEN
            var details = api.getMicroDepositsDetails(customerId, accountId);

            // THEN
            assertNotNull(details);
            assertNotNull(details.getStatus());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    public void verifyMicroAmountDepositsTest() {
        try {
            // GIVEN
            String customerId = createTestCustomer();
            var response = api.initiateMicroAmountDeposits(customerId, ModelFactory.newMicroDepositInitiation());
            var accountId = response.getAccountId();
            String status;
            do {
                var details = api.getMicroDepositsDetails(customerId, accountId);
                status = details.getStatus();
            } while (!"Completed".equals(status));

            // WHEN
            var verification = new MicroDepositVerification().amounts(Arrays.asList(0.01F, 0.02F));
            var verifyResponse = api.verifyMicroAmountDeposits(customerId, accountId, verification);

            // THEN
            assertNotNull(verifyResponse);
            assertNotNull(verifyResponse.getStatus());
        } catch (ApiException e) {
            fail(e);
        }
    }

    private static String createTestCustomer() throws ApiException {
        var customer = customersApi.addTestingCustomer(ModelFactory.newCustomer());
        var customerId = customer.getId();
        createdCustomerIds.add(customerId);
        return customerId;
    }
}