package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.CustomerAccount;
import com.mastercard.finicity.client.model.StatementData;
import com.mastercard.finicity.client.model.StatementReportConstraints;
import com.mastercard.finicity.client.test.BaseTest;
import com.mastercard.finicity.client.test.utils.AccountUtils;
import com.mastercard.finicity.client.test.utils.ConsumerUtils;
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
            // A consumer is required to generate reports
            ConsumerUtils.getOrCreateDefaultConsumer(new ConsumersApi(apiClient), CUSTOMER_ID);

            // Find an existing account ID
            Optional<CustomerAccount> account = AccountUtils.getCustomerAccounts(new AccountsApi(apiClient), CUSTOMER_ID)
                    .stream()
                    .findFirst();
            if (account.isEmpty()) {
                fail();
            }
            existingAccountId = account.get().getId();
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void generateStatementReportTest() {
        try {
            var constraints = new StatementReportConstraints()
                    .statementReportData(new StatementData()
                            .statementIndex(1)
                            .accountId(Long.valueOf(existingAccountId)));
            var reportAck = api.generateStatementReport(CUSTOMER_ID, constraints, null);
            assertEquals("inProgress", reportAck.getStatus());
            assertEquals("statement", reportAck.getType());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getCustomerAccountStatementTest() {
        try {
            var pdf = api.getCustomerAccountStatement(CUSTOMER_ID, existingAccountId, 1, null);
            assertNotNull(pdf);
        } catch (ApiException e) {
            fail(e);
        }
    }
}
