package com.mastercard.finicity.client.test;

import com.google.gson.Gson;
import com.mastercard.finicity.client.ApiClient;
import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.Configuration;
import com.mastercard.finicity.client.api.CustomersApi;
import com.mastercard.finicity.client.model.ErrorMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Prerequisites:
 * 1. Accounts from "FinBank Profiles - A" / "profile_03" were added using Finicity Connect (see: https://docs.finicity.com)
 * 2. "refreshCustomerAccounts" has been called
 */
public abstract class BaseTest {

    protected final static String PARTNER_ID = System.getProperty("partnerId");
    protected final static String PARTNER_SECRET = System.getProperty("partnerSecret");
    protected final static String APP_KEY = System.getProperty("appKey");
    protected final static String CUSTOMER_ID = System.getProperty("customerId");

    protected final static ApiClient apiClient = Configuration.getDefaultApiClient();

    protected final static List<String> createdCustomerIds = new ArrayList<>();
    protected final static CustomersApi customersApi = new CustomersApi(apiClient);

    private final static FinicityAuthInterceptor authInterceptor = new FinicityAuthInterceptor(PARTNER_ID, PARTNER_SECRET, APP_KEY);

    static {
        if (Arrays.asList(PARTNER_ID, PARTNER_SECRET, APP_KEY, CUSTOMER_ID).contains(null)) {
            throw new InvalidParameterException("Required options: -DpartnerId=*** -DpartnerSecret=*** -DappKey=*** -DcustomerId=***");
        }

        apiClient.setDebugging(true);  // Client logging
        apiClient.setConnectTimeout(240000);
        apiClient.setReadTimeout(240000);
        apiClient.setHttpClient(
                apiClient.getHttpClient()
                        .newBuilder()
                        .addInterceptor(authInterceptor)  // Handle Finicity authentication
                        .build()
        );
    }

    protected static void logApiException(ApiException e) {
        System.err.println("Status code: " + e.getCode());
        System.err.println("Reason: " + e.getResponseBody());
        System.err.println("Response headers: " + e.getResponseHeaders());
    }

    private static ErrorMessage parseError(ApiException e) {
        return new Gson().fromJson(e.getResponseBody(), ErrorMessage.class);
    }

    protected static void assertErrorCodeEquals(String expectedCode, ApiException e) {
        assertEquals(expectedCode, parseError(e).getCode().toString());
    }

    protected static void assertErrorCodeEquals(Integer expectedCode, ApiException e) {
        Integer actualCode = ((Double)parseError(e).getCode()).intValue();
        assertEquals(expectedCode, actualCode);
    }

    protected static void assertErrorMessageEquals(String message, ApiException e) {
        assertEquals(message, parseError(e).getMessage());
    }

    protected static void fail() {
        Assertions.fail("Shouldn't reach this line");
    }

    protected static void fail(ApiException e) {
        logApiException(e);
        Assertions.fail("Code: " + e.getCode() + ", response: " + e.getResponseBody());
    }

    @AfterAll
    static void afterAll() {
        // Delete created resources
        var toDelete = new ArrayList<>(createdCustomerIds);
        createdCustomerIds.clear();
        toDelete.forEach(id -> {
            try {
                customersApi.deleteCustomer(id);
            } catch (ApiException e) {
                fail(e);
            }
        });
    }
}
