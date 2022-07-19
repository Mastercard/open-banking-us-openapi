package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.test.BaseTest;
import com.mastercard.finicity.client.test.ModelFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssetsApiTest extends BaseTest {

    private final AssetsApi api = new AssetsApi(apiClient);

    @Test
    void getAssetByCustomerIDTest_UnknownID() {
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

    @Test
    void getAssetByCustomerIDTest() throws IOException {
        try {
            var payStatementsApi = new PayStatementsApi(apiClient);
            var asset = payStatementsApi.storeCustomerPayStatement(CUSTOMER_ID, ModelFactory.newPayStatement());
            var file = api.getAssetByCustomerID(CUSTOMER_ID, asset.getAssetId());
            assertEquals("This must be an image", Files.readString(file.toPath()));
        } catch (ApiException e) {
            fail(e);
        }
    }
}
