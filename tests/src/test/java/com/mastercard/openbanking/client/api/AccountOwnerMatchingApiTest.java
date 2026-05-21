package com.mastercard.openbanking.client.api;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.*;
import com.mastercard.openbanking.client.test.utils.AccountUtils;
import com.mastercard.openbanking.client.test.BaseTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountOwnerMatchingApiTest extends BaseTest {

    private final IdentityApi IdentityApi = new IdentityApi();
    private final static AccountsApi api = new AccountsApi(apiClient);
    private static String existingInstitutionLoginId;
    private static Long existingInstitutionId;
    private static String existingAccountId;

    /**
     * Verify and provide additional details about the accounts for which customer has given the consent.
     * Match and provide scoring details for the claimed account associated with a specific customer in comparision with the user input with the consent given for a account matching information. _Supported regions_: ![🇺🇸](https://flagcdn.com/20x15/us.png)
     * @throws ApiException if the Api call fails
     */

    @BeforeAll
    protected static void beforeAll() {
        try {
            // Load existing IDs to be used in the subsequent tests
            var existingAccount = AccountUtils.getCustomerAccounts(api, CUSTOMER_ID).get(0);
            existingInstitutionLoginId = existingAccount.getInstitutionLoginId().toString();
            existingAccountId = existingAccount.getId();
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    public void accountMatchingScoreDetailsTest() {
        try {
            AccountOwnerVerificationMatchingRequestName name = new AccountOwnerVerificationMatchingRequestName();
            name.setFirstName("John");
            name.setLastName("Doe");

            AccountOwnerVerificationMatchingRequestOneOf oneOf = new AccountOwnerVerificationMatchingRequestOneOf();
            oneOf.setName(name);

            AccountOwnerVerificationMatchingRequest request = new AccountOwnerVerificationMatchingRequest();
            request.setActualInstance(oneOf);

            Boolean withInsights = false;
            var response = IdentityApi.accountMatchingScoreDetails(
                    CUSTOMER_ID,
                    existingAccountId,
                    request,
                    withInsights
            );
            assertNotNull(response);
        }
        catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    public void accountMatchingScoreFirstNameFieldValidations() {
        try {
            AccountOwnerVerificationMatchingRequestName name = new AccountOwnerVerificationMatchingRequestName()
                    .middleName("MiddleName")
                    .lastName("LastName")
                    .suffix("Suffix");

            AccountOwnerVerificationMatchingRequestOneOf oneOf = new AccountOwnerVerificationMatchingRequestOneOf();
            oneOf.setName(name);

            AccountOwnerVerificationMatchingRequest request = new AccountOwnerVerificationMatchingRequest();
            request.setActualInstance(oneOf);

            Boolean withInsights = false;
            AccountOwnerVerificationMatchResults response = IdentityApi.accountMatchingScoreDetails(
                    CUSTOMER_ID,
                    existingAccountId,
                    request,
                    withInsights
            );
            Assertions.fail();
        }
        catch (ApiException e) {
            logApiException(e);
            assertErrorCodeEquals(1002, e);
            assertErrorMessageEquals("Improperly Formatted Request Body", e);
        }
    }

    @Test
    public void accountMatchingScoreBusinessNameFieldValidation() {
        try {
            AccountOwnerVerificationMatchingRequestOneOf1 oneOf1 = new AccountOwnerVerificationMatchingRequestOneOf1();
            oneOf1.setBusinessName("ABC Tires Inc");

            AccountOwnerVerificationMatchingRequest request = new AccountOwnerVerificationMatchingRequest();
            request.setActualInstance(oneOf1);

            Boolean withInsights = false;
            var response = IdentityApi.accountMatchingScoreDetails(
                    CUSTOMER_ID,
                    existingAccountId,
                    request,
                    withInsights
            );
            assertNotNull(response);
        } catch (ApiException e) {
            logApiException(e);
        }
    }

    @Test
    public void invalid_whenNeitherNameNorBusinessNameProvided() {
        try {
            AccountOwnerVerificationMatchingRequest request = new AccountOwnerVerificationMatchingRequest();

            Boolean withInsights = false;

            IdentityApi.accountMatchingScoreDetails(
                    CUSTOMER_ID,
                    existingAccountId,
                    request,
                    withInsights
            );

            Assertions.fail("Expected validation error when neither name nor businessName are provided");
        }
        catch (ApiException e) {
            logApiException(e);
            assertErrorCodeEquals(1002, e);
            assertErrorMessageEquals("Improperly Formatted Request Body", e);
        }
    }

}