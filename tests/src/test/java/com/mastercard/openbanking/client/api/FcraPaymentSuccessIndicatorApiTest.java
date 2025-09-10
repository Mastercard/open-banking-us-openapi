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
import com.mastercard.openbanking.client.model.CustomerUpdate;
import com.mastercard.openbanking.client.model.PaymentSuccessIndicatorsProperties;
import com.mastercard.openbanking.client.model.PaymentSuccessIndicatorsTransaction;
import com.mastercard.openbanking.client.test.BaseTest;
import com.mastercard.openbanking.client.test.utils.AccountUtils;

@TestMethodOrder(OrderAnnotation.class)
class FcraPaymentSuccessIndicatorApiTest extends BaseTest {

    private final FcraPaymentSuccessIndicatorLegacyApi legacyApi = new FcraPaymentSuccessIndicatorLegacyApi(apiClient);
    private final FcraPaymentSuccessIndicatorApi api = new FcraPaymentSuccessIndicatorApi(apiClient);
    private static CustomerAccount account;

    private static BigDecimal settlementAmount = new BigDecimal(10);
    private static LocalDate settleByDate = LocalDate.now().plusDays(1);
    private static String purposeCode = "1P";

    private static String payRequestId;


    @BeforeAll
    static void beforeAll() {
        try {
            var customerApi = new CustomersApi(apiClient);
            var customerUpdate = new CustomerUpdate()
                    .putAdditionalProperty("email", "test@finicity.com");
            customerApi.modifyCustomer(CUSTOMER_ID, customerUpdate);

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
    void getFcraPaymentSuccessIndicatorTest() {
        try {
            var response = legacyApi.getFCRAPaymentSuccessIndicator(
                    CUSTOMER_ID,
                    account.getId(),
                    settlementAmount,
                    settleByDate,
                    purposeCode
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
    void generateFcraPaymentSuccessIndicatorTest() {
        try {
            var properties = new PaymentSuccessIndicatorsProperties().transaction(
                    new PaymentSuccessIndicatorsTransaction()
                            .settleByDate(settleByDate)
                            .amount(settlementAmount)
            );
            var response = api.generateFcraPaymentSuccessIndicators(
                    CUSTOMER_ID,
                    account.getId(),
                    properties,
                    purposeCode);

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
    void retrieveFcraPaymentSuccessIndicatorsTest() throws InterruptedException {
        try {
            var response = api.getFcraPaymentSuccessIndicators(
                    CUSTOMER_ID,
                    account.getId(),
                    payRequestId,
                    false);
            for (int i = 0; i < 10; ++i) {
                if (!response.getStatus().equals("IN PROGRESS")) {
                    break;
                }
                Thread.sleep(1000);
                response = api.getFcraPaymentSuccessIndicators(
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
            response = api.getFcraPaymentSuccessIndicators(
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
