package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.*;
import com.mastercard.finicity.client.test.BaseAppKeyAppTokenTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerApiTest extends BaseAppKeyAppTokenTest {

    private final CustomerApi api = new CustomerApi();

    @Test
    @Disabled
    void addTestingCustomerTest() throws ApiException {
        var username = "customer_" + RandomStringUtils.randomAlphabetic(10);
        var request = new AddCustomerRequest()
                .username(username);
        var response = api.addTestingCustomer(request);
        assertNotNull(response.getId());
        assertNotNull(response.getCreatedDate());
        assertEquals(username, response.getUsername());
    }

    @Test
    @Disabled
    void addCustomerTest() throws ApiException {
        AddCustomerRequest addCustomerRequest = null;
        AddCustomerResponse response = api.addCustomer(addCustomerRequest);
        // TODO: test validations
    }

    @Test
    @Disabled
    void deleteCustomerTest() throws ApiException {
        String customerId = null;
                api.deleteCustomer(customerId);
        // TODO: test validations
    }

    @Test
    @Disabled
    void getCustomerTest() throws ApiException {
        String contentLength = null;
        String customerId = null;
                Customer response = api.getCustomer(contentLength, customerId);
        // TODO: test validations
    }

    @Test
    @Disabled
    void getCustomerWithApplicationDataTest() throws ApiException {
        String customerId = null;
                CustomerWithApplicationData response = api.getCustomerWithApplicationData(customerId);
        // TODO: test validations
    }

    @Test
    @Disabled
    void getCustomersTest() throws ApiException {
        String search = null;
        String username = null;
        Long start = null;
        Long limit = null;
        String type = null;
                GetCustomersResponse response = api.getCustomers(search, username, start, limit, type);
        // TODO: test validations
    }

    @Test
    @Disabled
    void modifyCustomerTest() throws ApiException {
        String customerId = null;
        ModifyCustomerRequest modifyCustomerRequest = null;
                api.modifyCustomer(customerId, modifyCustomerRequest);
        // TODO: test validations
    }
    
}
