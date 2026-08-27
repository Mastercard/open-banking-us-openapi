package com.mastercard.openbanking.client.api;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.ApiResponse;
import com.mastercard.openbanking.client.model.*;
import com.mastercard.openbanking.client.test.BaseTest;
import com.mastercard.openbanking.client.test.ModelFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * API tests for SmallBusinessCreditAnalyticsApi
 */
@Disabled("These tests require valid Small Business Credit Analytics credentials, which are not available for TESTING partners." +
        "We don't have any static profile from SBCA Mastercard team to get data from their production environment. " +
        "Once available, enable and configure accordingly." +
        "For more details, refer to US Open Banking Small Business Credit Analytics API documentation." +
        "NOTE: You can run these in Staging environment, as profiles used in this test belongs to SBCA Sandbox Environment.")
public class SmallBusinessCreditAnalyticsApiTest extends BaseTest {

    private static final Logger log = Logger.getLogger(SmallBusinessCreditAnalyticsApiTest.class.getName());

    private static final String SMALL_BUSINESS_ANALYTICS_REPORT = "sbcaanalyticsnoncra";
    private static final String SMALL_BUSINESS_BENCHMARK_REPORT = "sbcabenchmarksnoncra";
    private static final String BUSINESS = "Business";

    private final SmallBusinessCreditAnalyticsApi smallBusinessApi = new SmallBusinessCreditAnalyticsApi();

    @Test
    public void generateSmallBusinessAnalyticsReportTest() throws ApiException {
        try {
            SBCAMetricsReportConstraints sbcaReportConstraints = buildConstraints("retail_sales_analytics");
            log.log(Level.FINER, sbcaReportConstraints.toString());
            GenerateSBCAMetricsReport200Response response = smallBusinessApi.generateSBCAMetricsReport(CUSTOMER_ID, sbcaReportConstraints);

            Object reportInstance = response.getActualInstance();
            System.out.println("Report type while getReportStatus: " + reportInstance.getClass() + " END");
            if (reportInstance instanceof SBCAAnalyticsReport annalyticsReport) {
                assertNotNull(annalyticsReport);
                assertEquals("success", annalyticsReport.getStatus());
                assertEquals(SMALL_BUSINESS_ANALYTICS_REPORT, annalyticsReport.getType());
                assertNotNull(annalyticsReport.getRetailSalesAnalyticsMetrics());
            }
        } catch (ApiException e) {
            fail(e);
        }
    }


    @Test
    public void generateSmallBusinessBemchmarkReportTest() throws ApiException {
        try {
            SBCAMetricsReportConstraints sbcaReportConstraints = buildConstraints("retail_sales_benchmarks");
            log.log(Level.FINER, sbcaReportConstraints.toString());
            GenerateSBCAMetricsReport200Response response = smallBusinessApi.generateSBCAMetricsReport(CUSTOMER_ID, sbcaReportConstraints);

            Object reportInstance = response.getActualInstance();
            System.out.println("Report type while getReportStatus: " + reportInstance.getClass() + " END");
            if (reportInstance instanceof SBCABenchmarkReport benchmarkReport) {
                assertNotNull(benchmarkReport);
                assertEquals("success", benchmarkReport.getStatus());
                assertEquals(SMALL_BUSINESS_BENCHMARK_REPORT, benchmarkReport.getType());
                assertNotNull(benchmarkReport.getRetailSalesBenchmarksMetrics());
            }
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    public void getLocationByIdTest() throws ApiException {
        try {
            String idType = "MERCHANT_ID";
            String idValue = "106241230D01";
            LocationMatches locationMatchResponse = smallBusinessApi.getLocationMatches(idType, idValue, null, null, null, null, null);

            assertNotNull(locationMatchResponse);
            assertNotNull(locationMatchResponse.getLocationMatches());
            assertNotNull(locationMatchResponse.getLocationMatches().get(0).getLocationId());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    public void getLocationByInfoTest() throws ApiException {
        try {
            String merchantName = "Artisan Emporium";
            String streetAddress = "2000 Purchase St";
            String city = "Purchase";
            String stateProvinceRegion = "NY";
            String postalCode = "10577";

            LocationMatches locationMatchResponse = smallBusinessApi.getLocationMatches("", "", merchantName, streetAddress, city, stateProvinceRegion, postalCode);

            assertNotNull(locationMatchResponse);
            assertNotNull(locationMatchResponse.getLocationMatches());
            assertNotNull(locationMatchResponse.getLocationMatches().get(0).getLocationId());
        } catch (ApiException e) {
            fail(e);
        }
    }

    private static SBCAMetricsReportConstraints buildConstraints(String metricType) {
        BusinessDetailsRequest business = buildBusiness();

        SBCAParams sbcaParams = new SBCAParams();
        sbcaParams.setHasConsent(true);
        sbcaParams.setMetricFrequency("Monthly");
        sbcaParams.setMetricType(metricType);
        sbcaParams.setLocationId(UUID.fromString("a1b2c3d4-0000-1234-abcd-000000000001"));


        SBCAanalyticsReportData analyticsData = new SBCAanalyticsReportData();
        analyticsData.setBusinessDetails(business);
        analyticsData.setSbcaParams(sbcaParams);

        SBCAMetricsReportConstraints sbcaReportConstrains = new SBCAMetricsReportConstraints();
        sbcaReportConstrains.setAnalyticsReportData(analyticsData);

        return sbcaReportConstrains;
    }

    private static BusinessDetailsRequest buildBusiness() {
        BusinessDetailsRequestAddress businessAddress = new BusinessDetailsRequestAddress();
        businessAddress.setAddressLine1("2025 State Street");
        businessAddress.setAddressLine2("Suite 65");
        businessAddress.setCity("Salt Lake City");
        businessAddress.setState("Utah");
        businessAddress.setCountry("US");
        businessAddress.setPostalCode("84116");

        PhoneNumberWithCountryCode businessPhoneNumber = new PhoneNumberWithCountryCode();
        businessPhoneNumber.setPhoneNo("8042221111");
        businessPhoneNumber.setCountryCode("1");

        BusinessDetailsRequest business = new BusinessDetailsRequest();
        business.setName("ADil Business Name");
        business.setPersonallyLiable(true);
        business.setAddress(businessAddress);
        business.setPhoneNumber(businessPhoneNumber);

        return business;
    }
}
