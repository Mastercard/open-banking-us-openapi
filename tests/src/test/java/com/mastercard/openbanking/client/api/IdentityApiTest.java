package com.mastercard.openbanking.client.api;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.CustomerAccount;
import com.mastercard.openbanking.client.test.BaseTest;
import com.mastercard.openbanking.client.test.utils.AccountUtils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;


class IdentityApiTest extends BaseTest {

    private final IdentityApi api = new IdentityApi(apiClient);

    private static CustomerAccount existingAccount;

    @BeforeAll
    static void beforeAll() {
        try {
            var accountApi = new AccountsApi(apiClient);

            existingAccount = AccountUtils
                    .getCustomerAccounts(accountApi, CUSTOMER_ID, "savings")
                    .get(0);

        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getAccountOwnerTest() {
        try {
            var owner = api.getAccountOwner(CUSTOMER_ID, existingAccount.getId());
            assertNotNull(owner);
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getAccountOwnerDetailsTest() {
        try {
            var ownerDetails = api.getAccountOwnerDetails(
                    CUSTOMER_ID,
                    existingAccount.getId(),
                    true,
                    "program=OBAO"
            );
            assertNotNull(ownerDetails);
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getAccountOwnerDetailsWithInsightsFalseTest() {
        try {
            var ownerDetails = api.getAccountOwnerDetails(
                    CUSTOMER_ID,
                    existingAccount.getId(),
                    false,
                    "program=OBAO"
            );
            assertNotNull(ownerDetails);
        } catch (ApiException e) {
            fail(e);
        }
    }
}