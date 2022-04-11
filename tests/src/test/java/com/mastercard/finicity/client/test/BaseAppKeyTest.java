package com.mastercard.finicity.client.test;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.auth.ApiKeyAuth;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseAppKeyTest extends BaseTest {

    @BeforeEach
    void setUp() throws ApiException {
        super.setUp();

        // Add 'Finicity-App-Key'
        ApiKeyAuth appKey = (ApiKeyAuth) BaseTest.apiClient.getAuthentication("FinicityAppKey");
        appKey.setApiKey(BaseTest.APP_KEY);
    }
}
