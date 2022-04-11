package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.CustomerAccount;
import com.mastercard.finicity.client.model.CustomerAccounts;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class AccountsApiTest {

    private final AccountsApi api = new AccountsApi();

    @Test
    public void deleteCustomerAccountTest() throws ApiException {
        String customerId = null;
        Long accountId = null;
                api.deleteCustomerAccount(customerId, accountId);
        // TODO: test validations
    }

    @Test
    public void deleteCustomerAccountsByInstitutionLoginTest() throws ApiException {
        String customerId = null;
        Long institutionLoginId = null;
                api.deleteCustomerAccountsByInstitutionLogin(customerId, institutionLoginId);
        // TODO: test validations
    }

    @Test
    public void getCustomerAccountTest() throws ApiException {
        String customerId = null;
        Long accountId = null;
                CustomerAccount response = api.getCustomerAccount(customerId, accountId);
        // TODO: test validations
    }

    @Test
    public void getCustomerAccountsTest() throws ApiException {
        String customerId = null;
        String status = null;
                CustomerAccounts response = api.getCustomerAccounts(customerId, status);
        // TODO: test validations
    }

    @Test
    public void getCustomerAccountsByInstitutionTest() throws ApiException {
        String customerId = null;
        Long institutionId = null;
                CustomerAccounts response = api.getCustomerAccountsByInstitution(customerId, institutionId);
        // TODO: test validations
    }

    @Test
    public void getCustomerAccountsByInstitutionLoginTest() throws ApiException {
        String customerId = null;
        Long institutionLoginId = null;
                CustomerAccounts response = api.getCustomerAccountsByInstitutionLogin(customerId, institutionLoginId);
        // TODO: test validations
    }

    @Test
    public void loadHistoricTransactionsForCustomerAccountTest() throws ApiException {
        Integer contentLength = null;
        String customerId = null;
        Long accountId = null;
                api.loadHistoricTransactionsForCustomerAccount(contentLength, customerId, accountId);
        // TODO: test validations
    }

    @Test
    public void refreshCustomerAccountsTest() throws ApiException {
        Integer contentLength = null;
        String customerId = null;
                CustomerAccounts response = api.refreshCustomerAccounts(contentLength, customerId);
        // TODO: test validations
    }

    @Test
    public void refreshCustomerAccountsByInstitutionLoginTest() throws ApiException {
        Integer contentLength = null;
        Boolean interactive = null;
        String customerId = null;
        String institutionLoginId = null;
                CustomerAccounts response = api.refreshCustomerAccountsByInstitutionLogin(contentLength, interactive, customerId, institutionLoginId);
        // TODO: test validations
    }
    
}
