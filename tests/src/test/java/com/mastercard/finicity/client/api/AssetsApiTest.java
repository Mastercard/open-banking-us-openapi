package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.test.BaseAppKeyAppTokenTest;
import org.junit.jupiter.api.Test;

public class AssetsApiTest extends BaseAppKeyAppTokenTest {

    private final AssetsApi api = new AssetsApi(apiClient);

    @Test
    public void getAssetByCustomerIDTest_UnknownID() {
        try {
            api.getAssetByCustomerID(CUSTOMER_ID, "1234");
            fail();
        } catch (ApiException e) {
            // {"code":13002,"message":"Asset not found"}
            logApiException(e);
            assertErrorCodeEquals(13002, e);
            assertErrorMessageEquals("Asset not found", e);
        }
    }
}
