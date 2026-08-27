package com.mastercard.openbanking.client.api;

// import client.api.CustomBalanceAnalyticsApi;
import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.AnalyticsReportData;
import com.mastercard.openbanking.client.model.BalanceAndCashFlowAnalyticsReportConstraints;
import com.mastercard.openbanking.client.model.ForesightAnalyticsReportConstraints;
import com.mastercard.openbanking.client.model.ForesightAnalyticsReportData;
import com.mastercard.openbanking.client.test.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ForesightAnalyticsApiTest extends BaseTest {

    private final ConsumerForesightApi api = new ConsumerForesightApi(apiClient);

    @Test
    void generateForesightAnalyticsTest() {
        try {
            // Generate a report
            var reportConstraints = new ForesightAnalyticsReportConstraints();
            ForesightAnalyticsReportData foresightAnalyticsReportData = new ForesightAnalyticsReportData();
            foresightAnalyticsReportData.setForCraPurpose(false);
            reportConstraints.setAnalyticsReportData(foresightAnalyticsReportData);
            var reportAck = api.generateForesightAnalyticsNonCraReport(CUSTOMER_ID,"Personal" , reportConstraints,null);
            assertNotNull(reportAck);
            assertEquals("inProgress", reportAck.getStatus());
            assertEquals("farpbfnoncra", reportAck.getType());
        } catch (ApiException e) {
            fail(e);
        }
    }

}
