package com.mastercard.finicity.client.test;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.auth.ApiKeyAuth;
import com.mastercard.finicity.client.model.PartnerCredentials;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public abstract class BaseAppKeyAppTokenTest extends BaseAppKeyTest {

    @BeforeEach
    void setUp() throws ApiException {
        // Add 'Finicity-App-Key'
        super.setUp();

        // Add 'Finicity-App-Token'
        var response = BaseTest.authenticationApi.createToken(new PartnerCredentials()
                .partnerId(BaseTest.PARTNER_ID)
                .partnerSecret(BaseTest.PARTNER_SECRET));
        var token = response.getToken();
        assertNotNull(token);
        var appToken = (ApiKeyAuth) BaseTest.apiClient.getAuthentication("FinicityAppToken");
        appToken.setApiKey(token);
    }
}
