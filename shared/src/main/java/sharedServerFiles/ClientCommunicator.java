package sharedServerFiles;

import com.google.gson.Gson;
import model.Request;
import model.Response;

import java.net.URI;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;


public class ClientCommunicator {
    private static final int TIMEOUT_MILLIS = 5000;
    private static final HttpClient client = HttpClient.newHttpClient();

    public static <T extends Response> Response post(String serverUrl, String path , Request request, Class<T> responseClass) throws Exception {
        HttpRequest httpRequest = requestBuilder("POST", serverUrl,path,request);
        var hResponse = sendRequest(httpRequest);
        Response response = handleResponse(hResponse,responseClass);

        return response;
    }


    private static HttpRequest requestBuilder(String method,String serverUrl, String path, Object body){
        var hRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .timeout(java.time.Duration.ofMillis(TIMEOUT_MILLIS))
                .method(method, makeRequestBody(body));
        if (body != null) {
            hRequest.setHeader("Content-Type", "application/json");
        }
        return hRequest.build();
    }

    private static BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }
    private static HttpResponse<String> sendRequest(HttpRequest request) throws Exception {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new Exception( ex.getMessage());
        }
    }



    private static <T extends Response> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws Exception {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw new Exception("This one has a body" +status);
            }

            throw new Exception(String.valueOf(status));
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private static boolean isSuccessful(int status) {
        return status / 100 == 2;
    }


}
