package com.mastercard.openbanking.client.api;

import com.google.gson.Gson;
import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.AnalyticsReportConstraints;
import com.mastercard.openbanking.client.model.AnalyticsReportData;
import com.mastercard.openbanking.client.model.ErrorMessage;
import com.mastercard.openbanking.client.model.NewAddress;
import com.mastercard.openbanking.client.model.NewBusiness;
import com.mastercard.openbanking.client.model.PhoneNumberFormat;
import com.mastercard.openbanking.client.test.BaseTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentHistoryAnalyticsApiTest extends BaseTest {

    private static final String PHRBNONCRA = "phrbnoncra";
    private static final String PHRBCRA = "phrbcra";
    private static final String PHRBFTC = "phrbftc";
    private static final String INPROGRESS = "inProgress";
    private final PaymentHistoryReportApi api = new PaymentHistoryReportApi(apiClient);

    @BeforeAll
    static void beforeAll() {
        try {
            new BusinessesApiApi(apiClient).addBusinessDetails(CUSTOMER_ID, getNewBusiness());
        } catch (ApiException e) {
            ErrorMessage errorMessage = new Gson().fromJson(e.getResponseBody(), ErrorMessage.class);
            if ((Double) errorMessage.getCode() != 10004) {
                fail(e);
            }
        }
    }

    @Test
    void generateForesightAnalyticsPHRBCRATest() {
        try {

            // Generate a report
            var reportConstraints = new AnalyticsReportConstraints();
            AnalyticsReportData analyticsReportData = new AnalyticsReportData();
            analyticsReportData.setForCraPurpose(true);
            analyticsReportData.setApplicantIsPersonalGuarantor(true);
            reportConstraints.setAnalyticsReportData(analyticsReportData);
            var reportAck = api.generatePaymentHistoryReport(CUSTOMER_ID, reportConstraints, null);
            assertNotNull(reportAck);
            assertEquals(INPROGRESS, reportAck.getStatus());
            assertEquals(PHRBCRA, reportAck.getType());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void generateForesightAnalyticsPHRBNONCRATypeTest() {
        try {
            var reportConstraints = new AnalyticsReportConstraints();
            AnalyticsReportData analyticsReportData = new AnalyticsReportData();
            analyticsReportData.setForCraPurpose(false);
            analyticsReportData.setApplicantIsPersonalGuarantor(true);
            reportConstraints.setAnalyticsReportData(analyticsReportData);
            var reportAck = api.generatePaymentHistoryReport(CUSTOMER_ID, reportConstraints, null);
            assertNotNull(reportAck);
            assertEquals(INPROGRESS, reportAck.getStatus());
            assertEquals(PHRBNONCRA, reportAck.getType());
        } catch (ApiException e) {
            fail(e);
        }
    }


    @Test
    void generateForesightAnalyticsPHRBFTCTypeTest() {
        try {
            var reportConstraints = new AnalyticsReportConstraints();
            AnalyticsReportData analyticsReportData = new AnalyticsReportData();
            analyticsReportData.setForCraPurpose(true);
            analyticsReportData.setApplicantIsPersonalGuarantor(false);
            reportConstraints.setAnalyticsReportData(analyticsReportData);
            var reportAck = api.generatePaymentHistoryReport(CUSTOMER_ID, reportConstraints, null);
            assertNotNull(reportAck);
            assertEquals(INPROGRESS, reportAck.getStatus());
            assertEquals(PHRBFTC, reportAck.getType());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @NotNull
    private static NewBusiness getNewBusiness() {
        NewBusiness newBusiness = new NewBusiness();
        newBusiness.setName("ABC Tires Inc - 1");
        newBusiness.setPersonallyLiable(true);
        NewAddress newAddress = new NewAddress();
        newAddress.setAddressLine1("2020 State Street");
        newAddress.setAddressLine2("Suite 301");
        newAddress.setCity("Salt Lake City");
        newAddress.setState("Utah");
        newAddress.setCountry("USA");
        newAddress.setPostalCode("84116");
        newBusiness.setAddress(newAddress);
        PhoneNumberFormat phoneNumberFormat = new PhoneNumberFormat();
        phoneNumberFormat.setPhoneNo("9012345678");
        phoneNumberFormat.setCountryCode("1");
        newBusiness.setPhoneNumber(phoneNumberFormat);
        return newBusiness;
    }

}
