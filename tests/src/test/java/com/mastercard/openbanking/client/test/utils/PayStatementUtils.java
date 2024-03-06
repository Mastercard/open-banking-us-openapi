package com.mastercard.openbanking.client.test.utils;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.api.PayStatementsApi;
import com.mastercard.openbanking.client.test.ModelFactory;

public class PayStatementUtils {

    public static String storeAsset(PayStatementsApi api, String customerId) throws ApiException {
        var asset = api.storeCustomerPayStatement(customerId, ModelFactory.newPayStatementPdf());
        return asset.getAssetId();
    }
}
