package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.CustomerUpdate;
import com.mastercard.finicity.client.model.NewCustomer;
import com.mastercard.finicity.client.test.BaseAppKeyAppTokenTest;
import com.mastercard.finicity.client.test.ModelFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.mastercard.finicity.client.test.ModelFactory.randomStr;
import static org.junit.jupiter.api.Assertions.*;

class CustomersApiTest extends BaseAppKeyAppTokenTest {

    private final static CustomersApi api = new CustomersApi();
    private final static List<String> createdCustomerIds = new ArrayList<>();

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
            logApiException(e);
            fail();
        }
    }

    @Test
    void addCustomerTest() {
        try {
            var username = "customer_" + RandomStringUtils.randomAlphabetic(10);
            var newCustomer = new NewCustomer()
                    .username(username);
            api.addCustomer(newCustomer);
            fail();
        } catch (ApiException e) {
            // Not available from the Test Drive
            assertEquals(429, e.getCode());
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
            logApiException(e);
            fail();
        }
    }

    @Test
    void getCustomerTest_UnknownCustomerId() {
        try {
            api.getCustomer("1234");
            fail();
        } catch (ApiException e) {
            // {"code":14001,"message":"Customer not found."}
            logApiException(e);
            assertErrorCodeEquals("14001", e);
            assertErrorMessageEquals("Customer not found.", e);
        }
    }

    @Test
    void getCustomerWithApplicationDataTest_NoAppRegistered() {
        try {
            api.getCustomerWithAppData(CUSTOMER_ID);
            fail();
        } catch (ApiException e) {
            // {"code":50051,"message":"No registered partner applications found."}
            logApiException(e);
            assertErrorCodeEquals("50051", e);
            assertErrorMessageEquals("No registered partner applications found.", e);
        }
    }

    @Test
    void getCustomersTest() {
        try {
            var customers = api.getCustomers(null, null, null, null, null);
            assertTrue(customers.getCustomers().size() > 0);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void getCustomersTest_UnknownUsername() {
        try {
            var customers = api.getCustomers(null, "1234", null, null, null);
            assertEquals(0, customers.getCustomers().size());
            assertEquals(0, customers.getDisplaying());
            assertFalse(customers.getMoreAvailable());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void modifyCustomerTest() {
        try {
            var customerUpdate = new CustomerUpdate()
                    .firstName("John_" + randomStr())
                    .lastName("Smith_" + randomStr());
            api.modifyCustomer(CUSTOMER_ID, customerUpdate);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void deleteCustomerTest() {
        try {
            var newCustomer = ModelFactory.newCustomer();
            var customer = api.addTestingCustomer(newCustomer);
            api.deleteCustomer(customer.getId());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void deleteCustomerTest_UnknownCustomerId() {
        try {
            api.deleteCustomer("1234");
            fail();
        } catch (ApiException e) {
            // {"code":14001,"message":"Customer not found."}
            logApiException(e);
            assertErrorCodeEquals("14001", e);
            assertErrorMessageEquals("Customer not found.", e);
        }
    }

    @AfterAll
    static void afterAll() {
        // Clean-up
        createdCustomerIds.forEach(id -> {
            try {
                api.deleteCustomer(id);
            } catch (ApiException e) {
                logApiException(e);
                fail();
            }
        });
    }
}
