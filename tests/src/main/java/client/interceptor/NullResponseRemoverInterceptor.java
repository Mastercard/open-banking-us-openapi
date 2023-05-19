package client.interceptor;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

/**
 * Workaround to "Expected the field `specialInstructions` to be an array in the JSON string but got `null`" with OpenAPI Generator 6.6.0.
 * See: https://github.com/OpenAPITools/openapi-generator/issues/12549
 */
public class NullResponseRemoverInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            return response;
        }
        String jsonString = responseBody.string();
        String nullInstructions = "\"specialInstructions\":null";
        jsonString = jsonString.replaceAll(nullInstructions + ",", "");
        jsonString = jsonString.replaceAll(nullInstructions, "");
        ResponseBody newResponseBody = ResponseBody.create(jsonString, responseBody.contentType());
        return response.newBuilder().body(newResponseBody).build();
    }
}