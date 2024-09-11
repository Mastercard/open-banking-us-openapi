package com.mastercard.openbanking.client.api;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.CustomerUpdate;
import com.mastercard.openbanking.client.model.NewCustomer;
import com.mastercard.openbanking.client.test.BaseTest;
import com.mastercard.openbanking.client.test.ModelFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomersApiTest extends BaseTest {

    private final CustomersApi api = new CustomersApi(apiClient);

    @Test
    void addTestingCustomerTest() {
        try {
            var newCustomer = ModelFactory.newCustomer();
            var customer = api.addTestingCustomer(newCustomer);
            assertNotNull(customer.getId());
            assertNotNull(customer.getCreatedDate());
            assertEquals(newCustomer.getUsername(), customer.getUsername());
            createdCustomerIds.add(customer.getId());
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void addCustomerTest() {
        try {
            var username = "customer_" + RandomStringUtils.randomAlphabetic(10);
            var emailAddress= username+"@test.com";
            var newCustomer = new NewCustomer().username(username).email(emailAddress);
            var customer = api.addCustomer(newCustomer);
            assertNotNull(customer.getId());
            assertNotNull(customer.getCreatedDate());
            assertEquals(newCustomer.getUsername(), customer.getUsername());
            createdCustomerIds.add(customer.getId());
        } catch (ApiException e) {
            // HTTP 401: Not available from the Test Drive
            logApiException(e);
        }
    }

    @Test
    void getCustomerTest() {
        try {
            var customer = api.getCustomer(CUSTOMER_ID);
            assertEquals(CUSTOMER_ID, customer.getId());
            assertNotNull(customer.getUsername());
            assertNotNull(customer.getCreatedDate());
            assertNotNull(customer.getType());
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getCustomerTest_UnknownCustomerId() {
        try {
            api.getCustomer("1234");
            Assertions.fail();
        } catch (ApiException e) {
            // {"code":14001,"message":"Customer not found."}
            logApiException(e);
            assertErrorCodeEquals(14001, e);
            assertErrorMessageEquals("Customer not found.", e);
        }
    }

    @Test
    void getCustomerWithApplicationDataTest_NoAppRegistered() {
        try {
            var customer = api.getCustomerWithAppData(CUSTOMER_ID);
            assertEquals(CUSTOMER_ID, customer.getId());
        } catch (ApiException e) {
            // {"code":50051,"message":"No registered partner applications found."}
            logApiException(e);
            assertErrorCodeEquals(50051, e);
            assertErrorMessageEquals("No registered partner applications found.", e);
        }
    }

    @Test
    void getCustomersTest_NoFilter() {
        try {
            var customers = api.getCustomers(null, null, null, null, null);
            assertTrue(customers.getCustomers().size() > 0);
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getCustomersTest_UnknownUsername() {
        try {
            var customers = api.getCustomers("1234", null, null, null, null);
            assertEquals(0, customers.getCustomers().size());
            assertEquals(0, customers.getDisplaying());
            assertFalse(customers.getMoreAvailable());
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void getCustomersTest_UsernameFilter() {
        try {
            var customer = api.getCustomer(CUSTOMER_ID);
            var customers = api.getCustomers(customer.getUsername(), null, null, null, null);
            assertTrue(customers.getCustomers().size() > 0);
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void modifyCustomerTest() {
        try {
            var customerUpdate = new CustomerUpdate()
                    .firstName("John_" + ModelFactory.randomStr())
                    .lastName("Smith_" + ModelFactory.randomStr());
            api.modifyCustomer(CUSTOMER_ID, customerUpdate);
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void deleteCustomerTest() {
        try {
            var newCustomer = ModelFactory.newCustomer();
            var customer = api.addTestingCustomer(newCustomer);
            api.deleteCustomer(customer.getId());
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void deleteCustomerTest_UnknownCustomerId() {
        try {
            api.deleteCustomer("1234");
            Assertions.fail();
        } catch (ApiException e) {
            // {"code":14001,"message":"Customer not found."}
            logApiException(e);
            assertErrorCodeEquals(14001, e);
            assertErrorMessageEquals("Customer not found.", e);
        }
    }
}
