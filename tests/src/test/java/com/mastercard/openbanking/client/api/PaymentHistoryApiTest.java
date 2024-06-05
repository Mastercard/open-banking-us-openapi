package com.mastercard.openbanking.client.api;

import client.api.CustomPaymentHistoryApi;
import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.test.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentHistoryApiTest extends BaseTest {

    private final CustomPaymentHistoryApi api = new CustomPaymentHistoryApi(apiClient);

    @Test
    void generatePaymentHistoryTest() {
        try {
            // Generate the report
            var generateResponse = api.generatePaymentHistory(CUSTOMER_ID, null);
            var reportId = generateResponse.getReportId();

            // Fetch the report as JSON
            var report = api.getObbAnalyticsJsonReport(reportId);
            assertNotNull(report);

            // Fetch the report as PDF
            var pdf = api.getObbAnalyticsPdfReport(reportId);
            assertNotNull(pdf);
        }catch(

                ApiException e)
        {
            fail(e);
        }
    }

}
