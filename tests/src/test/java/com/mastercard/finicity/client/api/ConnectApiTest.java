package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.*;
import com.mastercard.finicity.client.test.BaseTest;
import com.mastercard.finicity.client.test.utils.AccountUtils;
import com.mastercard.finicity.client.test.ModelFactory;
import org.junit.jupiter.api.Test;

import static com.mastercard.finicity.client.model.BorrowerType.JOINTBORROWER;
import static com.mastercard.finicity.client.model.BorrowerType.PRIMARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConnectApiTest extends BaseTest {

    private final ConnectApi api = new ConnectApi(apiClient);

    private final static int FINBANK_A = 102105;
    private final static String CONSUMER_ID = "0bf46322c167b562e6cbed9d40e19a4c";

    @Test
    void generateConnectUrlTest() {
        try {
            var request = new ConnectUrlRequest()
                    .customerId(CUSTOMER_ID)
                    .partnerId(PARTNER_ID);
            var connectUrl = api.generateConnectUrl(request);
            var link = connectUrl.getLink();
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
            api.generateLiteConnectUrl(request);
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
            api.generateLiteConnectUrl(request);
            fail();
        } catch (ApiException e) {
            // {"code":"10010","status":"400","message":"Invalid \"partnerId\" in request body","user_message":"One or more of the fields could not be validated. Please ensure you have entered the correct data.","tags":""}
            logApiException(e);
            assertErrorCodeEquals("10010", e);
            assertErrorMessageEquals("Invalid \"partnerId\" in request body", e);
        }
    }

    @Test
    void generateLiteConnectUrlTest() {
        try {
            var request = new LiteConnectUrlRequest()
                    .institutionId(FINBANK_A)
                    .customerId(CUSTOMER_ID)
                    .partnerId(PARTNER_ID);
            var connectUrl = api.generateLiteConnectUrl(request);
            var link = connectUrl.getLink();
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
            api.generateFixConnectUrl(request);
            fail();
        } catch (ApiException e) {
            // {"code":"38007","status":"404","title":"Connecting accounts error","user_message":"Customer does not have any accounts associated with institutionLoginId.","tags":"","level":"error","message":"Customer does not have any accounts associated with institutionLoginId."}
            logApiException(e);
            assertErrorCodeEquals("38007", e);
            assertErrorMessageEquals("Customer does not have any accounts associated with institutionLoginId.", e);
        }
    }

    @Test
    void generateFixConnectUrlTest() {
        try {
            var accountApi = new AccountsApi(apiClient);
            var institutionLoginId = AccountUtils.getCustomerAccounts(accountApi, CUSTOMER_ID).get(0).getInstitutionLoginId();
            var request = new FixConnectUrlRequest()
                    .institutionLoginId(institutionLoginId)
                    .customerId(CUSTOMER_ID)
                    .partnerId(PARTNER_ID);
            var connectUrl = api.generateFixConnectUrl(request);
            var link = connectUrl.getLink();
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
    void sendConnectEmailTest() {
        try {
            var request = new ConnectEmailRequest()
                    .customerId(CUSTOMER_ID)
                    .partnerId(PARTNER_ID)
                    .consumerId(CONSUMER_ID)
                    .email(new EmailOptions().to("someone@company.com"));
            var connectEmailUrl = api.sendConnectEmail(request);
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
    void generateJointBorrowerConnectUrlTest() {
        try {
            var request = new ConnectJointBorrowerUrlRequest()
                    .partnerId(PARTNER_ID)
                    .addBorrowersItem(ModelFactory.newBorrower(PRIMARY, CONSUMER_ID, CUSTOMER_ID))
                    .addBorrowersItem(ModelFactory.newBorrower(JOINTBORROWER, CONSUMER_ID, CUSTOMER_ID));
            var connectUrl = api.generateJointBorrowerConnectUrl(request);
            var link = connectUrl.getLink();
            assertTrue(link.contains("partnerId=" + PARTNER_ID));
            assertTrue(link.contains("consumerId=" + CONSUMER_ID));
            assertTrue(link.contains("customerId=" + CUSTOMER_ID));
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void sendJointBorrowerConnectEmailTest() {
        try {
            var request = new ConnectJointBorrowerEmailRequest()
                    .partnerId(PARTNER_ID)
                    .addBorrowersItem(ModelFactory.newBorrower(PRIMARY, CONSUMER_ID, CUSTOMER_ID))
                    .addBorrowersItem(ModelFactory.newBorrower(JOINTBORROWER, CONSUMER_ID, CUSTOMER_ID))
                    .email(new EmailOptions().to("someone@company.com"));
            var connectEmailUrl = api.sendJointBorrowerConnectEmail(request);
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
}
