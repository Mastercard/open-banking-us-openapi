package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.model.VOAReportConstraints;
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
            // Get the existing consumer ID
            consumerId = ConsumerUtils.getOrCreateDefaultConsumer(new ConsumersApi(apiClient), CUSTOMER_ID);

            // Create a report and get the portfolio ID
            var verifyAssetsApi = new VerifyAssetsApi(apiClient);
            portfolioId = verifyAssetsApi.generateVOAReport(CUSTOMER_ID, new VOAReportConstraints(), null).getPortfolioId();
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getPortfolioByConsumerTest() {
        try {
            var summary = api.getPortfolioByConsumer(consumerId, portfolioId);
            assertEquals(portfolioId, summary.getPortfolioId());
            assertNotNull(summary.getConsumer());
        } catch (ApiException e) {
            // {"code":10100,"message":"No reports found for portfolioId {id}."}
            logApiException(e);
        }
    }

    @Test
    void getPortfolioByCustomerTest() {
        try {
            var summary = api.getPortfolioByCustomer(CUSTOMER_ID, portfolioId);
            assertEquals(portfolioId, summary.getPortfolioId());
        } catch (ApiException e) {
            // {"code":10100,"message":"No reports found for portfolioId {id}."}
            logApiException(e);
        }
    }
}
