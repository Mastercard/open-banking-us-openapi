package com.mastercard.finicity.client.api;

import client.api.CustomBalanceAnalyticsApi;
import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.BalanceAndCashFlowAnalyticsReportConstraints;
import com.mastercard.finicity.client.test.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class BalanceAnalyticsApiTest extends BaseTest {

    private final CustomBalanceAnalyticsApi api = new CustomBalanceAnalyticsApi(apiClient);

    @Test
    void generateBalanceAnalyticsTest() {
        try {
            // Generate a report
            var reportConstraints = new BalanceAndCashFlowAnalyticsReportConstraints();
            var generateResponse = api.generateBalanceAnalytics(CUSTOMER_ID, reportConstraints, null);
            var reportId = generateResponse.getReportId();

            // Fetch the report as JSON
            var report = api.getObbAnalyticsJsonReport(reportId);
            assertNotNull(report);

            // Fetch the report as PDF
            var pdf = api.getObbAnalyticsPdfReport(reportId);
            assertNotNull(pdf);
        } catch (ApiException e) {
            fail(e);
        }
    }
}
