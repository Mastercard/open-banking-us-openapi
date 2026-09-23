package com.mastercard.openbanking.client.api;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.EncryptedFeedbackRequestProperties;
import com.mastercard.openbanking.client.model.PayFeedbackItem;
import com.mastercard.openbanking.client.model.PayFeedbackRequestProperties;
import com.mastercard.openbanking.client.test.BaseTest;

class PayFeedbackApiTest extends BaseTest {

    private final PayFeedbackApiApi api = new PayFeedbackApiApi(apiClient);

    @Test
    @Disabled("Pay Feedback endpoints are not available in the CI environment yet")
    void submitPaymentFeedbackTest() {
        try {
            var item = new PayFeedbackItem()
                    .product(PayFeedbackItem.ProductEnum.PSI)
                    .partnerId(1234567890L)
                    .customerId(987654321L)
                    .paymentId("PAY123456")
                    .settled(PayFeedbackItem.SettledEnum.Y)
                    .initiationDate(LocalDate.parse("2025-09-10"))
                    .settledDate(LocalDate.parse("2025-09-11"));

            var request = new PayFeedbackRequestProperties()
                    .payFeedbacks(List.of(item));

            var response = api.submitPaymentFeedback(request);
            assertNotNull(response);
            assertNotNull(response.getTotalFeedbackCount());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    @Disabled("Pay Feedback endpoints are not available in the CI environment yet")
    void submitEncryptedPaymentFeedbackTest() {
        try {
            var request = new EncryptedFeedbackRequestProperties()
                    .encryptedFeedbackRequestProperties(
                            "eyJraWQiOiI3NjFiMDAzYzFlYWRlM(...)==.Y+oPYKZEMTKyYcSIVEgtQw==");

            var response = api.submitPaymentFeedbackEncrypted(request);
            assertNotNull(response);
            assertNotNull(response.getEncryptedFeedbackResponseProperties());
        } catch (ApiException e) {
            fail(e);
        }
    }
}
