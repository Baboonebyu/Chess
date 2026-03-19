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

    public static <T extends Response> Response post(String serverUrl, String path , Request request, Class<T> responseClass,String token) throws Exception {
        HttpRequest httpRequest = requestBuilder("POST", serverUrl,path,request,token);
        var hResponse = sendRequest(httpRequest);

        return handleResponse(hResponse,responseClass);
    }

    public static <T extends Response> Response get(String serverUrl, String path , Request request, Class<T> responseClass,String token) throws Exception {
        HttpRequest httpRequest = requestBuilder("GET", serverUrl,path,request,token);
        var hResponse = sendRequest(httpRequest);

        return handleResponse(hResponse,responseClass);
    }


    public static <T extends Response> Response delete(String serverUrl, String path , Request request, Class<T> responseClass,String token) throws Exception {
        HttpRequest httpRequest = requestBuilder("Delete", serverUrl,path,request,token);

        var hResponse = sendRequest(httpRequest);

        return handleResponse(hResponse,responseClass);
    }





    private static HttpRequest requestBuilder(String method,String serverUrl, String path, Object body,String token){

        var hRequest = HttpRequest.newBuilder()

                .uri(URI.create(serverUrl + path))
                .timeout(java.time.Duration.ofMillis(TIMEOUT_MILLIS))
                .method(method, makeRequestBody(body));
        if(token != null)
        {
            hRequest.header("Authorization",token);
        }




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
                if(status == 403){
                    throw new Exception("This username is already taken");
                } else if (status == 401) {
                    throw new Exception("Bad password");
                }
                else if (status == 400) {
                    throw new Exception("Something was wrong with your request");
                }
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
