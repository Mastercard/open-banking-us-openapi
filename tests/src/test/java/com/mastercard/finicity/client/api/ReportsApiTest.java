package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.*;
import com.mastercard.finicity.client.test.BaseTest;
import com.mastercard.finicity.client.test.ModelFactory;
import com.mastercard.finicity.client.test.utils.ConsumerUtils;
import com.mastercard.finicity.client.test.utils.PayStatementUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReportsApiTest extends BaseTest {

    private final static ReportsApi api = new ReportsApi(apiClient);

    private final VerifyAssetsApi verifyAssetsApi = new VerifyAssetsApi(apiClient);
    private final VerifyIncomeAndEmploymentApi verifyIncomeAndEmploymentApi = new VerifyIncomeAndEmploymentApi(apiClient);
    private final TransactionsApi transactionsApi = new TransactionsApi(apiClient);
    private final CashFlowApi cashFlowApi = new CashFlowApi(apiClient);

    private static String existingAssetId;
    private static String consumerId;
    private static Map<ReportType, String> reportsByType; // type <-> id

    private final static String IN_PROGRESS = "inProgress";
    private final static String ON_BEHALF_OF = "Someone";
    private final static String PURPOSE = "99";

    @BeforeAll
    protected static void beforeAll() {
        try {
            // A consumer is required to generate reports
            consumerId = ConsumerUtils.getOrCreateDefaultConsumer(new ConsumersApi(apiClient), CUSTOMER_ID);

            // Upload a pay statement for the tests
            existingAssetId = PayStatementUtils.storeAsset(new PayStatementsApi(apiClient), CUSTOMER_ID);

            // Fetch existing reports
            ReportSummaries reports = new ReportsApi(apiClient).getReportsByCustomer(CUSTOMER_ID, null);
            reportsByType = reports.getReports()
                    .stream()
                    .collect(Collectors.toMap(ReportSummary::getType, ReportSummary::getId, (key1, key2) -> key1));
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void getPrequalificationReportByCustomerTest() throws Exception {
        try {
            var reportId = reportsByType.get(ReportType.PREQUALVOA);
            if (reportId == null) {
                // Create a report the first time
                var reportData = verifyAssetsApi.generatePrequalificationReport(CUSTOMER_ID, new ReportConstraints(), null);
                reportId = reportData.getId();
            }
            fetchPrequalificationReportReport(reportId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getVOAReportByConsumerTest() throws Exception {
        try {
            var reportId = reportsByType.get(ReportType.VOA);
            if (reportId == null) {
                // Create a report the first time
                var reportData = verifyAssetsApi.generateVOAReport(CUSTOMER_ID, new ReportConstraints(), null);
                reportId = reportData.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getVOAWithIncomeReportByConsumerTest() throws Exception {
        try {
            var reportId = reportsByType.get(ReportType.VOAHISTORY);
            if (reportId == null) {
                // Create a report the first time
                var reportData = verifyAssetsApi.generateVOAWithIncomeReport(CUSTOMER_ID, new ReportConstraints(), null);
                reportId = reportData.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getPrequalificationNonCRAReportByConsumerTest() throws Exception {
        try {
            var reportId = reportsByType.get(ReportType.ASSETSUMMARY);
            if (reportId == null) {
                // Create a report the first time
                var reportData = verifyAssetsApi.generatePrequalificationNonCRAReport(CUSTOMER_ID, new ReportConstraints(), null);
                reportId = reportData.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getReportsByConsumerTest() {
        try {
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
            var reportId = reportsByType.get(ReportType.VOI);
            if (reportId == null) {
                // Create a report the first time
                var reportData = verifyIncomeAndEmploymentApi.generateVOIReport(CUSTOMER_ID, new ReportConstraints(), null);
                reportId = reportData.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getVOEPayrollReportByConsumerTest() throws Exception {
        try {
            var reportId = reportsByType.get(ReportType.VOEPAYROLL);
            if (reportId == null) {
                // Create a report the first time
                var constraints = new PayrollReportConstraints().payrollData(ModelFactory.newPayrollData());
                var reportData = verifyIncomeAndEmploymentApi.generateVOEPayrollReport(CUSTOMER_ID, constraints, null);
                reportId = reportData.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getPayStatementReportByConsumerTest() {
        try {
            var reportId = reportsByType.get(ReportType.PAYSTATEMENT);
            if (reportId == null) {
                // Create a report the first time
                var constraints = new PayStatementReportConstraints().paystatementReport(new PayStatementData().addAssetIdsItem(existingAssetId));
                var reportData = verifyIncomeAndEmploymentApi.generatePayStatementReport(CUSTOMER_ID, constraints, null);
                reportId = reportData.getId();
            }
            // This report's final status will be 'failure' since the asset uploaded isn't a valid statement
            var report = api.getReportByConsumer(consumerId, reportId, ON_BEHALF_OF, PURPOSE);
            assertNotNull(report);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getVOIEPaystubReportByConsumerTest() {
        try {
            var reportId = reportsByType.get(ReportType.VOIETXVERIFY);
            if (reportId == null) {
                // Create a report the first time
                var voieWithStatementData = new VOIEWithStatementData().addAssetIdsItem(existingAssetId);
                var constraints = new VOIEWithStatementReportConstraints().voieWithStatementData(voieWithStatementData);
                var reportData = verifyIncomeAndEmploymentApi.generateVOIEPaystubReport(CUSTOMER_ID, constraints, null);
                reportId = reportData.getId();
            }
            // This report's final status will be 'failure' since the asset uploaded isn't a valid statement
            var report = api.getReportByConsumer(consumerId, reportId, ON_BEHALF_OF, PURPOSE);
            assertNotNull(report);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getVOIEPaystubWithTXVerifyReportByConsumerTest() {
        try {
            var reportId = reportsByType.get(ReportType.VOIETXVERIFY);
            if (reportId == null) {
                // Create a report the first time
                var voieWithInterviewData = new VOIEWithInterviewData().addTxVerifyInterviewItem(new TxVerifyInterview().assetId(existingAssetId));
                var constraints = new VOIEWithTXVerifyReportConstraints().voieWithInterviewData(voieWithInterviewData);
                var reportData = verifyIncomeAndEmploymentApi.generateVOIEPaystubWithTXVerifyReport(CUSTOMER_ID, constraints, null);
                reportId = reportData.getId();
            }
            // This report's final status will be 'failure' since the asset uploaded isn't a valid statement
            var report = api.getReportByConsumer(consumerId, reportId, ON_BEHALF_OF, PURPOSE);
            assertNotNull(report);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getTransactionReportByConsumerTest() throws Exception {
        try {
            var reportId = reportsByType.get(ReportType.TRANSACTIONS);
            if (reportId == null) {
                // Create a report the first time
                var toDate = LocalDateTime.now().toEpochSecond(UTC);
                var reportData = transactionsApi.generateTransactionsReport(CUSTOMER_ID, toDate, new ReportConstraints(), null, true);
                reportId = reportData.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getVOETransactionsReportByConsumerTest() throws Exception {
        try {
            var reportId = reportsByType.get(ReportType.VOETRANSACTIONS);
            if (reportId == null) {
                // Create a report the first time
                var reportData = verifyIncomeAndEmploymentApi.generateVOETransactionsReport(CUSTOMER_ID, new VOETransactionsReportConstraints(), null);
                reportId = reportData.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getCashFlowBusinessReportByConsumerTest() throws Exception {
        try {
            var reportId = reportsByType.get(ReportType.CFRB);
            if (reportId == null) {
                // Create a report the first time
                var reportData = cashFlowApi.generateCashFlowBusinessReport(CUSTOMER_ID, new ReportConstraints(), null);
                reportId = reportData.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getCashFlowPersonalReportByConsumerTest() throws Exception {
        try {
            var reportId = reportsByType.get(ReportType.CFRP);
            if (reportId == null) {
                // Create a report the first time
                var reportData = cashFlowApi.generateCashFlowPersonalReport(CUSTOMER_ID, new ReportConstraints(), null);
                reportId = reportData.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
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
