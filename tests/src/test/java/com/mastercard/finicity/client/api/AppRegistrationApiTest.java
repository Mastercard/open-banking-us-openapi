package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.test.AccountUtils;
import com.mastercard.finicity.client.test.BaseAppKeyTest;
import com.mastercard.finicity.client.test.ModelFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppRegistrationApiTest extends BaseAppKeyTest {

    private final AppRegistrationApi api = new AppRegistrationApi();
    private final static AccountsApi accountApi = new AccountsApi(apiClient);

    @Test
    void registerAppTest() {
        try {
            var application = ModelFactory.newApplication();
            var registeredApplication = api.registerApp(application);
            assertNotNull(registeredApplication.getPreAppId());
            assertEquals("P", registeredApplication.getStatus());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void getAppRegistrationStatusV2Test() {
        try {
            var statuses = api.getAppRegistrationStatusV2(null, null, null, null, null, null, null, null);
            assertNotNull(statuses.getNumberOfRecordsPerPage());
            assertNotNull(statuses.getPageNumber());
            assertNotNull(statuses.getTotalPages());
            assertNotNull(statuses.getTotalRecords());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void modifyAppRegistrationTest() {
        try {
            var application = ModelFactory.newApplication();
            var preAppId = api.registerApp(application).getPreAppId();
            application.setAppDescription("Updated description");
            var updatedApplication = api.modifyAppRegistration(preAppId, application);
            assertEquals(preAppId, updatedApplication.getPreAppId());
            assertEquals("P", updatedApplication.getStatus());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void migrateInstitutionLoginAccountsV2Test() {
        try {
            var existingAccount = AccountUtils.getCustomerAccounts(accountApi, CUSTOMER_ID).get(0);
            var existingInstitutionLoginId = existingAccount.getInstitutionLoginId();
            var accounts = api.migrateInstitutionLoginAccountsV2(CUSTOMER_ID, existingInstitutionLoginId, new Object());
            assertTrue(accounts.getAccounts().size() > 0);
            fail();
        } catch (ApiException e) {
            // {"code":20007,"message":"Accounts on institution XXXXXX cannot be migrated "}
            logApiException(e);
        }
    }

    @Test
    void setCustomerAppIDTest() {
        try {
            api.setCustomerAppID(CUSTOMER_ID, "1234", new Object());
        } catch (ApiException e) {
            // {"code":10007,"message":"invalid application id"}
            logApiException(e);
            assertErrorCodeEquals(10007, e);
            assertErrorMessageEquals("invalid application id", e);
        }
    }
}
