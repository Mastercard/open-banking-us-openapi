package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.ReportConstraints;
import com.mastercard.finicity.client.model.ReportType;
import com.mastercard.finicity.client.test.BaseTest;
import com.mastercard.finicity.client.test.utils.AccountUtils;
import com.mastercard.finicity.client.test.utils.ConsumerUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CashFlowApiTest extends BaseTest {

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
            logApiException(e);
            fail();
        }
    }

    @Test
    void generateCashFlowReportBusinessTest() {
        try {
            var constraints = new ReportConstraints().accountIds(customerAccountList);
            var reportData = api.generateCashFlowBusinessReport(CUSTOMER_ID, constraints, null, null);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.CFRB, reportData.getType());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void generateCashFlowReportPersonalTest() {
        try {
            var constraints = new ReportConstraints().accountIds(customerAccountList);
            var reportData = api.generateCashFlowPersonalReport(CUSTOMER_ID, constraints, null, null);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.CFRP, reportData.getType());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }
}
