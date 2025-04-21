package com.mastercard.openbanking.client.api;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.CustomerAccount;
import com.mastercard.openbanking.client.test.BaseTest;
import com.mastercard.openbanking.client.test.utils.AccountUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentSuccessIndicatorApiTest extends BaseTest {

    private final PaymentSuccessIndicatorApi api = new PaymentSuccessIndicatorApi(apiClient);
    private static CustomerAccount existingAccount;

    @BeforeAll
    static void beforeAll() {
        try {
            var accountApi = new AccountsApi(apiClient);
            // 0 th index account type ROTH does not return any PSS details. Untill fix from API side.
            existingAccount = AccountUtils.getCustomerAccounts(accountApi, CUSTOMER_ID).get(1);
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getPaymentSuccessIndicatorTest() {
        try {
            var settlementAmount = new BigDecimal(10);
            var response = api.getPaymentSuccessIndicator(CUSTOMER_ID, existingAccount.getId(),
                    settlementAmount, LocalDate.now().plusDays(1));
            assertNotNull(response.getPayReqId());
            assertNotNull(response.getCustomerId());
            assertEquals(CUSTOMER_ID, response.getCustomerId());
        } catch (ApiException e) {
            fail(e);
        }
    }
}
