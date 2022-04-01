package com.mastercard.finicity.client.test;

import com.mastercard.finicity.client.auth.ApiKeyAuth;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseAppKeyTest extends BaseTest {

    @BeforeAll
    protected static void beforeAll() {
        BaseTest.beforeAll();
        addAppKey();
    }

    /**
     * Add 'Finicity-App-Key'
     */
    private static void addAppKey() {
        var appKey = (ApiKeyAuth) apiClient.getAuthentication("FinicityAppKey");
        appKey.setApiKey(APP_KEY);
    }
}