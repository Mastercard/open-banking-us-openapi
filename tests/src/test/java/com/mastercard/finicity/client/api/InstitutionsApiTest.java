package com.mastercard.finicity.client.api;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.test.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class InstitutionsApiTest extends BaseTest {

    private final InstitutionsApi api = new InstitutionsApi(apiClient);

    private static final String existingInstitutionName = "Principal Financial - Retirement (Personal)";
    private static final Long existingInstitutionId = 4222L;

    @Test
    void getCertifiedInstitutionsTest() {
        try {
            var institutions = api.getCertifiedInstitutions(null, null, null, null, null);
            assertTrue(institutions.getFound() > 0);
            assertEquals(25, institutions.getDisplaying());
            assertEquals(25, institutions.getInstitutions().size());
            assertTrue(institutions.getMoreAvailable());
            assertNotNull(institutions.getRequestedDate());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getCertifiedInstitutionsTest_WithStartAndLimit() {
        try {
            var institutions = api.getCertifiedInstitutions(null, 2, 3, null, null);
            assertTrue(institutions.getFound() > 0);
            assertEquals(3, institutions.getDisplaying());
            assertEquals(3, institutions.getInstitutions().size());
            assertTrue(institutions.getMoreAvailable());
            assertNotNull(institutions.getRequestedDate());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getCertifiedInstitutionsTest_WithSearch() {
        try {
            var institutions = api.getCertifiedInstitutions(existingInstitutionName, null, null, null, null);
            assertEquals(1, institutions.getFound());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getCertifiedInstitutionsTest_WithCountries() {
        try {
            var institutions = api.getCertifiedInstitutions(null, null, null, null, Collections.singletonList("us"));
            assertTrue(institutions.getFound() > 0);
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getCertifiedInstitutionsWithRSSDTest() {
        try {
            var institutions = api.getCertifiedInstitutionsWithRSSD(null, null, 25, null, null);
            assertTrue(institutions.getFound() > 0);
            assertEquals(25, institutions.getDisplaying());
            assertEquals(25, institutions.getInstitutions().size());
            assertTrue(institutions.getMoreAvailable());
            assertNotNull(institutions.getRequestedDate());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getCertifiedInstitutionsWithRSSDTest_WithStartAndLimit() {
        try {
            var institutions = api.getCertifiedInstitutionsWithRSSD(null, 2, 3, null, null);
            assertTrue(institutions.getFound() > 0);
            assertEquals(3, institutions.getDisplaying());
            assertEquals(3, institutions.getInstitutions().size());
            assertTrue(institutions.getMoreAvailable());
            assertNotNull(institutions.getRequestedDate());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getCertifiedInstitutionsWithRSSDTest_WithSearch() {
        try {
            var institutions = api.getCertifiedInstitutionsWithRSSD(existingInstitutionName, null, null, null, null);
            assertEquals(1, institutions.getFound());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getInstitutionsTest() {
        try {
            var institutions = api.getInstitutions(null, null, null, null, null);
            assertTrue(institutions.getFound() > 0);
            assertEquals(25, institutions.getDisplaying());
            assertEquals(25, institutions.getInstitutions().size());
            assertTrue(institutions.getMoreAvailable());
            assertNotNull(institutions.getCreatedDate());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getInstitutionsTest_WithStartAndLimit() {
        try {
            var institutions = api.getInstitutions(null, 2, 3, null, null);
            assertTrue(institutions.getFound() > 0);
            assertEquals(3, institutions.getDisplaying());
            assertEquals(3, institutions.getInstitutions().size());
            assertTrue(institutions.getMoreAvailable());
            assertNotNull(institutions.getCreatedDate());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getInstitutionsTest_WithSearch() {
        try {
            var institutions = api.getInstitutions(existingInstitutionName, null, null, null, null);
            assertEquals(1, institutions.getFound());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getInstitutionTest() {
        try {
            var institutionWrapper = api.getInstitution(existingInstitutionId);
            assertEquals(existingInstitutionName, institutionWrapper.getInstitution().getName());
        } catch (ApiException e) {
            fail(e);
        }
    }

    @Test
    void getInstitutionBrandingTest() {
        try {
            var brandingWrapper = api.getInstitutionBranding(existingInstitutionId);
            assertNotNull(brandingWrapper);
            assertNotNull(brandingWrapper.getBranding());
        } catch (ApiException e) {
            fail(e);
        }
    }
}
