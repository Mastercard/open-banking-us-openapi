package com.mastercard.finicity.client.api;


import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.test.BaseTest;
import com.mastercard.finicity.client.test.ModelFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PayStatementsApiTest extends BaseTest {

    private final PayStatementsApi api = new PayStatementsApi(apiClient);

    @Test
    void storeCustomerPayStatementTest() {
        try {
            var payStatement = ModelFactory.newPayStatement();
            var asset = api.storeCustomerPayStatement(CUSTOMER_ID, payStatement);
            assertNotNull(asset.getAssetId());
        } catch (ApiException e) {
            fail(e);
        }
    }

}
