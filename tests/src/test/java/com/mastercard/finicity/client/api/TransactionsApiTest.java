package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.CustomerAccount;
import com.mastercard.finicity.client.model.ReportConstraints;
import com.mastercard.finicity.client.model.ReportType;
import com.mastercard.finicity.client.model.Transaction;
import com.mastercard.finicity.client.test.BaseTest;
import com.mastercard.finicity.client.test.utils.AccountUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.*;


class TransactionsApiTest extends BaseTest {

    private static final TransactionsApi api = new TransactionsApi(apiClient);
    private static String existingAccountId;
    private static Long existingTransactionId;

    private static final Long fromDate = LocalDateTime.now().minusYears(10).toEpochSecond(UTC);
    private static final Long toDate = LocalDateTime.now().toEpochSecond(UTC);

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

            // Find an existing transaction ID
            Optional<Transaction> transaction = api.getAllCustomerTransactions(CUSTOMER_ID, fromDate, toDate, null, null, null, true)
                    .getTransactions()
                    .stream()
                    .findFirst();
            if (transaction.isEmpty()) {
                fail();
            }
            existingTransactionId = transaction.get().getId();
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void generateTransactionsReportTest() {
        try {
            var reportData = api.generateTransactionsReport(CUSTOMER_ID, toDate, new ReportConstraints(), null, null, true);
            assertEquals("inProgress", reportData.getStatus());
            assertEquals(ReportType.TRANSACTIONS, reportData.getType());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void getAllCustomerTransactionsTest() {
        try {
            var transactions = api.getAllCustomerTransactions(CUSTOMER_ID, fromDate, toDate, null, null, null, true);
            assertTrue(transactions.getTransactions().size() > 0);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void getCustomerAccountTransactionsTest() {
        try {
            var transactions = api.getCustomerAccountTransactions(CUSTOMER_ID, existingAccountId, fromDate, toDate, null, null, null, true);
            assertTrue(transactions.getTransactions().size() > 0);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void getCustomerTransactionTest() {
        try {
            var transaction = api.getCustomerTransaction(CUSTOMER_ID, existingTransactionId);
            assertNotNull(transaction);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }
}
