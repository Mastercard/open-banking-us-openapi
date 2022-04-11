package com.mastercard.finicity.client.test;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.api.AuthenticationApi;
import com.mastercard.finicity.client.auth.ApiKeyAuth;
import com.mastercard.finicity.client.model.AccessToken;
import com.mastercard.finicity.client.model.PartnerCredentials;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class BaseAppKeyAppTokenTest extends BaseAppKeyTest {

    protected final static AuthenticationApi authenticationApi = new AuthenticationApi(apiClient);

    @BeforeAll
    protected static void beforeAll() {
        BaseAppKeyTest.beforeAll(); // Add 'Finicity-App-Key'
        addAppToken();
    }

    /**
     * Add 'Finicity-App-Token'
     */
    private static void addAppToken() {
        AccessToken accessToken = null;
        try {
            accessToken = authenticationApi.createToken(new PartnerCredentials()
                    .partnerId(PARTNER_ID)
                    .partnerSecret(PARTNER_SECRET));
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
        var token = accessToken.getToken();
        assertNotNull(token);
        var appToken = (ApiKeyAuth) apiClient.getAuthentication("FinicityAppToken");
        appToken.setApiKey(token);
    }
}