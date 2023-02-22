package com.mastercard.finicity.client.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.test.BaseTest;
import com.mastercard.finicity.client.test.utils.AccountSimpleUtils;

public class AccountsSimpleApiTest extends BaseTest {

    private final static AccountsSimpleApi api = new AccountsSimpleApi(apiClient);
    private static String existingInstitutionLoginId;
    private static Long existingInstitutionId;
    private static String existingAccountId;
    
    @BeforeAll
    protected static void beforeAll() {
        try {
            // Load existing IDs to be used in the subsequent tests
            var existingAccount = AccountSimpleUtils.getCustomerAccountsSimple(api, CUSTOMER_ID).get(0);
            existingInstitutionLoginId = existingAccount.getInstitutionLoginId().toString();
            existingAccountId = existingAccount.getId();
            existingInstitutionId = Long.parseLong(existingAccount.getInstitutionId());
        } catch (ApiException e) {
            fail(e);
        }
    }
    
    @Test
    void getCustomerAccountsByInstitutionLoginSimpleTest() throws ApiException {
      try {
            var accounts = api.getCustomerAccountsByInstitutionLoginSimple(CUSTOMER_ID, existingInstitutionLoginId);
            assertTrue(accounts.getAccounts().size() > 0);
        } catch (ApiException e) {
            fail(e);
        }  
    }
    
    @Test
    void getCustomerAccountsByInstitutionSimpleTest() throws ApiException {
        try  {
            var accounts = api.getCustomerAccountsByInstitutionSimple(CUSTOMER_ID, existingInstitutionId);
            var firstAccount = accounts.getAccounts().get(0);
            assertEquals(existingInstitutionId.toString(), firstAccount.getInstitutionId());
        } catch (ApiException e) {
            fail(e);
        }
    }
    
    @Test
    void getCustomerAccountsSimpleTest() throws ApiException {
        try  {
            var customerAccounts = api.getCustomerAccountsSimple(CUSTOMER_ID);
            var accounts = customerAccounts.getAccounts();
            assertTrue(accounts.size() > 0);
            var firstAccount = accounts.get(0);
            assertEquals(CUSTOMER_ID, firstAccount.getCustomerId());
        } catch (ApiException e) {
            fail(e);
        }
    }
	
    @Test
	void getCustomerAccountSimpleTest() {
        try {
            var account = api.getCustomerAccountSimple(CUSTOMER_ID, existingAccountId);
            assertEquals(existingAccountId, account.getId());
            assertEquals(CUSTOMER_ID, account.getCustomerId());
            assertNotNull(account);
        } catch (ApiException e) {
            fail(e);
        }
    }
    
}
