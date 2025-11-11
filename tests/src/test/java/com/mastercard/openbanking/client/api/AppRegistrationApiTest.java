package com.mastercard.openbanking.client.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.App;
import com.mastercard.openbanking.client.model.InstitutionResponse;
import com.mastercard.openbanking.client.test.BaseTest;
import com.mastercard.openbanking.client.test.ModelFactory;
import com.mastercard.openbanking.client.test.utils.AccountUtils;

class AppRegistrationApiTest extends BaseTest {

    private final AppRegistrationApi api = new AppRegistrationApi(apiClient);

    @Test
    void registerAppTest() {
        try {
            var application = ModelFactory.newApplication();
            var registeredApplication = api.registerApp(application);
            assertNotNull(registeredApplication.getPreAppId());
            assertEquals("P", registeredApplication.getStatus());
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getAppRegistrationStatusTest() {
        try {
            var statuses = api.getAppRegistrationStatus(null, null, null, null, null, null, null, null);
            assertNotNull(statuses.getNumberOfRecordsPerPage());
            assertNotNull(statuses.getPageNumber());
            assertNotNull(statuses.getTotalPages());
            assertNotNull(statuses.getTotalRecords());
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void modifyAppRegistrationTest() {
        try {
            var application = ModelFactory.newApplication();
            var preAppId = api.registerApp(application).getPreAppId();
            application.setAppDescription("Updated description");
            var updatedApplication = api.modifyAppRegistration(String.valueOf(preAppId), application);
            assertEquals(preAppId, updatedApplication.getPreAppId());
            assertEquals("P", updatedApplication.getStatus());
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }
    
    
//    @Test
//    void getApplicationsTest() throws ApiException {
//    	try {
//    		Integer start = 1;
//    		Integer limit = 25;
//    		String status = "A";
//    		var applicationResponse = api.getApplications(start, limit, null, null, null, status);
//    		assertTrue(applicationResponse.getFound()>0);
//    		assertTrue(applicationResponse.getDisplaying()>0);
//            assertFalse(applicationResponse.getApplications().isEmpty());
//    	}catch (Exception e) {
//    		Assertions.fail(e);
//		}
//    }

//    @Test
//    void getRegisteredInstitutionsTest() throws ApiException {
//    	try {
//    		String status = "A";
//    		var applicationResponse = api.getApplications(1, 50, null, null, null, status);
//    		Optional<String> applicationIds = applicationResponse.getApplications().stream().map(App::getApplicationId).filter(appId -> Objects.nonNull(appId)).findFirst();
//    		String applicationId = applicationIds.get();
//    		Integer start = 1;
//    		Integer limit = 25;
//    		InstitutionResponse institutionResponse = api.getRegisteredInstitutions(applicationId, start, limit, null);
//    		assertTrue(institutionResponse.getDisplaying()>0);
//    		assertTrue(institutionResponse.getFound()>0);
//    		assertTrue(institutionResponse.getMoreAvailable());
//    		assertFalse(institutionResponse.getInstitutions().isEmpty());
//    	}catch (Exception e) {
//    		Assertions.fail(e);
//    	}
//    }

    @Test
    void setCustomerAppIDTest() {
        try {
            api.setCustomerAppID(CUSTOMER_ID, "1234");
        } catch (ApiException e) {
            // {"code":10007,"message":"invalid application id"}
            logApiException(e);
            assertErrorCodeEquals(10007, e);
            assertErrorMessageEquals("invalid application id", e);
        }
    }
    
    @Test
    void migrateInstitutionLoginAccountsTest() {
        try {
            var accountApi = new AccountsApi(apiClient);
            var existingAccount = AccountUtils.getCustomerAccounts(accountApi, CUSTOMER_ID).get(0);
            var existingInstitutionLoginId = existingAccount.getInstitutionLoginId();
            // Enable this to actually migrate accounts
            // var accounts = api.migrateInstitutionLoginAccounts(CUSTOMER_ID, String.valueOf(existingInstitutionLoginId));
            // assertTrue(accounts.getAccounts().size() > 0);
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

   

}
