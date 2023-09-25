package com.mastercard.openbanking.client.api;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.PrequalificationReportConstraints;
import com.mastercard.openbanking.client.model.VOAReportConstraints;
import com.mastercard.openbanking.client.model.VOAWithIncomeReportConstraints;
import com.mastercard.openbanking.client.test.BaseTest;
import com.mastercard.openbanking.client.test.utils.AccountUtils;
import com.mastercard.openbanking.client.test.utils.ConsumerUtils;
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
            fail(e);
        }
    }

    @Test
    void generatePrequalificationNonCRAReportTest() {
        try {
            var reportConstraints = new PrequalificationReportConstraints().accountIds(customerAccountList);
            var reportAck = api.generatePrequalificationNonCRAReport(CUSTOMER_ID, reportConstraints, null);
            assertNotNull(reportAck);
            assertEquals("inProgress", reportAck.getStatus());
            assertEquals("assetSummary", reportAck.getType());
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void generatePrequalificationCRAReportTest() {
        try {
            var reportConstraints = new PrequalificationReportConstraints().accountIds(customerAccountList);
            var reportAck = api.generatePrequalificationCRAReport(CUSTOMER_ID, reportConstraints, null);
            assertNotNull(reportAck);
            assertEquals("inProgress", reportAck.getStatus());
            assertEquals("preQualVoa", reportAck.getType());
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void generateVOAReportTest() {
        try {
            var reportConstraints = new VOAReportConstraints().accountIds(customerAccountList);
            var reportAck = api.generateVOAReport(CUSTOMER_ID, reportConstraints, null);
            assertNotNull(reportAck);
            assertEquals("inProgress", reportAck.getStatus());
            assertEquals("voa", reportAck.getType());
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void generateVOAWithIncomeReportTest() {
        try {
            var reportConstraints = new VOAWithIncomeReportConstraints().accountIds(customerAccountList);
            var reportAck = api.generateVOAWithIncomeReport(CUSTOMER_ID, reportConstraints, null);
            assertNotNull(reportAck);
            assertEquals("inProgress", reportAck.getStatus());
            assertEquals("voaHistory", reportAck.getType());
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }
}
