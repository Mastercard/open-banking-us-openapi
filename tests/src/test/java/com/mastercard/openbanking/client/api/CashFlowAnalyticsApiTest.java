package com.mastercard.openbanking.client.api;

import client.api.CustomCashFlowAnalyticsApi;
import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.BalanceAndCashFlowAnalyticsReportConstraints;
import com.mastercard.openbanking.client.test.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CashFlowAnalyticsApiTest extends BaseTest {

    private final CustomCashFlowAnalyticsApi api = new CustomCashFlowAnalyticsApi(apiClient);

    // @Test
    // void generateCashFlowAnalyticsTest() {
    //     try {
    //         // Generate the report
    //         var reportConstraints = new BalanceAndCashFlowAnalyticsReportConstraints();
    //         var generateResponse = api.generateCashFlowAnalytics(CUSTOMER_ID, reportConstraints, null);
    //         var reportId = generateResponse.getReportId();

    //         // Fetch the report as JSON
    //         var report = api.getObbAnalyticsJsonReport(reportId);
    //         assertNotNull(report);

    //         // Fetch the report as PDF
    //         var pdf = api.getObbAnalyticsPdfReport(reportId);
    //         assertNotNull(pdf);
    //     } catch (ApiException e) {
    //         fail(e);
    //     }
    // }

}
