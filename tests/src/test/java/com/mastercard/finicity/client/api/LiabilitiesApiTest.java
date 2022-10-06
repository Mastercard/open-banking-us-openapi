package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.test.BaseTest;
import com.mastercard.finicity.client.test.utils.AccountUtils;
import org.junit.jupiter.api.Test;

class LiabilitiesApiTest extends BaseTest {

    private final LiabilitiesApi api = new LiabilitiesApi(apiClient);

    @Test
    void getLoanPaymentDetailsTest() {
        try {
            var existingAccount = AccountUtils.getCustomerAccounts(new AccountsApi(apiClient), CUSTOMER_ID).get(0);
            api.getLoanPaymentDetails(CUSTOMER_ID, existingAccount.getId());
            fail();
        } catch (ApiException e) {
            // {"code":14020,"message":"Bad request. (Account type not supported)"}
            logApiException(e);
        }
    }
}
