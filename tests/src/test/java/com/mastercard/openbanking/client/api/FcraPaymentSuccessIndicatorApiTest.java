package com.mastercard.openbanking.client.api;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.CustomerAccount;
import com.mastercard.openbanking.client.model.CustomerUpdate;
import com.mastercard.openbanking.client.test.BaseTest;
import com.mastercard.openbanking.client.test.utils.AccountUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FcraPaymentSuccessIndicatorApiTest extends BaseTest {

    private final FcraPaymentSuccessIndicatorApi api = new FcraPaymentSuccessIndicatorApi(apiClient);
    private static CustomerAccount existingAccount;


    @BeforeAll
    static void beforeAll() {
        try {
            var customerApi = new CustomersApi(apiClient);
            var customerUpdate = new CustomerUpdate()
                    .putAdditionalProperty("email", "test@finicity.com");
            customerApi.modifyCustomer(CUSTOMER_ID, customerUpdate);

            var accountApi = new AccountsApi(apiClient);
            // 0 th index account type ROTH does not return any PSS details. Untill fix from API side.
            existingAccount = AccountUtils.getCustomerAccounts(accountApi, CUSTOMER_ID).get(1);
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getFcraPaymentSuccessIndicatorTest() {
        try {
            var settlementAmount = new BigDecimal(10);
            var response = api.getFCRAPaymentSuccessIndicator(CUSTOMER_ID, existingAccount.getId(),
                    settlementAmount, LocalDate.now().plusDays(1), "1P");
            assertNotNull(response.getPayReqId());
            assertNotNull(response.getCustomerId());
            assertEquals(CUSTOMER_ID, response.getCustomerId());
        } catch (ApiException e) {
            fail(e);
        }
    }
}
