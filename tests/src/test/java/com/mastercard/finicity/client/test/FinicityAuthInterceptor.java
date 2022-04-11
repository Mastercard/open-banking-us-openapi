package com.mastercard.finicity.client.test;

import com.mastercard.finicity.client.ApiException;
import com.mastercard.finicity.client.api.AuthenticationApi;
import com.mastercard.finicity.client.model.PartnerCredentials;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * An okHttp3 interceptor handing Finicity authentication.
 */
public class FinicityAuthInterceptor implements Interceptor {

    private final AuthenticationApi authenticationApi;
    private final String partnerId;
    private final String partnerSecret;
    private final String appKey;

    private String token;
    private LocalDateTime tokenExpiryTime;

    public FinicityAuthInterceptor(String partnerId, String partnerSecret, String appKey) {
        this.partnerId = partnerId;
        this.partnerSecret = partnerSecret;
        this.appKey = appKey;
        this.authenticationApi = new AuthenticationApi();
    }

    @NotNull
    public Response intercept(@NotNull Chain chain) throws IOException {
        try {
            // Always add "Finicity-App-Key"
            var request = chain.request();
            var requestBuilder = request
                    .newBuilder()
                    .addHeader("Finicity-App-Key", this.appKey);

            var url = request.url().toString();
            if (url.contains("/authentication")) {
                // No "Finicity-App-Token" header needed for /authentication
                return chain.proceed(requestBuilder.build());
            }

            if (this.tokenExpiryTime == null || this.tokenExpiryTime.isBefore(LocalDateTime.now())) {
                this.refreshToken();
            }

            // Add access token to the "Finicity-App-Token" header
            requestBuilder = requestBuilder
                    .addHeader("Finicity-App-Token", this.token);
            return chain.proceed(requestBuilder.build());
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    private void refreshToken() throws ApiException {
        this.token = createToken();
        this.tokenExpiryTime = LocalDateTime.now().plusMinutes(90);
    }

    private String createToken() throws ApiException {
        return authenticationApi.createToken(new PartnerCredentials()
                .partnerId(partnerId)
                .partnerSecret(partnerSecret)).getToken();
    }
}
