package com.mastercard.finicity.client.test;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.api.AccountsApi;
import com.mastercard.finicity.client.model.CustomerAccount;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountUtils {

    /**
     * Return all active accounts for the given customer.
     */
    public static List<CustomerAccount> getCustomerAccounts(AccountsApi api, String customerId) throws ApiException {
        var customerAccounts = api.getCustomerAccounts(customerId, "active");
        var accounts = customerAccounts.getAccounts();
        assertTrue(accounts.size() > 0, "No account found for customerId " + customerId + "!");
        var firstAccount = accounts.get(0);
        assertEquals(customerId, firstAccount.getCustomerId());
        return accounts;
    }

    /**
     * Return all active accounts of the given type for the given customer.
     */
    public static List<CustomerAccount> getCustomerAccounts(AccountsApi api, String customerId, String type) throws ApiException {
        return AccountUtils.getCustomerAccounts(api, customerId)
                .stream()
                .filter(a -> type.equals(a.getType().getValue()))
                .collect(Collectors.toList());
    }
}
