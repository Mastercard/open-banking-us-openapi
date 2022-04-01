package com.mastercard.finicity.client.test;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.api.AuthenticationApi;
import com.mastercard.finicity.client.auth.ApiKeyAuth;
import com.mastercard.finicity.client.model.AccessToken;
import com.mastercard.finicity.client.model.PartnerCredentials;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public abstract class BaseAppKeyAppTokenTest extends BaseAppKeyTest {

    protected final static AuthenticationApi authenticationApi = new AuthenticationApi(apiClient);

    @BeforeEach
    void setUp() {
        // Add 'Finicity-App-Key'
        super.setUp();

        // Add 'Finicity-App-Token'
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