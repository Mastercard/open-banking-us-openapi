package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.*;
import com.mastercard.finicity.client.test.BaseAppKeyAppTokenTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectApiTest extends BaseAppKeyAppTokenTest {

    private final ConnectApi api = new ConnectApi(apiClient);
    protected final static String FINBANK_A = "102105";

    @Test
    void generateConnectUrlV2Test() throws ApiException {
        var request = new ConnectUrlRequest()
                .customerId(CUSTOMER_ID)
                .partnerId(PARTNER_ID);
        var response = api.generateConnectUrlV2(request);
        assertNotNull(response);
        var link = response.getLink();
        assertNotNull(link);
        assertTrue(link.contains("customerId=5025024821"));
        assertTrue(link.contains("partnerId=2445583925753"));
    }

    @Test
    public void generateLiteConnectUrlV2Test() throws ApiException {
        var request = new LiteConnectUrlRequest()
                .institutionId(FINBANK_A)
                .customerId(CUSTOMER_ID)
                .partnerId(PARTNER_ID);
        var response = api.generateLiteConnectUrlV2(request);
        assertNotNull(response);
        var link = response.getLink();
        assertNotNull(link);
        assertTrue(link.contains("type=lite"));
        assertTrue(link.contains("institutionId=" + FINBANK_A));
        assertTrue(link.contains("customerId=5025024821"));
        assertTrue(link.contains("partnerId=2445583925753"));
    }
    
    @Test
    @Disabled
    public void generateV2ConnectURLJointBorrowerTest() throws ApiException {
        ConnectUrlRequestJointBorrower connectUrlRequestJointBorrower = null;
        ConnectUrl response = api.generateV2ConnectURLJointBorrower(connectUrlRequestJointBorrower);
        // TODO: test validations
    }

    @Test
    @Disabled
    public void generateV2FixConnectURLTest() throws ApiException {
        GenerateConnectURLRequestfixV2 generateConnectURLRequestfixV2 = null;
        ConnectUrl response = api.generateV2FixConnectURL(generateConnectURLRequestfixV2);
        // TODO: test validations
    }

    @Test
    @Disabled
    public void sendV2ConnectEmailTest() throws ApiException {
        GenerateV2ConnectEmailRequest generateV2ConnectEmailRequest = null;
        GenerateV2ConnectEmailResponse response = api.sendV2ConnectEmail(generateV2ConnectEmailRequest);
        // TODO: test validations
    }

    @Test
    @Disabled
    public void sendV2ConnectEmailJointBorrowerTest() throws ApiException {
        V2ConnectEmailRequestJointBorrower v2ConnectEmailRequestJointBorrower = null;
        GenerateV2ConnectEmailResponse response = api.sendV2ConnectEmailJointBorrower(v2ConnectEmailRequestJointBorrower);
        // TODO: test validations
    }
}
