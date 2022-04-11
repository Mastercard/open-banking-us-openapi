package com.mastercard.finicity.client.api;


import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.PayStatement;
import com.mastercard.finicity.client.test.BaseAppKeyAppTokenTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PayStatementsApiTest extends BaseAppKeyAppTokenTest {

    private final PayStatementsApi api = new PayStatementsApi(apiClient);

    @Test
    public void storeCustomerPayStatementTest() {
        try {
            var payStatement = new PayStatement()
                    .label("lastPayPeriod")
                    .statement("VGhpcyBtdXN0IGJlIGFuIGltYWdl");
            var asset = api.storeCustomerPayStatement(CUSTOMER_ID, payStatement);
            assertNotNull(asset.getAssetId());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }
    
}
