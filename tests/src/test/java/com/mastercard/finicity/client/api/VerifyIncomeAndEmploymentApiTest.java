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

import static org.junit.jupiter.api.Assertions.assertEquals;


class VerifyIncomeAndEmploymentApiTest extends BaseTest {

    private static String existingAssetId;
    private static String customerAccountList;
    private final VerifyIncomeAndEmploymentApi api = new VerifyIncomeAndEmploymentApi(apiClient);

    @BeforeAll
    protected static void beforeAll() {
        try {
            // A consumer is required to generate reports
            ConsumerUtils.getOrCreateDefaultConsumer(new ConsumersApi(apiClient), CUSTOMER_ID);

            // Upload a pay statement for the tests
            existingAssetId = PayStatementUtils.storeAsset(new PayStatementsApi(apiClient), CUSTOMER_ID);

            // Fetch some accounts IDs to be included in reports
            customerAccountList = AccountUtils.getCustomerAccountListString(new AccountsApi(apiClient), CUSTOMER_ID);
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void generateVOIReportTest() {
        try {
            var constraints = new ReportConstraints().accountIds(customerAccountList);
            var reportData = api.generateVOIReport(CUSTOMER_ID, constraints, null);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.VOI, reportData.getType());
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void generateVOEPayrollReportTest() {
        try {
            var constraints = new PayrollReportConstraints().payrollData(ModelFactory.newPayrollData());
            var reportData = api.generateVOEPayrollReport(CUSTOMER_ID, constraints, null);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.VOEPAYROLL, reportData.getType());
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void refreshVOIEPayrollReportTest() {
        try {
            var constraints = new PayrollReportConstraints().payrollData(ModelFactory.newPayrollData());
            var reportData = api.refreshVOIEPayrollReport(CUSTOMER_ID, constraints, null);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.VOIEPAYROLL, reportData.getType());
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void generatePayStatementReportTest() {
        try {
            var constraints = new PayStatementReportConstraints().paystatementReport(new PayStatementData().addAssetIdsItem(existingAssetId));
            // This report's final status will be 'failure' since the asset uploaded isn't a valid statement
            var reportData = api.generatePayStatementReport(CUSTOMER_ID, constraints, null);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.PAYSTATEMENT, reportData.getType());
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void generatePayStatementReportTest_UnknownAsset() {
        try {
            var constraints = new PayStatementReportConstraints().paystatementReport(new PayStatementData().addAssetIdsItem("Unknown ID"));
            api.generatePayStatementReport(CUSTOMER_ID, constraints, null);
            fail();
        } catch (ApiException e) {
            // {"code":10100,"message":"PAY_STATEMENT_REPORT: Asset(s) not found"}
            logApiException(e);
        }
    }

    @Test
    void generateVOIEPaystubReportTest() {
        try {
            var voieWithStatementData = new VOIEWithStatementData().addAssetIdsItem(existingAssetId);
            var constraints = new VOIEWithStatementReportConstraints().voieWithStatementData(voieWithStatementData);
            var reportData = api.generateVOIEPaystubReport(CUSTOMER_ID, constraints, null);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.VOIETXVERIFY, reportData.getType());
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void generateVOIEPaystubWithTXVerifyReportTest() {
        try {
            var voieWithInterviewData = new VOIEWithInterviewData().addTxVerifyInterviewItem(new TxVerifyInterview().assetId(existingAssetId));
            var constraints = new VOIEWithTXVerifyReportConstraints()
                    .voieWithInterviewData(voieWithInterviewData)
                    .accountIds(customerAccountList);
            var reportData = api.generateVOIEPaystubWithTXVerifyReport(CUSTOMER_ID, constraints, null);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.VOIETXVERIFY, reportData.getType());
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void generateVOETransactionsReportTest() {
        try {
            var constraints = new VOETransactionsReportConstraints().accountIds(customerAccountList);
            var reportData = api.generateVOETransactionsReport(CUSTOMER_ID, constraints, null);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.VOETRANSACTIONS, reportData.getType());
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }
}
