package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.ReportConstraints;
import com.mastercard.finicity.client.model.ReportType;
import com.mastercard.finicity.client.test.BaseTest;
import com.mastercard.finicity.client.test.utils.AccountUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.*;


class TransactionsApiTest extends BaseTest {

    private static final TransactionsApi api = new TransactionsApi(apiClient);
    private static String existingAccountId;
    private static Long existingTransactionId;
    private static String customerAccountList;

    private static final Long fromDate = LocalDateTime.now().minusYears(10).toEpochSecond(UTC);
    private static final Long toDate = LocalDateTime.now().toEpochSecond(UTC);

    @BeforeAll
    protected static void beforeAll() {
        try {
            // Find an existing account ID
            var account = AccountUtils.getCustomerAccounts(new AccountsApi(apiClient), CUSTOMER_ID)
                    .stream()
                    .findFirst();
            if (account.isEmpty()) {
                fail();
            }
            existingAccountId = account.get().getId();

            // Find an existing transaction ID
            var transaction = api.getAllCustomerTransactions(CUSTOMER_ID, fromDate, toDate, null, null, null, true)
                    .getTransactions()
                    .stream()
                    .findFirst();
            if (transaction.isEmpty()) {
                fail();
            }
            existingTransactionId = transaction.get().getId();

            // Fetch some accounts IDs to be included in reports
            customerAccountList = AccountUtils.getCustomerAccountListString(new AccountsApi(apiClient), CUSTOMER_ID);
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void generateTransactionsReportTest() {
        try {
            var constraints = new ReportConstraints().accountIds(customerAccountList);
            var reportData = api.generateTransactionsReport(CUSTOMER_ID, toDate, constraints, null, true);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.TRANSACTIONS, reportData.getType());
        } catch (ApiException e) {
            // Status code: 429, Reason: Too Many Requests
            logApiException(e);
        }
    }

    @Test
    void getAllCustomerTransactionsTest() {
        try {
            var transactions = api.getAllCustomerTransactions(CUSTOMER_ID, fromDate, toDate, null, null, null, true);
            assertTrue(transactions.getTransactions().size() > 0);
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getCustomerAccountTransactionsTest() {
        try {
            var transactions = api.getCustomerAccountTransactions(CUSTOMER_ID, existingAccountId, fromDate, toDate, null, null, null, true);
            assertTrue(transactions.getTransactions().size() > 0);
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getCustomerTransactionTest() {
        try {
            var transaction = api.getCustomerTransaction(CUSTOMER_ID, existingTransactionId);
            assertNotNull(transaction);
        } catch (ApiException e) {
            fail(e);
        }
    }
}
