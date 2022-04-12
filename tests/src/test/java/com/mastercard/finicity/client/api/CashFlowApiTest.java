package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.ReportConstraints;
import com.mastercard.finicity.client.model.ReportType;
import com.mastercard.finicity.client.test.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CashFlowApiTest extends BaseTest {

    private final CashFlowApi api = new CashFlowApi();

    @Test
    void generateCashFlowReportBusinessTest() {
        try {
            var reportData = api.generateCashFlowBusinessReport(CUSTOMER_ID, new ReportConstraints(), null, null);
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
            var reportData = api.generateCashFlowPersonalReport(CUSTOMER_ID, new ReportConstraints(), null, null);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.CFRP, reportData.getType());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }
}
