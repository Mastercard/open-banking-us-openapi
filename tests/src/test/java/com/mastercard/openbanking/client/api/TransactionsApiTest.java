package com.mastercard.openbanking.client.api;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.TransactionsReportConstraints;
import com.mastercard.openbanking.client.test.BaseTest;
import com.mastercard.openbanking.client.test.utils.AccountUtils;
import org.junit.jupiter.api.Assertions;
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
            // Find the last account ID
            var accounts = AccountUtils.getCustomerAccounts(new AccountsApi(apiClient), CUSTOMER_ID);
            var account = accounts.get(accounts.size() - 1);
            existingAccountId = account.getId();

            // Find an existing transaction ID
            var transaction = api.getAllCustomerTransactions(CUSTOMER_ID, fromDate, toDate, null, null, null, true)
                    .getTransactions()
                    .stream()
                    .findFirst();
            if (transaction.isEmpty()) {
                Assertions.fail();
            }
            existingTransactionId = transaction.get().getId();

            // Fetch some accounts IDs to be included in reports
            customerAccountList = AccountUtils.getCustomerAccountListString(new AccountsApi(apiClient), CUSTOMER_ID);
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void generateTransactionsReportTest() {
        try {
            var constraints = new TransactionsReportConstraints().accountIds(customerAccountList);
            var reportAck = api.generateTransactionsReport(CUSTOMER_ID,  constraints, null,fromDate,toDate, true);
            assertEquals("inProgress", reportAck.getStatus());
            assertEquals("transactions", reportAck.getType());
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
            Assertions.fail(e);
        }
    }

    @Test
    void getCustomerAccountTransactionsTest() {
        try {
            var transactions = api.getCustomerAccountTransactions(CUSTOMER_ID, existingAccountId, fromDate, toDate, null, null, null, true);
            assertTrue(transactions.getTransactions().size() > 0);
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getCustomerTransactionTest() {
        try {
            var transaction = api.getCustomerTransaction(CUSTOMER_ID, existingTransactionId);
            assertNotNull(transaction);
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void loadHistoricTransactionsForCustomerAccountTest() {
        try {
            api.loadHistoricTransactionsForCustomerAccount(CUSTOMER_ID, existingAccountId);
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }
}
