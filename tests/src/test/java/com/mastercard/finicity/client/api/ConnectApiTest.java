package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.*;
import com.mastercard.finicity.client.test.BaseTest;
import com.mastercard.finicity.client.test.ModelFactory;
import com.mastercard.finicity.client.test.utils.AccountUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConnectApiTest extends BaseTest {

    private final ConnectApi api = new ConnectApi(apiClient);
    private final static Long FINBANK_A = 102105L;
    private final static String CONSUMER_ID = "0bf46322c167b562e6cbed9d40e19a4c";
    private final static String ACCOUNT_ID = "6025959239";
    @Test
    void generateConnectUrlTest() {
        try {
            var params = new ConnectParameters()
                    .customerId(CUSTOMER_ID)
                    .language("fr-CA")
                    .partnerId(PARTNER_ID);
            var connectUrl = api.generateConnectUrl(params);
            var link = connectUrl.getLink();
            assertTrue(link.contains("customerId=" + CUSTOMER_ID));
            assertTrue(link.contains("partnerId=" + PARTNER_ID));
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void generateLiteConnectUrlTest_UnknownCustomerId() {
        try {
            var params = new LiteConnectParameters()
                    .institutionId(FINBANK_A)
                    .customerId("1234")
                    .partnerId(PARTNER_ID);
            api.generateLiteConnectUrl(params);
            fail();
        } catch (ApiException e) {
            // {"code":"10010","status":"400","message":"Customer ID does not exist or does not belong to this partner","user_message":"One or more of the fields could not be validated. Please ensure you have entered the correct data.","tags":""}
            logApiException(e);
            assertErrorCodeEquals("10010", e);
            assertErrorMessageEquals("Customer ID does not exist or does not belong to this partner", e);
        }
    }

    @Test
    void generateLiteConnectUrlTest_UnknownPartnerId() {
        try {
            var params = new LiteConnectParameters()
                    .institutionId(FINBANK_A)
                    .customerId(CUSTOMER_ID)
                    .partnerId("1234");
            api.generateLiteConnectUrl(params);
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
            var params = new LiteConnectParameters()
                    .institutionId(FINBANK_A)
                    .customerId(CUSTOMER_ID)
                    .partnerId(PARTNER_ID);
            var connectUrl = api.generateLiteConnectUrl(params);
            var link = connectUrl.getLink();
            assertTrue(link.contains("type=lite"));
            assertTrue(link.contains("institutionId=" + FINBANK_A));
            assertTrue(link.contains("customerId=" + CUSTOMER_ID));
            assertTrue(link.contains("partnerId=" + PARTNER_ID));
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void generateFixConnectUrlTest_UnknownInstitutionLoginId() {
        try {
            var params = new FixConnectParameters()
                    .institutionLoginId("1234")
                    .customerId(CUSTOMER_ID)
                    .partnerId(PARTNER_ID);
            api.generateFixConnectUrl(params);
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
            var params = new FixConnectParameters()
                    .institutionLoginId(String.valueOf(institutionLoginId))
                    .customerId(CUSTOMER_ID)
                    .partnerId(PARTNER_ID);
            var connectUrl = api.generateFixConnectUrl(params);
            var link = connectUrl.getLink();
            assertTrue(link.contains("type=fix"));
            assertTrue(link.contains("institutionLoginId=" + institutionLoginId));
            assertTrue(link.contains("customerId=" + CUSTOMER_ID));
            assertTrue(link.contains("partnerId=" + PARTNER_ID));
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void sendConnectEmailTest() {
        try {
            var params = new ConnectEmailParameters()
                    .customerId(CUSTOMER_ID)
                    .partnerId(PARTNER_ID)
                    .consumerId(CONSUMER_ID)
                    .language("fr-CA")
                    .email(new EmailOptions().to("someone@company.com"));
            var connectEmailUrl = api.sendConnectEmail(params);
            var link = connectEmailUrl.getLink();
            var emailConfig = connectEmailUrl.getEmailConfig();
            assertTrue(link.contains("origin=email"));
            assertTrue(link.contains("consumerId=" + CONSUMER_ID));
            assertTrue(link.contains("customerId=" + CUSTOMER_ID));
            assertTrue(link.contains("partnerId=" + PARTNER_ID));
            assertEquals("someone@company.com", emailConfig.getTo());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void generateJointBorrowerConnectUrlTest() {
        try {
            var params = new ConnectJointBorrowerParameters()
                    .partnerId(PARTNER_ID)
                    .addBorrowersItem(ModelFactory.newBorrower("primary", CONSUMER_ID, CUSTOMER_ID))
                    .addBorrowersItem(ModelFactory.newBorrower("jointBorrower", CONSUMER_ID, CUSTOMER_ID));
            var connectUrl = api.generateJointBorrowerConnectUrl(params);
            var link = connectUrl.getLink();
            assertTrue(link.contains("partnerId=" + PARTNER_ID));
            assertTrue(link.contains("consumerId=" + CONSUMER_ID));
            assertTrue(link.contains("customerId=" + CUSTOMER_ID));
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void sendJointBorrowerConnectEmailTest() {
        try {
            var params = new ConnectJointBorrowerEmailParameters()
                    .partnerId(PARTNER_ID)
                    .addBorrowersItem(ModelFactory.newBorrower("primary", CONSUMER_ID, CUSTOMER_ID))
                    .addBorrowersItem(ModelFactory.newBorrower("jointBorrower", CONSUMER_ID, CUSTOMER_ID))
                    .email(new EmailOptions().to("someone@company.com"));
            var connectEmailUrl = api.sendJointBorrowerConnectEmail(params);
            var link = connectEmailUrl.getLink();
            var emailConfig = connectEmailUrl.getEmailConfig();
            assertTrue(link.contains("origin=email"));
            assertTrue(link.contains("consumerId=" + CONSUMER_ID));
            assertTrue(link.contains("customerId=" + CUSTOMER_ID));
            assertTrue(link.contains("partnerId=" + PARTNER_ID));
            assertEquals("someone@company.com", emailConfig.getTo());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void verifyMicroDepositEntryTest() {
        try {
            String customerId = createTestCustomer();
            AccountValidationAssistanceApi accountApi = new AccountValidationAssistanceApi(apiClient);
            var response = accountApi.initiateMicroAmountDeposits(customerId, ModelFactory.newMicroDepositInitiation());
            var accountId = response.getAccountId();
            String status;
            do {
                var details = accountApi.getMicroDepositsDetails(customerId, accountId);
                status = details.getStatus();
            } while (!"Completed".equals(status));

            var params = new MicroEntryVerifyRequestParameter()
                    .partnerId(PARTNER_ID)
                    .customerId(customerId)
                    .accountId(accountId);
            var connectVerifyUrl = api.verifyMicroEntryMicrodeposit(params);
            var link = connectVerifyUrl.getLink();
            assertTrue(link.contains("accountId=" + accountId));
            assertTrue(link.contains("customerId=" + customerId));
            assertTrue(link.contains("partnerId=" + PARTNER_ID));
        } catch (ApiException e) {
            fail(e);
        }
    }
    private static String createTestCustomer() throws ApiException {
        var customer = customersApi.addTestingCustomer(ModelFactory.newCustomer());
        var customerId = customer.getId();
        createdCustomerIds.add(customerId);
        return customerId;
    }
}
