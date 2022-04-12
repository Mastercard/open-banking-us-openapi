package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.ReportConstraints;
import com.mastercard.finicity.client.test.BaseTest;
import com.mastercard.finicity.client.test.utils.ConsumerUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PortfoliosApiTest extends BaseTest {

    private final PortfoliosApi api = new PortfoliosApi(apiClient);
    private static String consumerId;
    private static String portfolioId;

    @BeforeAll
    protected static void beforeAll() {
        try {
            // Get the consumer ID
            var consumerApi = new ConsumersApi(apiClient);
            consumerId = ConsumerUtils.getOrCreateDefaultConsumer(consumerApi, CUSTOMER_ID);

            // Create a report and get the portfolio ID
            var verifyAssetsApi = new VerifyAssetsApi(apiClient);
            portfolioId = verifyAssetsApi.generateVOAReport(CUSTOMER_ID, new ReportConstraints(), null, null).getPortfolioId();
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void getPortfolioByConsumerTest() {
        try {
            var summary = api.getPortfolioByConsumer(consumerId, portfolioId);
            assertEquals(portfolioId, summary.getPortfolioId());
            assertNotNull(summary.getConsumer());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }

    @Test
    void getPortfolioByCustomerTest() {
        try {
            var summary = api.getPortfolioByCustomer(CUSTOMER_ID, portfolioId);
            assertEquals(portfolioId, summary.getPortfolioId());
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
    }
}
