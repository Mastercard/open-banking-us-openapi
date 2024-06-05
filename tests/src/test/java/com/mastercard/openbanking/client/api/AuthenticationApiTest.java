package com.mastercard.openbanking.client.api;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.PartnerCredentials;
import com.mastercard.openbanking.client.model.PartnerCredentialsWithNewSecret;
import com.mastercard.openbanking.client.test.BaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthenticationApiTest extends BaseTest {

    private final AuthenticationApi api = new AuthenticationApi(apiClient);

    @Test
    void createTokenTest() {
        try {
            var response = api.createToken(new PartnerCredentials()
                    .partnerId(PARTNER_ID)
                    .partnerSecret(PARTNER_SECRET));
            assertNotNull(response);
            assertNotNull(response.getToken());
        } catch (ApiException e) {
            fail(e);
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
            api.modifyPartnerSecret(credentials);
        } catch (ApiException e) {
            fail(e);
        }

        try {
            // 2. Try to get a token using the old secret
            api.createToken(new PartnerCredentials()
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
            api.modifyPartnerSecret(credentials);
        } catch (ApiException e) {
            fail(e);
        }
    }
}
