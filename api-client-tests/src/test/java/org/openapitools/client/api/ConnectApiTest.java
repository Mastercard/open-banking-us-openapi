package org.openapitools.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.api.ConnectApi;
import com.mastercard.finicity.client.auth.ApiKeyAuth;
import com.mastercard.finicity.client.model.ConnectUrlRequest;
import com.mastercard.finicity.client.model.PartnerCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConnectApiTest extends BaseTest {

    private final ConnectApi api = new ConnectApi(apiClient);

    @BeforeEach
    void setUp() throws ApiException {
        // Add 'Finicity-App-Key'
        super.setUp();

        // Add 'Finicity-App-Token'
        var response = authenticationApi.createToken(new PartnerCredentials()
                .partnerId(PARTNER_ID)
                .partnerSecret(PARTNER_SECRET));
        var token = response.getToken();
        assertNotNull(token);
        var appToken = (ApiKeyAuth) apiClient.getAuthentication("FinicityAppToken");
        appToken.setApiKey(token);
    }

    @Test
    void generateConnectUrlV2Test() throws ApiException {
        var request = new ConnectUrlRequest()
                .customerId("5021068136")
                .partnerId(PARTNER_ID);
        var response = api.generateConnectUrlV2(request);
        assertNotNull(response);
        assertNotNull(response.getLink());
    }
}
