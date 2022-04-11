package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.CustomerAccount;
import com.mastercard.finicity.client.model.ReportConstraints;
import com.mastercard.finicity.client.model.ReportType;
import com.mastercard.finicity.client.test.AccountUtils;
import com.mastercard.finicity.client.test.BaseAppKeyAppTokenTest;
import com.mastercard.finicity.client.test.ConsumerUtils;
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
            // A consumer is required to generate some of the reports
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
    public void generateAssetSummaryReportTest() {
        try {
            var reportConstraints = new ReportConstraints().accountIds(accountIds);
            var response = api.generateAssetSummaryReport(CUSTOMER_ID, null, reportConstraints);
            assertNotNull(response);
            assertEquals("inProgress", response.getStatus());
            assertEquals(ReportType.ASSETSUMMARY, response.getType());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    public void generatePrequalificationReportTest() {
        try {
            var reportConstraints = new ReportConstraints().accountIds(accountIds);
            var response = api.generatePrequalificationReport(CUSTOMER_ID, null, reportConstraints);
            assertNotNull(response);
            assertEquals("inProgress", response.getStatus());
            assertEquals(ReportType.PREQUALVOA, response.getType());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    public void generateVOAReportTest() {
        try {
            var reportConstraints = new ReportConstraints().accountIds(accountIds);
            var response = api.generateVOAReport(CUSTOMER_ID, null, null, reportConstraints);
            assertNotNull(response);
            assertEquals("inProgress", response.getStatus());
            assertEquals(ReportType.VOA, response.getType());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    public void generateVOAWithIncomeReportTest() {
        try {
            var reportConstraints = new ReportConstraints().accountIds(accountIds);
            var response = api.generateVOAWithIncomeReport(CUSTOMER_ID, null, null, reportConstraints);
            assertNotNull(response);
            assertEquals("inProgress", response.getStatus());
            assertEquals(ReportType.VOAHISTORY, response.getType());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }
}
