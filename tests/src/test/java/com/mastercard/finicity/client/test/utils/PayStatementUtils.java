package com.mastercard.finicity.client.test.utils;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.api.PayStatementsApi;
import com.mastercard.finicity.client.test.ModelFactory;

public class PayStatementUtils {

    public static String storeAsset(PayStatementsApi api, String customerId) throws ApiException {
        var asset = api.storeCustomerPayStatement(customerId, ModelFactory.newPayStatement());
        return asset.getAssetId();
    }
}
