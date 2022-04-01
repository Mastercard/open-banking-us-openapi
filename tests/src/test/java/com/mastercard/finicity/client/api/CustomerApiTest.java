package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class CustomerApiTest {

    private final CustomerApi api = new CustomerApi();

    @Test
    public void addCustomerTest() throws ApiException {
        AddCustomerRequest addCustomerRequest = null;
                AddCustomerResponse response = api.addCustomer(addCustomerRequest);
        // TODO: test validations
    }

    @Test
    public void addTestingCustomerTest() throws ApiException {
        AddCustomerRequest addCustomerRequest = null;
                AddCustomerResponse response = api.addTestingCustomer(addCustomerRequest);
        // TODO: test validations
    }

    @Test
    public void deleteCustomerTest() throws ApiException {
        String customerId = null;
                api.deleteCustomer(customerId);
        // TODO: test validations
    }

    @Test
    public void getCustomerTest() throws ApiException {
        String contentLength = null;
        String customerId = null;
                Customer response = api.getCustomer(contentLength, customerId);
        // TODO: test validations
    }

    @Test
    public void getCustomerWithApplicationDataTest() throws ApiException {
        String customerId = null;
                CustomerWithApplicationData response = api.getCustomerWithApplicationData(customerId);
        // TODO: test validations
    }

    @Test
    public void getCustomersTest() throws ApiException {
        String search = null;
        String username = null;
        Long start = null;
        Long limit = null;
        String type = null;
                GetCustomersResponse response = api.getCustomers(search, username, start, limit, type);
        // TODO: test validations
    }

    @Test
    public void modifyCustomerTest() throws ApiException {
        String customerId = null;
        ModifyCustomerRequest modifyCustomerRequest = null;
                api.modifyCustomer(customerId, modifyCustomerRequest);
        // TODO: test validations
    }
    
}
