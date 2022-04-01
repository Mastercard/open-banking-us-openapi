package org.openapitools.client.api;

import com.mastercard.finicity.client.ApiClient;
import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.Configuration;
import com.mastercard.finicity.client.api.AuthenticationApi;
import com.mastercard.finicity.client.auth.ApiKeyAuth;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {

    protected final static String PARTNER_ID = System.getProperty("partnerId");
    protected final static String PARTNER_SECRET = System.getProperty("partnerSecret");
    protected final static String APP_KEY = System.getProperty("appKey");

    protected final static ApiClient apiClient = Configuration.getDefaultApiClient();
    protected final static AuthenticationApi authenticationApi = new AuthenticationApi(apiClient);

    @BeforeEach
    void setUp() throws ApiException {
        // Prism
        // apiClient.setBasePath("http://localhost:4010");

        // Client logging
        apiClient.setDebugging(true);

        // Add 'Finicity-App-Key'
        ApiKeyAuth appKey = (ApiKeyAuth) apiClient.getAuthentication("FinicityAppKey");
        appKey.setApiKey(APP_KEY);
    }
}
