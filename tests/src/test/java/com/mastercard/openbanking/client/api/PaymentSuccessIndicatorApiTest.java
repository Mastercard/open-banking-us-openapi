package com.mastercard.openbanking.client.api;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.CustomerAccount;
import com.mastercard.openbanking.client.model.PaymentSuccessIndicatorsProperties;
import com.mastercard.openbanking.client.model.PaymentSuccessIndicatorsTransaction;
import com.mastercard.openbanking.client.test.BaseTest;
import com.mastercard.openbanking.client.test.utils.AccountUtils;

@TestMethodOrder(OrderAnnotation.class)
class PaymentSuccessIndicatorApiTest extends BaseTest {

    private final PaymentSuccessIndicatorLegacyApi legacyApi = new PaymentSuccessIndicatorLegacyApi(apiClient);
    private final PaymentSuccessIndicatorApi api = new PaymentSuccessIndicatorApi(apiClient);
    private static CustomerAccount account;

    private static BigDecimal settlementAmount = new BigDecimal(10);
    private static LocalDate settleByDate = LocalDate.now().plusDays(1);

    private static String payRequestId;


    @BeforeAll
    static void beforeAll() {
        try {
            var accountApi = new AccountsApi(apiClient);
            var accounts = AccountUtils.getCustomerAccounts(accountApi, CUSTOMER_ID);
            for (int i = 0; i < accounts.size(); ++i) {
                var acct = accounts.get(i);
                if (acct.getType().equals("savings")) {
                    account = acct;
                    break;
                }
            }
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    @Order(1)
    void getPaymentSuccessIndicatorTest() {
        try {
            var response = legacyApi.getPaymentSuccessIndicator(
                    CUSTOMER_ID,
                    account.getId(),
                    settlementAmount,
                    settleByDate
            );
            assertNotNull(response.getPayReqId());
            assertNotNull(response.getCustomerId());
            assertEquals(CUSTOMER_ID, response.getCustomerId());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    @Order(2)
    void generatePaymentSuccessIndicatorTest() {
        try {
            var properties = new PaymentSuccessIndicatorsProperties().transaction(
                    new PaymentSuccessIndicatorsTransaction()
                            .settleByDate(settleByDate)
                            .amount(settlementAmount)
            );
            var response = api.generatePaymentSuccessIndicators(
                    CUSTOMER_ID,
                    account.getId(),
                    properties);

            assertNotNull(response.getPayRequestId());
            assertEquals("IN PROGRESS", response.getStatus());
            assertNotNull(response.getCustomerId());
            assertEquals(CUSTOMER_ID, response.getCustomerId());
            assertNotNull(response.getRequestDate());
            assertEquals(LocalDate.now(), response.getRequestDate());

            payRequestId = response.getPayRequestId();
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    @Order(3)
    void retrievePaymentSuccessIndicatorsTest() throws InterruptedException {
        try {
            var response = api.getPaymentSuccessIndicators(
                    CUSTOMER_ID,
                    account.getId(),
                    payRequestId,
                    false);
            for (int i = 0; i < 10; ++i) {
                if (!response.getStatus().equals("IN PROGRESS")) {
                    break;
                }
                Thread.sleep(1000);
                response = api.getPaymentSuccessIndicators(
                        CUSTOMER_ID,
                        account.getId(),
                        payRequestId,
                        false);
            }

            // Validate response without reasons
            assertEquals("SUCCESS", response.getStatus());

            assertNotNull(response.getNsfReturnRisk());
            var nsfReturnRisk = response.getNsfReturnRisk();

            assertNotNull(nsfReturnRisk.getResult());
            assertNull(nsfReturnRisk.getError());

            var nsfResult = nsfReturnRisk.getResult();
            assertEquals(3, nsfResult.getDailyResults().size());

            var dayResult = nsfResult.getDailyResults().get(0);
            assertNull(dayResult.getReasons());
            assertNotNull(response.getUnauthorizedReturnRisk());

            var unauthorizedReturnRisk = response.getUnauthorizedReturnRisk();
            assertNotNull(unauthorizedReturnRisk.getResult());
            assertNull(unauthorizedReturnRisk.getError());

            // Now re-validate with reasons
            response = api.getPaymentSuccessIndicators(
                    CUSTOMER_ID,
                    account.getId(),
                    payRequestId,
                    true);

            assertNotNull(response.getNsfReturnRisk());
            nsfReturnRisk = response.getNsfReturnRisk();

            assertNotNull(nsfReturnRisk.getResult());
            nsfResult = nsfReturnRisk.getResult();
            dayResult = nsfResult.getDailyResults().get(0);
            assertNotNull(dayResult.getReasons());
        } catch (ApiException e) {
            fail(e);
        }
    }
}
