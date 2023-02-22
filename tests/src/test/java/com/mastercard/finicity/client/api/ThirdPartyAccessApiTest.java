package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.ThirdPartyAccessKeyReceiptData;
import com.mastercard.finicity.client.model.ThirdPartyAccessReceipt;
import com.mastercard.finicity.client.test.BaseTest;
import com.mastercard.finicity.client.test.ModelFactory;
import com.mastercard.finicity.client.test.utils.AccountUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ThirdPartyAccessApiTest extends BaseTest {

    private static final ThirdPartyAccessApi api = new ThirdPartyAccessApi(apiClient);
    private static final AccountsApi accountApi = new AccountsApi(apiClient);

    private static String existingAccountId;

    @BeforeAll
    protected static void beforeAll() {
        try {
            // Load existing IDs to be used in the subsequent tests
            var existingAccount = AccountUtils.getCustomerAccounts(accountApi, CUSTOMER_ID).get(0);
            existingAccountId = existingAccount.getId();
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    public void generateThirdPartyAccessKeyTest() {
        try {
            var receiptData = generateThirdPartyAccessKey();
            var data = receiptData.getData();
            assertNotNull(data);
            assertTrue(data.size() > 0);
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    public void revokeThirdPartyAccessKeyTest() {
        try {
            var receiptData = generateThirdPartyAccessKey();
            var consentReceiptId = extractReceiptId(receiptData);
            api.revokeThirdPartyAccessKey(consentReceiptId);
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    public void updateThirdPartyAccessKeyTest() {
        try {
            // GIVEN
            var receiptData = generateThirdPartyAccessKey();
            var consentReceiptId = extractReceiptId(receiptData);

            // WHEN
            var newData = ModelFactory.newThirdPartyAccessKeyData("single", existingAccountId, CUSTOMER_ID, PARTNER_ID);
            var newReceiptData = api.updateThirdPartyAccessKey(consentReceiptId, newData);

            // THEN
            var receipt = extractReceipt(newReceiptData);
            var products = receipt.getProducts();
            assertNotNull(products);
            assertTrue(products.size() > 0);
            assertEquals("single", products.get(0).getAccessPeriod().getType());
        } catch (ApiException e) {
            fail(e);
        }
    }

    private static ThirdPartyAccessKeyReceiptData generateThirdPartyAccessKey() throws ApiException {
        var data = ModelFactory.newThirdPartyAccessKeyData("timeframe", existingAccountId, CUSTOMER_ID, PARTNER_ID);
        return api.generateThirdPartyAccessKey(data);
    }

    private static ThirdPartyAccessReceipt extractReceipt(ThirdPartyAccessKeyReceiptData receiptData) {
        var data = receiptData.getData();
        assertNotNull(data);
        var receipt = data.get(0).getReceipt();
        assertNotNull(receipt);
        return receipt;
    }

    private static String extractReceiptId(ThirdPartyAccessKeyReceiptData receiptData) {
        var receipt = extractReceipt(receiptData);
        var consentReceiptId = receipt.getReceiptId();
        assertNotNull(consentReceiptId);
        return consentReceiptId;
    }
}