package com.mastercard.openbanking.client.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mastercard.openbanking.client.ApiException;
import com.mastercard.openbanking.client.model.ConnectGenerateInstantOnboarding;
import com.mastercard.openbanking.client.test.BaseTest;
import com.mastercard.openbanking.client.test.ModelFactory;
import okhttp3.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class UserDetailsApiTest extends BaseTest {

    private static final String CONNECT_BASE_URL = System.getProperty("connectBaseUrl");
    private static final String JUMPSTART_API_URL = System.getProperty("jumpstartApiUrl");
    private static final MediaType JSON_TYPE = MediaType.get("application/json");

    private final OkHttpClient httpClient = new OkHttpClient();
    private final InstantOnboardingApi instantOnboardingApi = new InstantOnboardingApi(apiClient);
    private final ConsumersApi consumersApi = new ConsumersApi(apiClient);
    private final Gson gson = new Gson();

    @BeforeAll
    static void checkRequiredProperties() {
        assumeTrue(CONNECT_BASE_URL != null, "connectBaseUrl system property not set — skipping UserDetailsApiTest");
        assumeTrue(JUMPSTART_API_URL != null, "jumpstartApiUrl system property not set — skipping UserDetailsApiTest");
    }

    @Test
    void userDetailsTest() {
        try {
            // Create customer and consumer
            String customerId = createTestCustomer();
            consumersApi.createConsumer(customerId, ModelFactory.newConsumer());

            // Generate instant-onboarding link and extract flowId
            var params = new ConnectGenerateInstantOnboarding()
                    .customerId(customerId)
                    .partnerId(PARTNER_ID);
            var connectUrl = instantOnboardingApi.generateInstantOnboardingConnectUrl(params);
            String flowId = extractFlowId(connectUrl.getLink().toString());
            assertNotNull(flowId, "flowId must be present in the connect link");

            // Authenticate with Connect server using flowId
            String connectClientToken = connectAuthenticate(flowId);

            // Load user registration data into the Connect session
            tokensUpdate(connectClientToken, customerId);

            // Authenticate with Jumpstart API
            String jwt = jumpstartAuthenticate(connectClientToken, customerId);

            // Submit user profile data
            postProfiles(jwt);

            // Complete the flow
            postCompletions(jwt);

            // Get fresh Finicity app token for /users/details auth headers
            String finicityToken = getFinicityToken();

            // Call /users/details
            var response = postUserDetails(jwt, finicityToken, customerId);
            assertNotNull(response.get("firstName"));
            assertNotNull(response.get("lastName"));
            assertNotNull(response.get("phone"));
        } catch (ApiException e) {
            fail(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractFlowId(String link) {
        Matcher m = Pattern.compile("[?&]flowId=([^&]+)").matcher(link);
        return m.find() ? m.group(1) : null;
    }

    private String connectAuthenticate(String flowId) throws IOException {
        var request = new Request.Builder()
                .url(CONNECT_BASE_URL + "/server/authenticate/v2/instant-onboarding?flowId=" + flowId)
                .post(RequestBody.create("", JSON_TYPE))
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        return extractField(executeRequest(request), "token");
    }

    private void tokensUpdate(String connectClientToken, String customerId) throws IOException {
        String body = String.format(
                "{\"user\":{\"userId\":\"test-user\",\"masterCustomerId\":\"%s\",\"masterPartnerId\":\"%s\"," +
                "\"phone\":\"2009046100\",\"phoneVerificationStatus\":true,\"accounts\":[]}}",
                customerId, PARTNER_ID);
        var request = new Request.Builder()
                .url(CONNECT_BASE_URL + "/server/tokens/update")
                .post(RequestBody.create(body, JSON_TYPE))
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + connectClientToken)
                .build();
        executeRequest(request);
    }

    private String jumpstartAuthenticate(String connectClientToken, String customerId) throws IOException {
        String body = String.format("{\"customerId\":\"%s\",\"partnerId\":\"%s\"}", customerId, PARTNER_ID);
        var request = new Request.Builder()
                .url(JUMPSTART_API_URL + "/authenticates")
                .post(RequestBody.create(body, JSON_TYPE))
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + connectClientToken)
                .build();
        return extractField(executeRequest(request), "token");
    }

    private void postProfiles(String jwt) throws IOException {
        String body = "{\"phone\":\"2009046100\",\"dob\":\"1981-02-22\"}";
        var request = new Request.Builder()
                .url(JUMPSTART_API_URL + "/users/profiles")
                .post(RequestBody.create(body, JSON_TYPE))
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + jwt)
                .build();
        executeRequest(request);
    }

    private void postCompletions(String jwt) throws IOException {
        String body = "{\"user\":{\"accounts\":[{\"accountId\":\"3003506586\",\"institutionId\":\"4222\",\"institutionLoginId\":\"3003506587\"}]}}";
        var request = new Request.Builder()
                .url(JUMPSTART_API_URL + "/completions")
                .post(RequestBody.create(body, JSON_TYPE))
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + jwt)
                .build();
        executeRequest(request);
    }

    private String getFinicityToken() throws IOException {
        String body = String.format(
                "{\"partnerId\":\"%s\",\"partnerSecret\":\"%s\"}", PARTNER_ID, PARTNER_SECRET);
        var request = new Request.Builder()
                .url(apiClient.getBasePath() + "/aggregation/v2/partners/authentication")
                .post(RequestBody.create(body, JSON_TYPE))
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Finicity-App-Key", APP_KEY)
                .build();
        return extractField(executeRequest(request), "token");
    }

    private JsonObject postUserDetails(String jwt, String finicityToken, String customerId) throws IOException {
        String body = String.format("{\"customerId\":\"%s\",\"partnerId\":\"%s\"}", customerId, PARTNER_ID);
        var request = new Request.Builder()
                .url(JUMPSTART_API_URL + "/users/details")
                .post(RequestBody.create(body, JSON_TYPE))
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + jwt)
                .addHeader("Finicity-App-Key", APP_KEY)
                .addHeader("Finicity-App-Token", finicityToken)
                .build();
        return gson.fromJson(executeRequest(request), JsonObject.class);
    }

    private String executeRequest(Request request) throws IOException {
        try (var response = httpClient.newCall(request).execute()) {
            var body = response.body() != null ? response.body().string() : "";
            if (!response.isSuccessful()) {
                throw new IOException("Request to " + request.url() + " failed: " + response.code() + " " + body);
            }
            return body;
        }
    }

    private String extractField(String responseBody, String field) {
        return gson.fromJson(responseBody, JsonObject.class).get(field).getAsString();
    }

    private static String createTestCustomer() throws ApiException {
        var customer = customersApi.addTestingCustomer(ModelFactory.newCustomer());
        var customerId = customer.getId();
        createdCustomerIds.add(customerId);
        return customerId;
    }
}
