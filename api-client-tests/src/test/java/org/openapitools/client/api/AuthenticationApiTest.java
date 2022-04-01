package org.openapitools.client.api;

import com.google.gson.Gson;
import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.ErrorMessage;
import com.mastercard.finicity.client.model.PartnerCredentials;
import com.mastercard.finicity.client.model.PartnerCredentialsWithNewSecret;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationApiTest extends BaseTest {

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
                .newPartnerSecret("newC4uE82VYpX1X66WGz");
        authenticationApi.modifyPartnerSecret(credentials);

        try {
            // 2. Try to get a token using the old secret
            authenticationApi.createToken(new PartnerCredentials()
                    .partnerId(PARTNER_ID)
                    .partnerSecret(PARTNER_SECRET));
            fail("Shouldn't reach this line");
        } catch (ApiException e) {
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            // {"code":10001,"message":"Invalid credentials"}
            var errorMessage = new Gson().fromJson(e.getResponseBody(), ErrorMessage.class);
            assertEquals(10001, errorMessage.getCode());
            assertEquals("Invalid credentials", errorMessage.getMessage());
            assertNull(errorMessage.getUserMessage());
            assertNull(errorMessage.getStatus());
            assertNull(errorMessage.getTags());
            assertNull(errorMessage.getAccountId());
            assertNull(errorMessage.getAssetId());
        }

        // 3. Rollback
        credentials = new PartnerCredentialsWithNewSecret()
                .partnerId(PARTNER_ID)
                .partnerSecret("newC4uE82VYpX1X66WGz")
                .newPartnerSecret(PARTNER_SECRET);
        authenticationApi.modifyPartnerSecret(credentials);
    }
}
