package com.mastercard.finicity.client.test;

import com.mastercard.finicity.client.auth.ApiKeyAuth;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseAppKeyTest extends BaseTest {

    @BeforeEach
    void setUp() {
        super.setUp();

        // Add 'Finicity-App-Key'
        var appKey = (ApiKeyAuth) apiClient.getAuthentication("FinicityAppKey");
        appKey.setApiKey(APP_KEY);
    }
}