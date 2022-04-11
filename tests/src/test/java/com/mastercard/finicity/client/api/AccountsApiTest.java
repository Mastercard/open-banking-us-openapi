package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.CustomerAccount;
import com.mastercard.finicity.client.model.CustomerAccounts;
import com.mastercard.finicity.client.test.AccountUtils;
import com.mastercard.finicity.client.test.BaseAppKeyAppTokenTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountsApiTest extends BaseAppKeyAppTokenTest {

    private final AccountsApi api = new AccountsApi(apiClient);

    @Test
    void refreshCustomerAccountsTest() {
        try {
            var response = api.refreshCustomerAccounts(CUSTOMER_ID, new Object());
            assertNotNull(response.getAccounts());
            assertTrue(response.getAccounts().size() > 0);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    @Disabled
    void deleteCustomerAccountTest() throws ApiException {
        String customerId = null;
        Long accountId = null;
                api.deleteCustomerAccount(customerId, accountId);
        // TODO: test validations
    }

    @Test
    @Disabled
    void deleteCustomerAccountsByInstitutionLoginTest() throws ApiException {
        String customerId = null;
        String institutionLoginId = null;
                api.deleteCustomerAccountsByInstitutionLogin(customerId, institutionLoginId);
        // TODO: test validations
    }

    @Test
    @Disabled
    void getCustomerAccountTest() throws ApiException {
        String customerId = null;
        Long accountId = null;
                CustomerAccount response = api.getCustomerAccount(customerId, accountId);
        // TODO: test validations
    }

    @Test
    void getCustomerAccountsTest() {
        try  {
            var customerAccounts = api.getCustomerAccounts(CUSTOMER_ID, null);
            var accounts = customerAccounts.getAccounts();
            assertTrue(accounts.size() > 0);
            var firstAccount = accounts.get(0);
            assertEquals(CUSTOMER_ID, firstAccount.getCustomerId());
        } catch (ApiException e) {
            fail();
            logApiException(e);
        }
    }

    @Test
    void getCustomerAccountsByInstitutionTest() {
        try  {
            var institutionId = AccountUtils.getCustomerAccounts(api, CUSTOMER_ID).get(0).getInstitutionId();
            var accounts = api.getCustomerAccountsByInstitution(CUSTOMER_ID, Long.valueOf(institutionId));
            var firstAccount = accounts.getAccounts().get(0);
            assertEquals(institutionId, firstAccount.getInstitutionId());
        } catch (ApiException e) {
            fail();
            logApiException(e);
        }
    }

    @Test
    void getCustomerAccountsByInstitutionTest_UnknownInstitution() {
        try  {
            var institutionId = 1234567L;
            api.getCustomerAccountsByInstitution(CUSTOMER_ID, institutionId);
            fail();
        } catch (ApiException e) {
            // HTTP 404
            // {"code":38006,"message":"Customer does not have any accounts associated with institutionId = (1234567)"}
            logApiException(e);
            assertErrorCodeEquals("38006", e);
            assertErrorMessageEquals("Customer does not have any accounts associated with institutionId = (1234567)", e);
        }
    }

    @Test
    @Disabled
    void getCustomerAccountsByInstitutionLoginTest() throws ApiException {
        String customerId = null;
        String institutionLoginId = null;
                CustomerAccounts response = api.getCustomerAccountsByInstitutionLogin(customerId, institutionLoginId);
        // TODO: test validations
    }

    @Test
    @Disabled
    void loadHistoricTransactionsForCustomerAccountTest() throws ApiException {
        Integer contentLength = null;
        String customerId = null;
        Long accountId = null;
                api.loadHistoricTransactionsForCustomerAccount(contentLength, customerId, accountId);
        // TODO: test validations
    }

    @Test
    @Disabled
    void refreshCustomerAccountsByInstitutionLoginTest() throws ApiException {
        Integer contentLength = null;
        Boolean interactive = null;
        String customerId = null;
        String institutionLoginId = null;
                CustomerAccounts response = api.refreshCustomerAccountsByInstitutionLogin(contentLength, interactive, customerId, institutionLoginId);
        // TODO: test validations
    }
    
}
