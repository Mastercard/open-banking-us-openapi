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

import static com.mastercard.finicity.client.test.BaseTest.logApiException;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class AuthInterceptor implements Interceptor {

    String token;
    LocalDateTime tokenExpiryTime;
    protected final static AuthenticationApi authenticationApi = new AuthenticationApi(BaseTest.apiClient);

    String PARTNER_ID;
    String PARTNER_SECRET;
    String APP_KEY;

    public AuthInterceptor(String partnerId, String partnerSecret, String appKey) {
        PARTNER_ID = partnerId;
        PARTNER_SECRET = partnerSecret;
        APP_KEY = appKey;
    }

    @NotNull
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request()
                .newBuilder()
                .addHeader("Finicity-App-Key", this.APP_KEY);

        if (chain.request().url().toString().contains("authentication")) {
            return chain.proceed(requestBuilder.build());
        }

        if (this.tokenExpiryTime == null || this.tokenExpiryTime.isBefore(LocalDateTime.now())) {
            this.refreshToken();
        }

        requestBuilder = requestBuilder
                .addHeader("Finicity-App-Token", this.token);
        return chain.proceed(requestBuilder.build());
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
