package client.interceptor;

import com.google.gson.*;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.HashSet;

/**
 * Workaround to "Expected the field `{fieldName}` to be an array / a primitive type in the JSON string but got `null`"
 * with OpenAPI Generator 6.6.0.
 * See: https://github.com/OpenAPITools/openapi-generator/issues/12549
 */
public class NullRemoverResponseInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        ResponseBody body = response.body();
        if (body == null) {
            return response;
        }
        String updatedResponse = NullRemover.removeNullFields(body.string());
        ResponseBody newResponseBody = ResponseBody.create(updatedResponse, body.contentType());
        return response.newBuilder().body(newResponseBody).build();
    }

    public static class NullRemover {

        private NullRemover() {
        }

        public static String removeNullFields(String json) {
            JsonElement element;
            try {
                element = new Gson().fromJson(json, JsonElement.class);
            } catch (JsonSyntaxException e) {
                return json;
            }
            if (element == null) {
                return json;
            }
            removeNullFields(element);
            return element.toString();
        }

        private static void removeNullFields(JsonElement element) {
            if (element.isJsonObject()) {
                JsonObject object = element.getAsJsonObject();
                new HashSet<>(object.keySet()).forEach(k -> {
                    JsonElement child = object.get(k);
                    if (child.isJsonNull()) {
                        object.remove(k);
                    } else {
                        removeNullFields(child);
                    }
                });
            }
            if (element.isJsonArray()) {
                JsonArray array = element.getAsJsonArray();
                array.forEach(NullRemover::removeNullFields);
            }
        }
    }
}