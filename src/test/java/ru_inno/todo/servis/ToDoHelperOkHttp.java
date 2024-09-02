package ru_inno.todo.servis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru_inno.todo.interceptor.LoggingInterceptor;

public class ToDoHelperOkHttp implements ToDoHelper {

    public void setUp() {
        ConfProperties properties = new ConfProperties();
        ConfProperties.getProperty("db.host");
    }
    private final OkHttpClient client;


    public ToDoHelperOkHttp() {
        Interceptor interceptor = new LoggingInterceptor();

        client = new OkHttpClient.Builder().addNetworkInterceptor(interceptor).build();
    }

    @Override
    public Task createNewTask() throws IOException {
        RequestBody body = RequestBody.create("{\"title\" : \"test\"}", MyMediaTypes.JSON);
        Request request = new Request.Builder().url(ConfProperties.getProperty("db.host")).post(body).build();
        Response response = client.newCall(request).execute();
        return new ObjectMapper().readValue(response.body().string(), Task.class);
    }

    @Override
    public Task createAddTask(String myContent) throws IOException {
        //создние задачи
        Task newTask = createNewTask();
        // Выделяем id
        int id = newTask.id();
        //определяем тело запроса
        RequestBody body = RequestBody.create(myContent, MyMediaTypes.JSON);
        // Составляем запрос с новым телом
        Request request = new Request.Builder().url(ConfProperties.getProperty("db.host") + "/" + id).patch(body).build();
        //отправляем запрос
        Response response = client.newCall(request).execute();

        Request getAll = new Request.Builder().get().url(
            ConfProperties.getProperty("db.host") + "/" + id).build();
        Response newResponse = this.client.newCall(getAll).execute();
        String responseBody = newResponse.body().string();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(responseBody, new TypeReference<>() {
        });
    }

    @Override
    public List<Task> getTasks() throws IOException {
        Request getAll = new Request.Builder().get().url(ConfProperties.getProperty("db.host")).build();
        Response response = this.client.newCall(getAll).execute();
        String body = response.body().string();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(body, new TypeReference<>() {
        });
    }

    @Override
    public void deleteTask(Task t) throws IOException {
        Request delete = new Request.Builder().delete().url(
            ConfProperties.getProperty("db.host") + "/" + t.id()).build();
        client.newCall(delete).execute();
    }

    @Override
    public void setCompleted(Task task) throws IOException {
        RequestBody body = RequestBody.create("{\"completed\":true}", MyMediaTypes.JSON);

        Request setCompleted = new Request.Builder()
            .url(ConfProperties.getProperty("db.host") + "/" + task.id())
            .patch(body)
            .build();

        client.newCall(setCompleted).execute();
    }
}
