package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.PartnerCredentials;
import com.mastercard.finicity.client.model.PartnerCredentialsWithNewSecret;
import com.mastercard.finicity.client.test.BaseAppKeyTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthenticationApiTest extends BaseAppKeyTest {

    private final AuthenticationApi authenticationApi = new AuthenticationApi(apiClient);

    @Test
    void createTokenTest() throws ApiException {
        var response = authenticationApi.createToken(new PartnerCredentials()
                .partnerId(PARTNER_ID)
                .partnerSecret(PARTNER_SECRET));
        assertNotNull(response);
        assertNotNull(response.getToken());
    }

    @Test
    void modifyPartnerSecretTest() throws ApiException {
        // 1. Update Partner Secret
        var credentials = new PartnerCredentialsWithNewSecret()
                .partnerId(PARTNER_ID)
                .partnerSecret(PARTNER_SECRET)
                .newPartnerSecret("yESweOaabuRK4Qkotest");
        authenticationApi.modifyPartnerSecret(credentials);

        try {
            // 2. Try to get a token using the old secret
            authenticationApi.createToken(new PartnerCredentials()
                    .partnerId(PARTNER_ID)
                    .partnerSecret(PARTNER_SECRET));
            fail();
        } catch (ApiException e) {
            // {"code":10001,"message":"Invalid credentials"}
            logApiException(e);
            assertErrorCodeEquals(10001, e);
            assertErrorMessageEquals("Invalid credentials", e);
        }

        // 3. Rollback
        credentials = new PartnerCredentialsWithNewSecret()
                .partnerId(PARTNER_ID)
                .partnerSecret("yESweOaabuRK4Qkotest")
                .newPartnerSecret(PARTNER_SECRET);
        authenticationApi.modifyPartnerSecret(credentials);
    }
}
