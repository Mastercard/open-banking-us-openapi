package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.*;
import com.mastercard.finicity.client.test.BaseAppKeyAppTokenTest;
import com.mastercard.finicity.client.test.utils.ConsumerUtils;
import com.mastercard.finicity.client.test.ModelFactory;
import com.mastercard.finicity.client.test.utils.PayStatementUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReportsApiTest extends BaseAppKeyAppTokenTest {

    private final static ReportsApi api = new ReportsApi(apiClient);

    private final VerifyAssetsApi verifyAssetsApi = new VerifyAssetsApi(apiClient);
    private final VerifyIncomeAndEmploymentApi verifyIncomeAndEmploymentApi = new VerifyIncomeAndEmploymentApi(apiClient);

    private static String existingAssetId;

    private final static String IN_PROGRESS = "inProgress";
    private final static String ON_BEHALF_OF = "Someone";
    private final static String PURPOSE = "99";

    @BeforeAll
    protected static void beforeAll() {
        try {
            BaseAppKeyAppTokenTest.beforeAll();

            // A consumer is required to generate reports
            ConsumersApi consumersApi = new ConsumersApi(apiClient);
            ConsumerUtils.getOrCreateDefaultConsumer(consumersApi, CUSTOMER_ID);

            // Upload a pay statement for the tests
            existingAssetId = PayStatementUtils.storeAsset(new PayStatementsApi(apiClient), CUSTOMER_ID);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void getPrequalificationReportByCustomerTest() throws Exception {
        try {
            var reportData = verifyAssetsApi.generatePrequalificationReport(CUSTOMER_ID, new ReportConstraints(), null);
            fetchPrequalificationReportReport(reportData.getId());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void getVOAReportByConsumerTest() throws Exception {
        try {
            var reportData = verifyAssetsApi.generateVOAReport(CUSTOMER_ID, new ReportConstraints(), null, null);
            fetchReport(reportData.getId(), reportData.getConsumerId());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void getVOAWithIncomeReportByConsumerTest() throws Exception {
        try {
            var reportData = verifyAssetsApi.generateVOAWithIncomeReport(CUSTOMER_ID, new ReportConstraints(), null, null);
            fetchReport(reportData.getId(), reportData.getConsumerId());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void getAssetSummaryReportByConsumerTest() throws Exception {
        try {
            var reportData = verifyAssetsApi.generateAssetSummaryReport(CUSTOMER_ID, new ReportConstraints(), null);
            fetchReport(reportData.getId(), reportData.getConsumerId());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void getReportsByConsumerTest() {
        try {
            var consumerApi = new ConsumersApi(apiClient);
            var consumerId = ConsumerUtils.getOrCreateDefaultConsumer(consumerApi, CUSTOMER_ID);
            var reports = api.getReportsByConsumer(consumerId, null);
            assertNotNull(reports);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void getReportsByCustomerTest() {
        try {
            var reports = api.getReportsByCustomer(CUSTOMER_ID, null);
            assertNotNull(reports);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void getVOIReportByConsumerTest() throws Exception {
        try {
            var reportData = verifyIncomeAndEmploymentApi.generateVOIReport(CUSTOMER_ID, new ReportConstraints(), null);
            fetchReport(reportData.getId(), reportData.getConsumerId());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void getVOEPayrollReportByConsumerTest() throws Exception {
        try {
            var constraints = new PayrollReportConstraints().payrollData(ModelFactory.newPayrollData());
            var reportData = verifyIncomeAndEmploymentApi.generateVOEPayrollReport(CUSTOMER_ID, constraints, null);
            fetchReport(reportData.getId(), reportData.getConsumerId());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void getPayStatementReportByConsumerTest() {
        try {
            var constraints = new PayStatementReportConstraints().paystatementReport(new PayStatementData().addAssetIdsItem(existingAssetId));
            var reportData = verifyIncomeAndEmploymentApi.generatePayStatementReport(CUSTOMER_ID, constraints, null);
            // This report with have the 'failure' status since the asset uploaded isn't a valid statement
            var report = api.getReportByConsumer(reportData.getConsumerId(), reportData.getId(), ON_BEHALF_OF, PURPOSE);
            assertNotNull(report);
        } catch (ApiException e) {
            logApiException(e);
        }
    }

    @Test
    void getVOIEPaystubReportByConsumerTest() {
        try {
            var voieWithStatementData = new VOIEWithStatementData().addAssetIdsItem(existingAssetId);
            var constraints = new VOIEWithStatementReportConstraints().voieWithStatementData(voieWithStatementData);
            // This report with have the 'failure' status since the asset uploaded isn't a valid statement
            var reportData = verifyIncomeAndEmploymentApi.generateVOIEPaystubReport(CUSTOMER_ID, constraints, null);
            var report = api.getReportByConsumer(reportData.getConsumerId(), reportData.getId(), ON_BEHALF_OF, PURPOSE);
            assertNotNull(report);
        } catch (ApiException e) {
            logApiException(e);
        }
    }

    @Test
    void getVOIEPaystubWithTXVerifyReportByConsumerTest() {
        try {
            var voieWithInterviewData = new VOIEWithInterviewData().addTxVerifyInterviewItem(new TxVerifyInterview().assetId(existingAssetId));
            var constraints = new VOIEWithTXVerifyReportConstraints().voieWithInterviewData(voieWithInterviewData);
            var reportData = verifyIncomeAndEmploymentApi.generateVOIEPaystubWithTXVerifyReport(CUSTOMER_ID, constraints, null);
            // This report with have the 'failure' status since the asset uploaded isn't a valid statement
            var report = api.getReportByConsumer(reportData.getConsumerId(), reportData.getId(), ON_BEHALF_OF, PURPOSE);
            assertNotNull(report);
        } catch (ApiException e) {
            logApiException(e);
        }
    }

    @Test
    void getTransactionsReportByConsumerTest() throws Exception {
        try {
            var reportData = verifyIncomeAndEmploymentApi.generateVOETransactionsReport(CUSTOMER_ID, new VOETransactionsReportConstraints(), null, null);
            fetchReport(reportData.getId(), reportData.getConsumerId());
        } catch (ApiException e) {
            logApiException(e);
        }
    }

    private static void fetchPrequalificationReportReport(String reportId) throws Exception {
        fetchAnyReport(reportId, null, true);
    }

    private static void fetchReport(String reportId, String consumerId) throws Exception {
        fetchAnyReport(reportId, consumerId, false);
    }

    private static void fetchAnyReport(String reportId, String consumerId, boolean preQualificationReport) throws Exception {
        String status;
        do {
            System.out.println("Waiting for report " + reportId + " ...");
            Thread.sleep(5000);
            if (preQualificationReport) {
                var report = api.getPrequalificationReportByCustomer(CUSTOMER_ID, reportId, ON_BEHALF_OF, PURPOSE);
                assertNotNull(report);
                status = report.getStatus();
            } else {
                var report = api.getReportByConsumer(consumerId, reportId, ON_BEHALF_OF, PURPOSE);
                assertNotNull(report);
                status = report.getStatus();
            }
        } while (IN_PROGRESS.equals(status));
    }
}
