 package com.mastercard.openbanking.client.api;

 import com.mastercard.openbanking.client.ApiException;
 import com.mastercard.openbanking.client.model.*;
 import com.mastercard.openbanking.client.model.AccountOwnerVerificationMatchResults;
 import com.mastercard.openbanking.client.model.AccountOwnerVerificationMatchingRequest;
 import com.mastercard.openbanking.client.model.ErrorMessage;
 import com.mastercard.openbanking.client.test.ModelFactory;
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
      *
      * Match and provide scoring details for the claimed account associated with a specific customer in comparision with the user input with the consent given for a account matching information. _Supported regions_: ![🇺🇸](https://flagcdn.com/20x15/us.png)
      *
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
             var accountownerverificationmatchingrequest = ModelFactory.accountOwnerVerificationMatchingRequest();
             Boolean withInsights = false;

             var response = IdentityApi.accountMatchingScoreDetails(CUSTOMER_ID, existingAccountId, accountownerverificationmatchingrequest, withInsights);
             assertNotNull(response);
         }
         catch (ApiException e) {
             fail(e);
         }
     }


     @Test
     public void accountMatchingScoreFirstNameFieldValidations() {

         try {
             var accountownerverificationmatchingrequest = ModelFactory.accountOwnerVerificationMatchingRequest();
             accountownerverificationmatchingrequest.name(new AccountOwnerVerificationMatchingRequestName()
                     .middleName("MiddleName")
                     .lastName("LastName")
                     .suffix("Suffix")
             );
             Boolean withInsights = false;
             AccountOwnerVerificationMatchResults response = IdentityApi.accountMatchingScoreDetails(CUSTOMER_ID, existingAccountId, accountownerverificationmatchingrequest, withInsights);

             Assertions.fail();
         }
         catch (ApiException e) {
             logApiException(e);
             assertErrorCodeEquals(1002, e);
             assertErrorMessageEquals("Improperly Formatted Request Body", e);
         }
     }

 }
