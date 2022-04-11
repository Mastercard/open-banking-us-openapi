package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.CustomerAccount;
import com.mastercard.finicity.client.model.ReportConstraints;
import com.mastercard.finicity.client.model.ReportType;
import com.mastercard.finicity.client.test.utils.AccountUtils;
import com.mastercard.finicity.client.test.BaseAppKeyAppTokenTest;
import com.mastercard.finicity.client.test.utils.ConsumerUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class VerifyAssetsApiTest extends BaseAppKeyAppTokenTest {

    private final VerifyAssetsApi api = new VerifyAssetsApi(apiClient);
    private static String accountIds; // "5053253032 5053253033 ... 5053253038 5053253039"

    @BeforeAll
    protected static void beforeAll() {
        try {
            BaseAppKeyAppTokenTest.beforeAll();

            // A consumer is required to generate reports
            ConsumersApi consumersApi = new ConsumersApi(apiClient);
            ConsumerUtils.getOrCreateDefaultConsumer(consumersApi, CUSTOMER_ID);

            // Fetch some accounts IDs to be included in reports
            AccountsApi accountApi = new AccountsApi(apiClient);
            accountIds = AccountUtils.getCustomerAccounts(accountApi, CUSTOMER_ID)
                    .stream()
                    .map(CustomerAccount::getId)
                    .collect(Collectors.joining(" "));
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void generateAssetSummaryReportTest() {
        try {
            var reportConstraints = new ReportConstraints().accountIds(accountIds);
            var reportData = api.generateAssetSummaryReport(CUSTOMER_ID, reportConstraints, null);
            assertNotNull(reportData);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.ASSETSUMMARY, reportData.getType());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void generatePrequalificationReportTest() {
        try {
            var reportConstraints = new ReportConstraints().accountIds(accountIds);
            var reportData = api.generatePrequalificationReport(CUSTOMER_ID, reportConstraints, null);
            assertNotNull(reportData);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.PREQUALVOA, reportData.getType());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void generateVOAReportTest() {
        try {
            var reportConstraints = new ReportConstraints().accountIds(accountIds);
            var reportData = api.generateVOAReport(CUSTOMER_ID, reportConstraints, null, null);
            assertNotNull(reportData);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.VOA, reportData.getType());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void generateVOAWithIncomeReportTest() {
        try {
            var reportConstraints = new ReportConstraints().accountIds(accountIds);
            var reportData = api.generateVOAWithIncomeReport(CUSTOMER_ID, reportConstraints, null, null);
            assertNotNull(reportData);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.VOAHISTORY, reportData.getType());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }
}
