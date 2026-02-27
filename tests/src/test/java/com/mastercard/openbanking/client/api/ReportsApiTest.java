package com.mastercard.openbanking.client.api;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.*;
import com.mastercard.openbanking.client.test.BaseTest;
import com.mastercard.openbanking.client.test.ModelFactory;
import com.mastercard.openbanking.client.test.utils.AccountUtils;
import com.mastercard.openbanking.client.test.utils.ConsumerUtils;
import com.mastercard.openbanking.client.test.utils.PayStatementUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private final static Set<String> NON_FINAL_STATUS = Set.of("inProgress", "editing");
    private static final String SUCCESS_STATUS = "success";
    private final static String ON_BEHALF_OF = "Someone";
    private final static String PURPOSE = "99";
    private static String uniqueTransactionId;

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
                            .statementIndex(1)
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
                var fromDate = LocalDateTime.now().minusYears(2).toEpochSecond(UTC);
                var reportAck = transactionsApi.generateTransactionsReport(CUSTOMER_ID, new TransactionsReportConstraints(), null, fromDate,toDate,true);
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
            Report report = api.getReportByConsumer(consumerId, reportId, PURPOSE, ON_BEHALF_OF);
            assertNotNull(report);
            status = getReportStatus(report);
        } while (NON_FINAL_STATUS.contains(status));
        do {
            System.out.println("Waiting for report " + reportId + " by customer ID ...");
            Thread.sleep(5000);
            var report = api.getReportByCustomer(CUSTOMER_ID, reportId, PURPOSE, ON_BEHALF_OF);
            assertNotNull(report);
            status = getReportStatus(report);
        } while (NON_FINAL_STATUS.contains(status));
        assertEquals(SUCCESS_STATUS, status, "Report status is: failure");
    }



    private static String getReportStatus(Report reportInstance) throws IllegalArgumentException {
        System.out.println("reportInstance : "+reportInstance);
    Object report = reportInstance.getActualInstance();
    System.out.println("Report type while getReportStatus: " + report.getClass()+" END");
    if (report instanceof CashFlowReport) {
        return ((CashFlowReport) report).getStatus();
    }

    if (report instanceof PayStatementReport) {
        return ((PayStatementReport) report).getStatus();
    }

    if (report instanceof PrequalificationReport) {
        return ((PrequalificationReport) report).getStatus();
    }

    if (report instanceof StatementReport) {
        return ((StatementReport) report).getStatus();
    }

    if (report instanceof TransactionsReport) {
        return ((TransactionsReport) report).getStatus();
    }

    if (report instanceof VOAReport) {
        return ((VOAReport) report).getStatus();
    }

    if (report instanceof VOAWithIncomeReport) {
        return ((VOAWithIncomeReport) report).getStatus();
    }

    if (report instanceof VOEPayrollReport) {
        return ((VOEPayrollReport) report).getStatus();
    }

    if (report instanceof VOETransactionsReport) {
        return ((VOETransactionsReport) report).getStatus();
    }

    if (report instanceof VOIEPayrollReport) {
        return ((VOIEPayrollReport) report).getStatus();
    }

    if (report instanceof VOIEPaystubReport) {
        return ((VOIEPaystubReport) report).getStatus();
    }

    if (report instanceof VOIEPaystubWithTXVerifyReport) {
        return ((VOIEPaystubWithTXVerifyReport) report).getStatus();
    }

    if (report instanceof VOIReport) {
        return ((VOIReport) report).getStatus();
    }

    if (report instanceof AFBalanceAnalyticsReport) {
        return ((AFBalanceAnalyticsReport) report).getStatus();
    }

    if (report instanceof AFCashFlowAnalyticsReport) {
        return ((AFCashFlowAnalyticsReport) report).getStatus();
    }












    throw new IllegalArgumentException("Invalid report type"+report.getClass()+" END");
}
}
