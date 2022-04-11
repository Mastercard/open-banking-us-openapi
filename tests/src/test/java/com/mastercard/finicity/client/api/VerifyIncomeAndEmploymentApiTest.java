package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.*;
import com.mastercard.finicity.client.test.BaseAppKeyAppTokenTest;
import com.mastercard.finicity.client.test.utils.ConsumerUtils;
import com.mastercard.finicity.client.test.ModelFactory;
import com.mastercard.finicity.client.test.utils.PayStatementUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class VerifyIncomeAndEmploymentApiTest extends BaseAppKeyAppTokenTest {

    private static String existingAssetId;
    private final VerifyIncomeAndEmploymentApi api = new VerifyIncomeAndEmploymentApi(apiClient);

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
    void generateVOIReportTest() {
        try {
            var constraints = new ReportConstraints();
            var reportData = api.generateVOIReport(CUSTOMER_ID, constraints, null);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.VOI, reportData.getType());
        } catch (ApiException e) {
            logApiException(e);
            fail();
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
            logApiException(e);
            fail();
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
            logApiException(e);
            fail();
        }
    }

    @Test
    void generatePayStatementReportTest() {
        try {
            var constraints = new PayStatementReportConstraints().paystatementReport(new PayStatementData().addAssetIdsItem(existingAssetId));
            // This report with have the 'failure' status since the asset uploaded isn't a valid statement
            var reportData = api.generatePayStatementReport(CUSTOMER_ID, constraints, null);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.PAYSTATEMENT, reportData.getType());
        } catch (ApiException e) {
            logApiException(e);
            fail();
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
            logApiException(e);
            fail();
        }
    }

    @Test
    void generateVOIEPaystubWithTXVerifyReportTest() {
        try {
            var voieWithInterviewData = new VOIEWithInterviewData().addTxVerifyInterviewItem(new TxVerifyInterview().assetId(existingAssetId));
            var constraints = new VOIEWithTXVerifyReportConstraints().voieWithInterviewData(voieWithInterviewData);
            var reportData = api.generateVOIEPaystubWithTXVerifyReport(CUSTOMER_ID, constraints, null);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.VOIETXVERIFY, reportData.getType());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void generateVOETransactionsReportTest() {
        try {
            var reportData = api.generateVOETransactionsReport(CUSTOMER_ID, new VOETransactionsReportConstraints(), null, null);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.VOETRANSACTIONS, reportData.getType());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }
}
