package com.mastercard.openbanking.client.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.Experiences;
import com.mastercard.openbanking.client.test.BaseTest;

public class ExperiencesApiTest extends BaseTest {
    private final ConnectApi api = new ConnectApi(apiClient);
    private final static List<String> PRODUCT_CODE = Arrays.asList("ACH","ABC");
    private final static String APP_NAME = "TEST";
    
    @Test
    public void getAllExperienceTest() throws ApiException {
        String appName = APP_NAME;
        List<String> productCode = PRODUCT_CODE;
        try{
        List<Experiences> experiences = api.getAllExperience(appName, productCode);
        
        assertTrue(experiences.size() > 0);
        assertNotNull(experiences.get(0));
        assertNotNull(experiences.get(0).getProductCode());
        assertNotNull(experiences.get(0).getAppName());
        
        } catch (ApiException e) {
            Assertions.fail(e);
        }
    }


}
