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
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VerifyAssetsApiTest extends BaseTest {

    private final VerifyAssetsApi api = new VerifyAssetsApi(apiClient);
    private static String customerAccountList;

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
    void generatePrequalificationNonCRAReportTest() {
        try {
            var reportConstraints = new ReportConstraints().accountIds(customerAccountList);
            var reportData = api.generatePrequalificationNonCRAReport(CUSTOMER_ID, reportConstraints, null);
            assertNotNull(reportData);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.ASSETSUMMARY, reportData.getType());
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void generatePrequalificationReportTest() {
        try {
            var reportConstraints = new ReportConstraints().accountIds(customerAccountList);
            var reportData = api.generatePrequalificationReport(CUSTOMER_ID, reportConstraints, null);
            assertNotNull(reportData);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.PREQUALVOA, reportData.getType());
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void generateVOAReportTest() {
        try {
            var reportConstraints = new ReportConstraints().accountIds(customerAccountList);
            var reportData = api.generateVOAReport(CUSTOMER_ID, reportConstraints, null);
            assertNotNull(reportData);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.VOA, reportData.getType());
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void generateVOAWithIncomeReportTest() {
        try {
            var reportConstraints = new ReportConstraints().accountIds(customerAccountList);
            var reportData = api.generateVOAWithIncomeReport(CUSTOMER_ID, reportConstraints, null);
            assertNotNull(reportData);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.VOAHISTORY, reportData.getType());
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }
}
