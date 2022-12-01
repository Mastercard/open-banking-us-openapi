package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.*;
import com.mastercard.finicity.client.test.BaseTest;
import com.mastercard.finicity.client.test.ModelFactory;
import com.mastercard.finicity.client.test.utils.AccountUtils;
import com.mastercard.finicity.client.test.utils.ConsumerUtils;
import com.mastercard.finicity.client.test.utils.PayStatementUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReportsApiTest extends BaseTest {

    private final static ReportsApi api = new ReportsApi(apiClient);

    private final VerifyAssetsApi verifyAssetsApi = new VerifyAssetsApi(apiClient);
    private final VerifyIncomeAndEmploymentApi verifyIncomeAndEmploymentApi = new VerifyIncomeAndEmploymentApi(apiClient);
    private final TransactionsApi transactionsApi = new TransactionsApi(apiClient);
    private final CashFlowApi cashFlowApi = new CashFlowApi(apiClient);
    private final BankStatementsApi bankStatementsApi = new BankStatementsApi(apiClient);

    private static String existingAssetId;
    private static String existingAccountId;
    private static String consumerId;
    private static Map<String, String> reportsByType; // type <-> id

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

            // Find an existing account ID
            Optional<CustomerAccount> account = AccountUtils.getCustomerAccounts(new AccountsApi(apiClient), CUSTOMER_ID)
                    .stream()
                    .findFirst();
            if (account.isEmpty()) {
                fail();
            }
            existingAccountId = account.get().getId();

            // Fetch existing reports
            var reports = new ReportsApi(apiClient).getReportsByCustomerId(CUSTOMER_ID, null);
            reportsByType = reports.getReports()
                    .stream()
                    .collect(Collectors.toMap(ReportSummary::getType, ReportSummary::getId, (key1, key2) -> key1));
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getReportsByConsumerTest() {
        try {
            var reports = api.getReportsByConsumerId(consumerId, null);
            assertNotNull(reports);
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getReportsByCustomerTest() {
        try {
            var reports = api.getReportsByCustomerId(CUSTOMER_ID, null);
            assertNotNull(reports);
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getPrequalificationCRAReportByConsumerOrCustomerTest() throws Exception {
        try {
            var reportId = reportsByType.get("preQualVoa");
            if (reportId == null) {
                // Create a report the first time
                var reportAck = verifyAssetsApi.generatePrequalificationCRAReport(CUSTOMER_ID, new PrequalificationReportConstraints(), null);
                reportId = reportAck.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getVOAReportByConsumerOrCustomerTest() throws Exception {
        try {
            var reportId = reportsByType.get("voa");
            if (reportId == null) {
                // Create a report the first time
                var reportAck = verifyAssetsApi.generateVOAReport(CUSTOMER_ID, new VOAReportConstraints(), null);
                reportId = reportAck.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getVOAWithIncomeReportByConsumerOrCustomerTest() throws Exception {
        try {
            var reportId = reportsByType.get("voaHistory");
            if (reportId == null) {
                // Create a report the first time
                var reportAck = verifyAssetsApi.generateVOAWithIncomeReport(CUSTOMER_ID, new VOAWithIncomeReportConstraints(), null);
                reportId = reportAck.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getPrequalificationNonCRAReportByConsumerOrCustomerTest() throws Exception {
        try {
            var reportId = reportsByType.get("assetSummary");
            if (reportId == null) {
                // Create a report the first time
                var reportAck = verifyAssetsApi.generatePrequalificationNonCRAReport(CUSTOMER_ID, new PrequalificationReportConstraints(), null);
                reportId = reportAck.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getVOIReportByConsumerOrCustomerTest() throws Exception {
        try {
            var reportId = reportsByType.get("voi");
            if (reportId == null) {
                // Create a report the first time
                var reportAck = verifyIncomeAndEmploymentApi.generateVOIReport(CUSTOMER_ID, new VOIReportConstraints(), null);
                reportId = reportAck.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getVOEPayrollReportByConsumerOrCustomerTest() throws Exception {
        try {
            var reportId = reportsByType.get("voePayroll");
            if (reportId == null) {
                // Create a report the first time
                var constraints = new PayrollReportConstraints().payrollData(ModelFactory.newPayrollData());
                var reportAck = verifyIncomeAndEmploymentApi.generateVOEPayrollReport(CUSTOMER_ID, constraints, null);
                reportId = reportAck.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getPayStatementReportByConsumerOrCustomerTest() throws Exception {
        try {
            var reportId = reportsByType.get("paystatement");
            if (reportId == null) {
                // Create a report the first time
                var constraints = new PayStatementReportConstraints().paystatementReport(new PayStatementData().addAssetIdsItem(existingAssetId));
                var reportAck = verifyIncomeAndEmploymentApi.generatePayStatementReport(CUSTOMER_ID, constraints, null);
                reportId = reportAck.getId();
            }
            // This report's final status will be 'failure' since the asset uploaded isn't a valid statement
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getStatementReportByConsumerOrCustomerTest() throws Exception {
        try {
            var reportId = reportsByType.get("statement");
            if (reportId == null) {
                // Create a report the first time
                var constraints = new StatementReportConstraints()
                        .statementReportData(new StatementData()
                            .index(1)
                            .accountId(Long.valueOf(existingAccountId)));
                var reportAck = bankStatementsApi.generateStatementReport(CUSTOMER_ID, constraints, null);
                reportId = reportAck.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getVOIEPaystubReportByConsumerOrCustomerTest() throws Exception {
        try {
            var reportId = reportsByType.get("voieTxVerify");
            if (reportId == null) {
                // Create a report the first time
                var voieWithStatementData = new VOIEWithStatementData().addAssetIdsItem(existingAssetId);
                var constraints = new VOIEReportConstraints().voieWithStatementData(voieWithStatementData);
                var reportAck = verifyIncomeAndEmploymentApi.generateVOIEPaystubReport(CUSTOMER_ID, constraints, null);
                reportId = reportAck.getId();
            }
            // This report's final status will be 'failure' since the asset uploaded isn't a valid statement
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getVOIEPaystubWithTXVerifyReportByConsumerOrCustomerTest() throws Exception {
        try {
            var reportId = reportsByType.get("voieTxVerify");
            if (reportId == null) {
                // Create a report the first time
                var voieWithInterviewData = new VOIEWithInterviewData().addTxVerifyInterviewItem(new TxVerifyInterview().assetId(existingAssetId));
                var constraints = new VOIEWithTXVerifyReportConstraints().voieWithInterviewData(voieWithInterviewData);
                var reportAck = verifyIncomeAndEmploymentApi.generateVOIEPaystubWithTXVerifyReport(CUSTOMER_ID, constraints, null);
                reportId = reportAck.getId();
            }
            // This report's final status will be 'failure' since the asset uploaded isn't a valid statement
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getTransactionsReportByConsumerOrCustomerTest() throws Exception {
        try {
            var reportId = reportsByType.get("transactions");
            if (reportId == null) {
                // Create a report the first time
                var toDate = LocalDateTime.now().toEpochSecond(UTC);
                var reportAck = transactionsApi.generateTransactionsReport(CUSTOMER_ID, toDate, new TransactionsReportConstraints(), null, true);
                reportId = reportAck.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getVOETransactionsReportByConsumerOrCustomerTest() throws Exception {
        try {
            var reportId = reportsByType.get("voeTransactions");
            if (reportId == null) {
                // Create a report the first time
                var reportAck = verifyIncomeAndEmploymentApi.generateVOETransactionsReport(CUSTOMER_ID, new VOETransactionsReportConstraints(), null);
                reportId = reportAck.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getCashFlowBusinessReportByConsumerOrCustomerTest() throws Exception {
        try {
            var reportId = reportsByType.get("cfrb");
            if (reportId == null) {
                // Create a report the first time
                var reportAck = cashFlowApi.generateCashFlowBusinessReport(CUSTOMER_ID, new CashFlowReportConstraints(), null);
                reportId = reportAck.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getCashFlowPersonalReportByConsumerOrCustomerTest() throws Exception {
        try {
            var reportId = reportsByType.get("cfrp");
            if (reportId == null) {
                // Create a report the first time
                var reportAck = cashFlowApi.generateCashFlowPersonalReport(CUSTOMER_ID, new CashFlowReportConstraints(), null);
                reportId = reportAck.getId();
            }
            fetchReport(reportId, consumerId);
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    private static void fetchReport(String reportId, String consumerId) throws Exception {
        String status;
        do {
            System.out.println("Fetching report " + reportId + " by consumer ID ...");
            Thread.sleep(5000);
            var report = api.getReportByConsumer(consumerId, reportId, ON_BEHALF_OF, PURPOSE);
            assertNotNull(report);
            status = report.getStatus();
        } while (IN_PROGRESS.equals(status));
        do {
            System.out.println("Waiting for report " + reportId + " by customer ID ...");
            Thread.sleep(5000);
            var report = api.getReportByCustomer(CUSTOMER_ID, reportId, ON_BEHALF_OF, PURPOSE);
            assertNotNull(report);
            status = report.getStatus();
        } while (IN_PROGRESS.equals(status));
    }
}
