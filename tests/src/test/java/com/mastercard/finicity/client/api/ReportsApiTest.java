package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.ReportData;
import com.mastercard.finicity.client.model.ReportConstraints;
import com.mastercard.finicity.client.test.BaseAppKeyAppTokenTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReportsApiTest extends BaseAppKeyAppTokenTest {

    private final ReportsApi api = new ReportsApi(apiClient);

    private static ReportData voaReportData, prequalificationReportData;

    @BeforeAll
    public static void beforeAll() {
        try {
            BaseAppKeyAppTokenTest.beforeAll();
            // Generate reports we can fetch in the tests
            var verifyAssetsApi = new VerifyAssetsApi(apiClient);
            voaReportData = verifyAssetsApi.generateVOAReport(CUSTOMER_ID, null, null, new ReportConstraints());
            prequalificationReportData = verifyAssetsApi.generatePrequalificationReport(CUSTOMER_ID, null, new ReportConstraints());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    public void getPrequalificationReportByCustomerTest() {
        try {
            var reportId = prequalificationReportData.getId();
            var onBehalfOf = "Test";
            var purpose = "99";
            var report = api.getPrequalificationReportByCustomer(CUSTOMER_ID, reportId, onBehalfOf, purpose);
            assertNotNull(report);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    public void getReportByConsumerTest() {
        try {
            var reportId = voaReportData.getId();
            var consumerId = voaReportData.getConsumerId();
            var onBehalfOf = "Test";
            var purpose = "99";
            var report = api.getReportByConsumer(consumerId, reportId, onBehalfOf, purpose);
            assertNotNull(report);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    public void getReportsByConsumerTest() {
        try {
            var consumerId = voaReportData.getConsumerId();
            var report = api.getReportsByConsumer(consumerId, null);
            assertNotNull(report);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    public void getReportsByCustomerTest() {
        try {
            var report = api.getReportsByCustomer(CUSTOMER_ID, null);
            assertNotNull(report);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }
}
