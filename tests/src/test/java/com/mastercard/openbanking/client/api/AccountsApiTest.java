package com.mastercard.openbanking.client.api;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.CustomerAccounts;
import com.mastercard.openbanking.client.test.BaseTest;
import com.mastercard.openbanking.client.test.utils.AccountUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountsApiTest extends BaseTest {

    private final static AccountsApi api = new AccountsApi(apiClient);
    private static String existingInstitutionLoginId;
    private static Long existingInstitutionId;
    private static String existingAccountId;

    @BeforeAll
    protected static void beforeAll() {
        try {
            // Load existing IDs to be used in the subsequent tests
            var existingAccount = AccountUtils.getCustomerAccounts(api, CUSTOMER_ID).get(0);
            existingInstitutionLoginId = existingAccount.getInstitutionLoginId().toString();
            existingAccountId = existingAccount.getId();
            existingInstitutionId = Long.parseLong(existingAccount.getInstitutionId());
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getCustomerAccountsByInstitutionLoginTest() {
        try {
            var accounts = api.getCustomerAccountsByInstitutionLogin(CUSTOMER_ID, existingInstitutionLoginId);
            assertTrue(accounts.getAccounts().size() > 0);
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void refreshCustomerAccountsByInstitutionLoginTest() {
        try {
            CustomerAccounts accounts = api.refreshCustomerAccountsByInstitutionLogin(CUSTOMER_ID, existingInstitutionLoginId,null, null);
            assertTrue(accounts.getAccounts().size() > 0);
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getCustomerAccountTest() {
        try {
            var account = api.getCustomerAccount(CUSTOMER_ID, existingAccountId);
            assertEquals(existingAccountId, account.getId());
            assertEquals(CUSTOMER_ID, account.getCustomerId());
            assertNotNull(account);
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getCustomerAccountsTest() {
        try  {
            var customerAccounts = api.getCustomerAccounts(CUSTOMER_ID, null, null);
            var accounts = customerAccounts.getAccounts();
            assertTrue(accounts.size() > 0);
            var firstAccount = accounts.get(0);
            assertEquals(CUSTOMER_ID, firstAccount.getCustomerId());
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getCustomerAccountsByInstitutionTest() {
        try  {
            var accounts = api.getCustomerAccountsByInstitution(CUSTOMER_ID, existingInstitutionId);
            var firstAccount = accounts.getAccounts().get(0);
            assertEquals(existingInstitutionId, Long.parseLong(firstAccount.getInstitutionId()));
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void refreshCustomerAccountsTest() {
        try {
            var response = api.refreshCustomerAccounts(CUSTOMER_ID);
            assertNotNull(response.getAccounts());
            assertTrue(response.getAccounts().size() > 0);
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getCustomerAccountsByInstitutionTest_UnknownInstitution() {
        try {
            api.getCustomerAccountsByInstitution(CUSTOMER_ID, 1234567L);
            Assertions.fail();
        } catch (ApiException e) {
            // HTTP 404
            // {"code":38006,"message":"Customer does not have any accounts associated with institutionId = (1234567)"}
            logApiException(e);
            assertErrorCodeEquals(38006, e);
            assertErrorMessageEquals("Customer does not have any accounts associated with institutionId = (1234567)", e);
        }
    }

    @Test
    void deleteCustomerAccountsByInstitutionLoginTest_UnknownCustomerId() {
        try {
            api.deleteCustomerAccountsByInstitutionLogin("1234", "1234");
            Assertions.fail();
        } catch (ApiException e) {
            // {"code":38007,"message":"Customer does not have any accounts associated with institutionLoginId = (1234)"}
            logApiException(e);
            assertErrorCodeEquals(38007, e);
            assertErrorMessageEquals("Customer does not have any accounts associated with institutionLoginId = (1234)", e);
        }
    }

    @Test
    void deleteCustomerAccountsByInstitutionLoginTest_UnknownInstitutionLoginId() {
        try {
            api.deleteCustomerAccountsByInstitutionLogin(CUSTOMER_ID, "1234");
            Assertions.fail();
        } catch (ApiException e) {
            // {"code":38007,"message":"Customer does not have any accounts associated with institutionLoginId = (1234)"}
            logApiException(e);
            assertErrorCodeEquals(38007, e);
            assertErrorMessageEquals("Customer does not have any accounts associated with institutionLoginId = (1234)", e);
        }
    }

    @Test
    void deleteCustomerAccountTest_UnknownCustomerId() {
        try {
            api.deleteCustomerAccount("1234", "1234");
            Assertions.fail();
        } catch (ApiException e) {
            // {"code":38003,"message":"customer does not have given account (customerId = 1234, accountId = [1234])"}
            logApiException(e);
            assertErrorCodeEquals(38003, e);
            assertErrorMessageEquals("customer does not have given account (customerId = 1234, accountId = [1234])", e);
        }
    }

    @Test
    void deleteCustomerAccountTest_UnknownAccountId() {
        try {
            api.deleteCustomerAccount(CUSTOMER_ID, "1234");
            Assertions.fail();
        } catch (ApiException e) {
            // {"code":38003,"message":"customer does not have given account (customerId = 5026247981, accountId = [1234])"}
            logApiException(e);
            assertErrorCodeEquals(38003, e);
            assertErrorMessageEquals("customer does not have given account (customerId = " + CUSTOMER_ID + ", accountId = [1234])", e);
        }
    }

    @Test
    void refreshCustomerAccountsByInstitutionLoginV2Test() {
        String institutionLoginId = existingInstitutionLoginId;
       try {
    	   api.refreshCustomerAccountsByInstitutionLoginV2(CUSTOMER_ID, institutionLoginId);
       } catch (ApiException e) {
    	   // {"code":38007,"message":"Customer does not have any accounts associated with institutionLoginId = (1234)"}
    	   logApiException(e);
    	   assertErrorCodeEquals(38007, e);
    	   assertErrorMessageEquals("Customer does not have any accounts associated with institutionLoginId = (1234)", e);
       }     
    }

    @Test
    void refreshCustomerAccountsV2Test() {
        try {
        	api.refreshCustomerAccountsV2(CUSTOMER_ID);
        } catch (ApiException e) {
        	Assertions.fail(e);
        }        
    }
}

