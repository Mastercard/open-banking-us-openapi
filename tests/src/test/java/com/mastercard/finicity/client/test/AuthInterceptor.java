package com.mastercard.finicity.client.test;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.api.AuthenticationApi;
import com.mastercard.finicity.client.model.AccessToken;
import com.mastercard.finicity.client.model.PartnerCredentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.mastercard.finicity.client.test.BaseTest.PARTNER_ID;
import static com.mastercard.finicity.client.test.BaseTest.PARTNER_SECRET;
import static com.mastercard.finicity.client.test.BaseTest.logApiException;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class AuthInterceptor implements Interceptor {

    String token;
    LocalDateTime tokenExpiryTime;
    protected final static AuthenticationApi authenticationApi = new AuthenticationApi(BaseTest.apiClient);
    protected final static String FINICITY_APP_TOKEN = "Finicity-App-Token";

    @NotNull
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        if (chain.request().url().toString().contains("authentication")) {
            return chain.proceed(request);
        }

        if (this.tokenExpiryTime == null || this.tokenExpiryTime.isBefore(LocalDateTime.now())) {
            this.refreshToken();
        }

        request = chain.request().newBuilder()
                .addHeader(FINICITY_APP_TOKEN, this.token)
                .build();
        return chain.proceed(request);
    }

    private void refreshToken() {
        AccessToken accessToken = null;
        try {
            accessToken = authenticationApi.createToken(new PartnerCredentials()
                    .partnerId(PARTNER_ID)
                    .partnerSecret(PARTNER_SECRET));
            this.tokenExpiryTime = LocalDateTime.now().plusMinutes(90);
        } catch (ApiException e) {
            logApiException(e);
            fail();
        }
        this.token = accessToken.getToken();
        assertNotNull(this.token);
    }
}
