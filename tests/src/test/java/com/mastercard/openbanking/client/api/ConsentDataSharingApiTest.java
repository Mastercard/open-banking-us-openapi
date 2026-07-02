package com.mastercard.openbanking.client.api;


 import com.mastercard.openbanking.client.ApiException;
 import com.mastercard.openbanking.client.test.BaseTest;
 import org.junit.jupiter.api.Assertions;
 import org.junit.jupiter.api.Test;

 import java.util.Random;
 import java.util.UUID;

 import static org.junit.jupiter.api.Assertions.*;


 class ConsentDataSharingApiTest extends BaseTest {


     private final DataSharingConsentApi api = new DataSharingConsentApi(apiClient);
     Random random = new Random();

     private static final String PARTNER = "PARTNER";


     // ==================== Revoke by Consent ID Tests ====================


     @Test
     void revokeDataSharingConsentByConsentIdNotFoundTest() {
         try {
             // Attempt to revoke consent using a random/non-existent consent ID
             api.revokeDataSharingConsentById(UUID.randomUUID().toString(), PARTNER);
             Assertions.fail("Expected ApiException to be thrown");
         } catch (ApiException e) {
             // Partner will receive 404 NOT_FOUND as consent does not exist for the given consentId
             assertEquals(404, e.getCode());
             assertTrue(e.getMessage().contains("Consent receipt not found"));
         }
     }


     // ==================== Revoke by Institution Login ID Tests ====================


     @Test
     void revokeDataSharingConsentByInstitutionLoginIdNotFoundTest() {
         try {
             // Attempt to revoke using non-existent receipt ID and institution login ID
             String nonExistentReceiptId = UUID.randomUUID().toString();
             String nonExistentInstitutionLoginId = String.valueOf(random.nextInt(1000000));
             api.revokeDataSharingConsentByInstitutionLoginID(nonExistentReceiptId, nonExistentInstitutionLoginId, PARTNER);
             Assertions.fail("Expected ApiException to be thrown");
         } catch (ApiException e) {
             // Partner will receive 404 NOT_FOUND as consent receipt does not exist
             assertEquals(404, e.getCode());
             assertTrue(e.getMessage().contains("Consent receipt not found"));
         }
     }


     // ==================== Get Consent By CustomerId Tests ====================

     @Test
     void getDataSharingConsentsByCustomerIdNotFoundTest() {
         try {
             api.getDataSharingConsentsByCustomerId(String.valueOf(random.nextInt(10000)), "ACTIVE", null, null, 1, 10, null);
             Assertions.fail("Expected ApiException to be thrown");
         } catch (ApiException e) {
             // Partner will receive 404 NOT_FOUND  if no customer found for the given customerId.
             assertEquals(404, e.getCode());
             assertTrue(e.getMessage().contains("Customer not found"));
         }
     }

     // ==================== Get Consent By ConsentId Tests ====================

     @Test
     public void getConsentByReceiptIdNotFoundTest() {
         try {
             api.getDataSharingConsentsByID(UUID.randomUUID().toString());
             Assertions.fail("Expected ApiException to be thrown");
         } catch (ApiException e) {
             // Partner will receive 404 NOT_FOUND as consent receipt not exists for the given receiptId
             assertEquals(404, e.getCode());
             assertTrue(e.getMessage().contains("Consent receipt not found"));
         }
     }

 }
