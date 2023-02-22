package com.mastercard.finicity.client.test.utils;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.api.AccountsSimpleApi;
import com.mastercard.finicity.client.model.CustomerAccountSimple;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AccountSimpleUtils {

	/**
     * Return all active accounts for the given customer.
     */
    public static List<CustomerAccountSimple> getCustomerAccountsSimple(AccountsSimpleApi api, String customerId) throws ApiException {
        var customerAccounts = api.getCustomerAccountsSimple(customerId);
        var accounts = customerAccounts.getAccounts();
        assertTrue(accounts.size() > 0, "No account found for customerId " + customerId + "!");
        var firstAccount = accounts.get(0);
        assertEquals(customerId, firstAccount.getCustomerId());
        return accounts;
    }
}
