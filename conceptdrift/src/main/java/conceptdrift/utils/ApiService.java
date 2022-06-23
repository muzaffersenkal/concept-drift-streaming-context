package conceptdrift.utils;



import okhttp3.*;

public class ApiService {

    OkHttpClient client = new OkHttpClient().newBuilder().build();


    public void sendDrift(String type, long eventTime, long processTime) throws Exception {

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"type\": \""+type+"\",\n  \"eventTime\": "+eventTime+",\n  \"processTime\":"+processTime+"\n}");
        Request request = new Request.Builder()
                .url("http://localhost:8000/drift")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = client.newCall(request).execute();

    }

}