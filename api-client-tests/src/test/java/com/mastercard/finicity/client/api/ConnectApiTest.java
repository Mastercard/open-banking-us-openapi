package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.ConnectUrlRequest;
import com.mastercard.finicity.client.test.BaseAppKeyAppTokenTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectApiTest extends BaseAppKeyAppTokenTest {

    private final ConnectApi api = new ConnectApi(apiClient);

    @Test
    void generateConnectUrlV2Test() throws ApiException {
        var request = new ConnectUrlRequest()
                .customerId(CUSTOMER_ID)
                .partnerId(PARTNER_ID);
        var response = api.generateConnectUrlV2(request);
        assertNotNull(response);
        assertNotNull(response.getLink());
    }
}
