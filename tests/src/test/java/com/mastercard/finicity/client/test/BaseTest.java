package com.mastercard.finicity.client.test;

import com.google.gson.Gson;
import com.mastercard.finicity.client.ApiClient;
import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.Configuration;
import com.mastercard.finicity.client.model.ErrorMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Prerequisites:
 * 1. Accounts from "profile_09" were added using Finicity Connect (see: https://docs.finicity.com/test-the-apis/#test-the-apis-3)
 * 2. "refreshCustomerAccounts" has been called
 */
public abstract class BaseTest {

    protected final static String PARTNER_ID = System.getProperty("partnerId");
    protected final static String PARTNER_SECRET = System.getProperty("partnerSecret");
    protected final static String APP_KEY = System.getProperty("appKey");
    protected final static String CUSTOMER_ID = System.getProperty("customerId");

    protected final static ApiClient apiClient = Configuration.getDefaultApiClient();

    @BeforeEach
    void setUp() {
        // Prism
        // apiClient.setBasePath("http://localhost:4010");

        // Client logging
        apiClient.setDebugging(true);
        apiClient.setConnectTimeout(120000);
        apiClient.setReadTimeout(120000);
    }

    protected static void logApiException(ApiException e) {
        System.err.println("Status code: " + e.getCode());
        System.err.println("Reason: " + e.getResponseBody());
        System.err.println("Response headers: " + e.getResponseHeaders());
    }

    private static ErrorMessage parseError(ApiException e) {
        return new Gson().fromJson(e.getResponseBody(), ErrorMessage.class);
    }

    protected static void assertErrorCodeEquals(String code, ApiException e) {
        assertEquals(code, parseError(e).getCode());
    }

    protected static void assertErrorMessageEquals(String message, ApiException e) {
        assertEquals(message, parseError(e).getMessage());
    }

    protected static void fail() {
        Assertions.fail("Shouldn't reach this line");
    }
}