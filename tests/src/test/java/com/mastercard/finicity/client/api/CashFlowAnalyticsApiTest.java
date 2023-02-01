package com.mastercard.finicity.client.api;

import client.api.CustomCashFlowAnalyticsApi;
import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.BalanceAndCashFlowAnalyticsReportConstraints;
import com.mastercard.finicity.client.test.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CashFlowAnalyticsApiTest extends BaseTest {

    private final CustomCashFlowAnalyticsApi api = new CustomCashFlowAnalyticsApi(apiClient);

    @Test
    public void generateCashFlowAnalyticsTest() {
        try {
            // Generate the report
            var reportConstraints = new BalanceAndCashFlowAnalyticsReportConstraints();
            var generateResponse = api.generateCashFlowAnalytics(CUSTOMER_ID, reportConstraints, null);
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
