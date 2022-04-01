package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.*;
import com.mastercard.finicity.client.test.AccountUtils;
import com.mastercard.finicity.client.test.BaseAppKeyAppTokenTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectApiTest extends BaseAppKeyAppTokenTest {

    private final ConnectApi api = new ConnectApi(apiClient);
    private final AccountsApi accountApi = new AccountsApi();
    private final static String FINBANK_A = "102105";
    private final static String CONSUMER_ID = "0bf46322c167b562e6cbed9d40e19a4c";

    @Test
    void generateConnectUrlV2Test() {
        try {
            var request = new ConnectUrlRequest()
                    .customerId(CUSTOMER_ID)
                    .partnerId(PARTNER_ID);
            var connectUrl = api.generateConnectUrlV2(request);
            assertNotNull(connectUrl);
            var link = connectUrl.getLink();
            assertNotNull(link);
            assertTrue(link.contains("customerId=" + CUSTOMER_ID));
            assertTrue(link.contains("partnerId=" + PARTNER_ID));
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void generateLiteConnectUrlV2Test_UnknownCustomerId() {
        try {
            var request = new LiteConnectUrlRequest()
                    .institutionId(FINBANK_A)
                    .customerId("1234")
                    .partnerId(PARTNER_ID);
            api.generateLiteConnectUrlV2(request);
            fail();
        } catch (ApiException e) {
            // {"code":"10010","status":"400","message":"Customer ID does not exist or does not belong to this partner","user_message":"One or more of the fields could not be validated. Please ensure you have entered the correct data.","tags":""}
            logApiException(e);
            assertErrorCodeEquals("10010", e);
            assertErrorMessageEquals("Customer ID does not exist or does not belong to this partner", e);
        }
    }

    @Test
    void generateLiteConnectUrlV2Test_UnknownPartnerId() {
        try {
            var request = new LiteConnectUrlRequest()
                    .institutionId(FINBANK_A)
                    .customerId(CUSTOMER_ID)
                    .partnerId("1234");
            api.generateLiteConnectUrlV2(request);
            fail();
        } catch (ApiException e) {
            // {"code":"10010","status":"400","message":"Invalid \"partnerId\" in request body","user_message":"One or more of the fields could not be validated. Please ensure you have entered the correct data.","tags":""}
            logApiException(e);
            assertErrorCodeEquals("10010", e);
            assertErrorMessageEquals("Invalid \"partnerId\" in request body", e);
        }
    }

    @Test
    void generateLiteConnectUrlV2Test() {
        try {
            var request = new LiteConnectUrlRequest()
                    .institutionId(FINBANK_A)
                    .customerId(CUSTOMER_ID)
                    .partnerId(PARTNER_ID);
            var connectUrl = api.generateLiteConnectUrlV2(request);
            assertNotNull(connectUrl);
            var link = connectUrl.getLink();
            assertNotNull(link);
            assertTrue(link.contains("type=lite"));
            assertTrue(link.contains("institutionId=" + FINBANK_A));
            assertTrue(link.contains("customerId=" + CUSTOMER_ID));
            assertTrue(link.contains("partnerId=" + PARTNER_ID));
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void generateFixConnectUrlV2Test_UnknownInstitutionLoginId() {
        try {
            var request = new FixConnectUrlRequest()
                    .institutionLoginId("1234")
                    .customerId(CUSTOMER_ID)
                    .partnerId(PARTNER_ID);
            api.generateFixConnectUrlV2(request);
            fail();
        } catch (ApiException e) {
            // {"code":"38007","status":"404","title":"Connecting accounts error","user_message":"Customer does not have any accounts associated with institutionLoginId.","tags":"","level":"error","message":"Customer does not have any accounts associated with institutionLoginId."}
            logApiException(e);
            assertErrorCodeEquals("38007", e);
            assertErrorMessageEquals("Customer does not have any accounts associated with institutionLoginId.", e);
        }
    }

    @Test
    void generateFixConnectUrlV2Test() {
        try {
            var institutionLoginId = AccountUtils.getCustomerAccounts(accountApi, CUSTOMER_ID).get(0).getInstitutionLoginId();
            var request = new FixConnectUrlRequest()
                    .institutionLoginId(institutionLoginId)
                    .customerId(CUSTOMER_ID)
                    .partnerId(PARTNER_ID);
            var connectUrl = api.generateFixConnectUrlV2(request);
            var link = connectUrl.getLink();
            assertNotNull(link);
            assertTrue(link.contains("type=fix"));
            assertTrue(link.contains("institutionLoginId=" + institutionLoginId));
            assertTrue(link.contains("customerId=" + CUSTOMER_ID));
            assertTrue(link.contains("partnerId=" + PARTNER_ID));
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    @Disabled
    public void sendConnectEmailV2Test() {
        try {
            var request = new ConnectEmailRequest()
                    .customerId(CUSTOMER_ID)
                    .partnerId(PARTNER_ID)
                    .consumerId(CONSUMER_ID)
                    .email(new EmailOptions().to("someone@company.com"));
                    //.experience("default");
            var connectEmailUrl = api.sendConnectEmailV2(request);
            var link = connectEmailUrl.getLink();
            var emailConfig = connectEmailUrl.getEmailConfig();
            assertTrue(link.contains("origin=email"));
            assertTrue(link.contains("consumerId=" + CONSUMER_ID));
            assertTrue(link.contains("customerId=" + CUSTOMER_ID));
            assertTrue(link.contains("partnerId=" + PARTNER_ID));
            assertEquals("someone@company.com", emailConfig.getTo());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    @Disabled
    public void generateV2ConnectURLJointBorrowerTest() throws ApiException {
        var request = new ConnectUrlRequestJointBorrower();
        ConnectUrl response = api.generateV2ConnectURLJointBorrower(request);
        // TODO: test validations
    }

    @Test
    @Disabled
    public void sendV2ConnectEmailJointBorrowerTest() throws ApiException {
        var request = new V2ConnectEmailRequestJointBorrower();
        var connectEmailUrl = api.sendV2ConnectEmailJointBorrower(request);
        // TODO: test validations
    }
}
