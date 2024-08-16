package ru.inno;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class GetTasksDemo {

    // Http Request
    // 1. Method: GET/POST/PUT/DELETE
    // 2. URL
    // 3. Body
    // 4. Header

    // Http Response
    // 1. Status
    // 2. Header
    // 3. Body

    // 1. Получить список задач
    // 2. Создать задачу
    // 3. Удалить задачу
    // 4. Отметить выполненной
    // 5. Переименовать задачу

    // - что это и зачем?
    // - как оно работает?
    // - какие есть понятия?
    // - а как работать с этим "руками"?
    // - а как работать с этим на java?
    //// - google: <name> java client


    private final static String URL = "https://todo-app-sky.herokuapp.com/";

    public static void main(String[] args) throws IOException {

        HttpClient client = HttpClientBuilder.create().build();

        HttpGet request = new HttpGet(URL);

        HttpResponse response = client.execute(request);
        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(response.getStatusLine().getReasonPhrase());
        System.out.println(response.getStatusLine().getProtocolVersion());
        System.out.println(response.getStatusLine());

        // byte
        String body = EntityUtils.toString(response.getEntity());
        System.out.println(body);

        // 2269 -> 2
        // Jackson -> Task[]
        String json = """
                [{"id":2268,"title":"1","completed":null},{"id":2269,"title":"2","completed":null},{"id":2270,"title":"3","completed":null}]
                """;

        // Header[] headers = response.getHeaders("Content-Type");
        Header[] headers = response.getAllHeaders();
        for (Header header : headers) {
            System.out.println(header);
        }
    }
}
