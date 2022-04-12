package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.CustomerAccount;
import com.mastercard.finicity.client.model.ReportType;
import com.mastercard.finicity.client.model.StatementData;
import com.mastercard.finicity.client.model.StatementReportConstraints;
import com.mastercard.finicity.client.test.BaseTest;
import com.mastercard.finicity.client.test.utils.AccountUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class BankStatementsApiTest extends BaseTest {

    private final BankStatementsApi api = new BankStatementsApi(apiClient);
    private static String existingAccountId;

    @BeforeAll
    protected static void beforeAll() {
        try {
            // Find an existing account ID
            Optional<CustomerAccount> account = AccountUtils.getCustomerAccounts(new AccountsApi(apiClient), CUSTOMER_ID)
                    .stream()
                    .findFirst();
            if (account.isEmpty()) {
                fail();
            }
            existingAccountId = account.get().getId();
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void generateStatementReportTest() {
        try {
            var constraints = new StatementReportConstraints()
                    .statementReportData(new StatementData()
                            .index(1)
                            .accountId(existingAccountId));
            var reportData = api.generateStatementReport(CUSTOMER_ID, constraints, null);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.STATEMENT, reportData.getType());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void getCustomerAccountStatementTest() {
        try {
            var pdf = api.getCustomerAccountStatement(CUSTOMER_ID, existingAccountId, 1);
            assertNotNull(pdf);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }
}
