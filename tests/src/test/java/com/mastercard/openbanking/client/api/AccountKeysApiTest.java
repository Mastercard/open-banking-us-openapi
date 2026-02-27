//package com.mastercard.openbanking.client.api;
//
//
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import com.mastercard.openbanking.client.ApiClient;
//
//public class AccountKeysApiTest {
//
//    @Test
//    public void testDefaultConstructorSetsApiClient() {
//        AccountKeyApi api = new AccountKeyApi();
//        assertNotNull(api.getApiClient());
//    }
//
//    @Test
//    public void testConstructorWithApiClient() {
//        ApiClient client = new ApiClient();
//        AccountKeyApi api = new AccountKeyApi(client);
//        assertEquals(client, api.getApiClient());
//    }
//
//    @Test
//    public void testSetAndGetApiClient() {
//        ApiClient client = new ApiClient();
//        AccountKeyApi api = new AccountKeyApi();
//        api.setApiClient(client);
//        assertEquals(client, api.getApiClient());
//    }
//
//    @Test
//    public void testSetAndGetHostIndex() {
//        AccountKeyApi api = new AccountKeyApi();
//        api.setHostIndex(1);
//        assertEquals(1, api.getHostIndex());
//    }
//
//    @Test
//    public void testSetAndGetCustomBaseUrl() {
//        AccountKeyApi api = new AccountKeyApi();
//        String url = "https://test.com";
//        api.setCustomBaseUrl(url);
//        assertEquals(url, api.getCustomBaseUrl());
//    }
//}
