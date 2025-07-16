package com.mastercard.openbanking.client.api;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.PaymentEnablementBundle;
import com.mastercard.openbanking.client.test.BaseTest;
import com.mastercard.openbanking.client.test.utils.AccountUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PaymentEnablementBundleApiTest extends BaseTest {

    private static final AccountsApi api = new AccountsApi(apiClient);
    
    private final PaymentEnablementBundleApi paymentEnablementBundleApi = new PaymentEnablementBundleApi(apiClient);
    
    private static Long existingInstitutionLoginId;
    
    private static String existingAccountId;
    
    @BeforeAll
    protected static void beforeAll() {
        try {
            // Load existing IDs to be used in the subsequent tests
            var existingAccount = AccountUtils.getCustomerAccounts(api, CUSTOMER_ID).get(0);
            existingInstitutionLoginId = existingAccount.getInstitutionLoginId();
            existingAccountId = existingAccount.getId();
            assertNotNull(existingInstitutionLoginId);
            assertNotNull(existingAccountId);
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    public void getAccountDetailsByAccountIdTest() {
        try {
            String include = null;
            Integer balanceCacheInterval = null;
            PaymentEnablementBundle response = paymentEnablementBundleApi.getAccountDetailsByAccountId(CUSTOMER_ID, existingAccountId, include, balanceCacheInterval);
            assertNotNull(response);
        } catch (ApiException e) {
             fail(e);
        }
    }

    @Test
    public void getAccountDetailsByInstitutionLoginIdTest() {
        try {
            String include = null;
            Integer balanceCacheInterval = null;
            PaymentEnablementBundle response = paymentEnablementBundleApi.getAccountDetailsByInstitutionLoginId(CUSTOMER_ID, existingInstitutionLoginId, include, balanceCacheInterval);
            assertNotNull(response);
        } catch (ApiException e) {
             fail(e);
        }
    }

}
