package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.CustomerAccount;
import com.mastercard.finicity.client.model.TestTxPushTransaction;
import com.mastercard.finicity.client.model.TxPushSubscriptionParameters;
import com.mastercard.finicity.client.model.TxPushSubscriptions;
import com.mastercard.finicity.client.test.BaseTest;
import com.mastercard.finicity.client.test.utils.AccountUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TxPushApiTest extends BaseTest {

    private final static TxPushApi api = new TxPushApi(apiClient);

    private static CustomerAccount existingAccount;
    private final static String WEB_HOOK_URL = "https://eo309tmtekubcq.m.pipedream.net/"; // Simply returns the "txpush_verification_code" value


    @BeforeAll
    protected static void beforeAll() {
        try {
            // Find an existing account for the tests
            existingAccount = AccountUtils.getCustomerAccounts(new AccountsApi(apiClient), CUSTOMER_ID).get(0);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void subscribeToTxPushNotificationsTest() {
        var subscriptions = subscribe();
        assertTrue(subscriptions.getSubscriptions().size() > 0);
    }

    @Test
    void createTxPushTestTransactionTest() {
        try {
            subscribe();
            var transaction = new TestTxPushTransaction()
                    .description("This is a test transaction")
                    .transactionDate(LocalDateTime.now().toEpochSecond(UTC))
                    .amount(new BigDecimal(100));
            var createdTransaction = api.createTxPushTestTransaction(CUSTOMER_ID, existingAccount.getId(), transaction);
            assertNotNull(createdTransaction);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void deleteTxPushSubscriptionTest() {
        var subscriptions = subscribe();
        subscriptions.getSubscriptions().forEach(record -> {
            try {
                api.deleteTxPushSubscription(CUSTOMER_ID, record.getId());
            } catch (ApiException e) {
                // <error><code>14025</code><message>Not Acceptable.: [application/json]</message></error>
                logApiException(e);
            }
        });
    }

    @Test
    void disableTxPushNotificationsTest() {
        try {
            subscribe();
            api.disableTxPushNotifications(CUSTOMER_ID, existingAccount.getId());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    private TxPushSubscriptions subscribe() {
        var subscriptions = new TxPushSubscriptions();
        try {
            var params = new TxPushSubscriptionParameters().callbackUrl(WEB_HOOK_URL);
            subscriptions = api.subscribeToTxPushNotifications(CUSTOMER_ID, existingAccount.getId(), params);
            var records = subscriptions.getSubscriptions();
            assertTrue(records.size() > 0);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
        return subscriptions;
    }

    @AfterAll
    static void afterAll() {
        // Delete created resources
        try {
            api.disableTxPushNotifications(CUSTOMER_ID, existingAccount.getId());
        } catch (ApiException e) {
            // 60007 / No active subscription found for a given account
            logApiException(e);
        }
    }
}
