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
    void createTokenTest() {
        try {
            var response = authenticationApi.createToken(new PartnerCredentials()
                    .partnerId(PARTNER_ID)
                    .partnerSecret(PARTNER_SECRET));
            assertNotNull(response);
            assertNotNull(response.getToken());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void modifyPartnerSecretTest() {
        PartnerCredentialsWithNewSecret credentials;
        try {
            // 1. Update Partner Secret
            credentials = new PartnerCredentialsWithNewSecret()
                    .partnerId(PARTNER_ID)
                    .partnerSecret(PARTNER_SECRET)
                    .newPartnerSecret(PARTNER_SECRET + "_updated");
            authenticationApi.modifyPartnerSecret(credentials);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }

        try {
            // 2. Try to get a token using the old secret
            authenticationApi.createToken(new PartnerCredentials()
                    .partnerId(PARTNER_ID)
                    .partnerSecret(PARTNER_SECRET));
            fail();
        } catch (ApiException e) {
            // {"code":10001,"message":"Invalid credentials"}
            logApiException(e);
        }

        try {
            // 3. Rollback
            credentials = new PartnerCredentialsWithNewSecret()
                    .partnerId(PARTNER_ID)
                    .partnerSecret(PARTNER_SECRET + "_updated")
                    .newPartnerSecret(PARTNER_SECRET);
            authenticationApi.modifyPartnerSecret(credentials);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }
}
