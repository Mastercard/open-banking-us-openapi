package com.mastercard.openbanking.client.api;


import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.*;
import com.mastercard.openbanking.client.test.BaseTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.mastercard.openbanking.client.test.ModelFactory;

import static org.junit.jupiter.api.Assertions.*;

class BusinessesApiApiTest extends BaseTest {

    private static final BusinessesApiApi business_api = new BusinessesApiApi(apiClient);
    private static final CustomersApi api = new CustomersApi(apiClient);
    private static String customerId;


    @BeforeAll
    protected static void beforeAll() {
        try {
            var newCustomer = ModelFactory.newCustomer();
            var customer = api.addTestingCustomer(newCustomer);
            customerId = customer.getId();
            assertNotNull(customer.getId());
            assertNotNull(customer.getCreatedDate());
            assertEquals(newCustomer.getUsername(), customer.getUsername());
            var businessDetail = ModelFactory.newBusiness();
            var response = business_api.addBusinessDetails(customerId, businessDetail);
            assertNotNull(response.getBusinessId());
        } catch (ApiException e) {
            fail(e);
        }
    }


    @Test
    void getBusinessByCustomer() {
        try  {

            var businessDetails = business_api.getBusinessByCustomer(customerId);
            assertNotNull(businessDetails.get(0).getName());
            assertNotNull(businessDetails.get(0).getAddress());
            assertNotNull(businessDetails.get(0).getPersonallyLiable());
            assertNotNull(businessDetails.get(0).getPhoneNumber());

        } catch (ApiException e) {
            fail(e);
        }
    }


    @Test
    void updateBusiness() {
        try  {
            var businessDetail = ModelFactory.newBusiness();
            businessDetail.email("update@finicity.com");
            businessDetail.name("Updated business");
            businessDetail.address(new NewAddress()
                    .addressLine1("3# park street")
                    .addressLine2("Goergr valley").city("Murray")
                    .country("USA")
                    .state("California")
                    .postalCode("8412557"));
            businessDetail.phoneNumber(new PhoneNumberFormat()
                    .countryCode("372")
                    .phoneNo("7992222")
            );
            var businessDetails = business_api.getBusinessByCustomer(customerId);
            var response = business_api.updateBusiness(businessDetails.get(0).getBusinessId(), businessDetail);

            assertNotNull(response.getName());
            assertEquals(businessDetail.getEmail(),"update@finicity.com");
            assertEquals(businessDetail.getName(),"Updated business");
        } catch (ApiException e) {
            fail(e);
        }
    }


    @Test
    void getBusinessById() {
        try  {
            var businessDetails = business_api.getBusinessByCustomer(customerId);
            var response = business_api.getBusinessById(businessDetails.get(0).getBusinessId());

            assertNotNull(response.get(0).getBusinessId());
            assertNotNull(response.get(0).getPhoneNumber());
            assertNotNull(response.get(0).getPersonallyLiable());
        } catch (ApiException e) {
            fail(e);
        }
    }



}
