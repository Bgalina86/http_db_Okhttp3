package ru.inno;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class CreateNewTaskDemo {
    private final static String URL = "https://todo-app-sky.herokuapp.com/";

    public static void main(String[] args) throws IOException {

        HttpClient client = HttpClientBuilder.create().build();

        String json = """
                {
                 "title": "HTTP Client demo",
                  "completed": false
                }
                """;
        HttpEntity reqBody = new StringEntity(json, ContentType.APPLICATION_JSON);


        HttpPost createNewTaskRequest = new HttpPost(URL);
        createNewTaskRequest.setEntity(reqBody);
//        createNewTaskRequest.addHeader("Content-Type", "application/json");


        HttpResponse createNewTaskResponse = client.execute(createNewTaskRequest);

        String bodyResp = EntityUtils.toString(createNewTaskResponse.getEntity());
        System.out.println(createNewTaskResponse.getStatusLine());
        System.out.println(bodyResp);

    }
}
