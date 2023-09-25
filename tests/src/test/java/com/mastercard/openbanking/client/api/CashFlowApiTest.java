package com.mastercard.openbanking.client.api;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.CashFlowReportConstraints;
import com.mastercard.openbanking.client.test.BaseTest;
import com.mastercard.openbanking.client.test.utils.AccountUtils;
import com.mastercard.openbanking.client.test.utils.ConsumerUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CashFlowApiTest extends BaseTest {

    private static String customerAccountList;
    private final CashFlowApi api = new CashFlowApi(apiClient);

    @BeforeAll
    protected static void beforeAll() {
        try {
            // A consumer is required to generate reports
            ConsumerUtils.getOrCreateDefaultConsumer(new ConsumersApi(apiClient), CUSTOMER_ID);

            // Fetch some accounts IDs to be included in reports
            customerAccountList = AccountUtils.getCustomerAccountListString(new AccountsApi(apiClient), CUSTOMER_ID);
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void generateCashFlowReportBusinessTest() {
        try {
            var constraints = new CashFlowReportConstraints()
                    .showNsf(true)
                    .accountIds(customerAccountList);
            var reportAck = api.generateCashFlowBusinessReport(CUSTOMER_ID, constraints, null);
            assertEquals("inProgress", reportAck.getStatus());
            assertEquals("cfrb", reportAck.getType());
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void generateCashFlowReportPersonalTest() {
        try {
            var constraints = new CashFlowReportConstraints()
                    .showNsf(true)
                    .accountIds(customerAccountList);
            var reportAck = api.generateCashFlowPersonalReport(CUSTOMER_ID, constraints, null);
            assertEquals("inProgress", reportAck.getStatus());
            assertEquals("cfrp", reportAck.getType());
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }
}
