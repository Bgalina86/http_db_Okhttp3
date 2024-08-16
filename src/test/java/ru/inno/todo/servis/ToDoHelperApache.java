package ru.inno.todo.servis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class ToDoHelperApache implements ToDoHelper {

    private ConfProperties properties;

    public void setUp() {
        properties = new ConfProperties();
        properties.getProperty("db.host");
    }

    private final HttpClient client;

    public ToDoHelperApache() {
        this.client = HttpClientBuilder.create().build();
    }

    public Task createNewTask() throws IOException {
        HttpPost createItemReq = new HttpPost(properties.getProperty("db.host"));
        String myContent = "{\"title\" : \"testGA\"}";
        StringEntity entity = new StringEntity(myContent, ContentType.APPLICATION_JSON);
        createItemReq.setEntity(entity);
        HttpResponse response = client.execute(createItemReq);
        String body = EntityUtils.toString(response.getEntity());

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(body, Task.class);
    }


    @Override
    public Task createAddTask(String myContent) throws IOException {
        Task newTask = createNewTask();
        int id = newTask.getId();
        HttpPatch createItemReq = new HttpPatch(properties.getProperty("db.host") + "/" + id);
        StringEntity entity = new StringEntity(myContent, ContentType.APPLICATION_JSON);
        createItemReq.setEntity(entity);
        HttpResponse response = client.execute(createItemReq);
        String body = EntityUtils.toString(response.getEntity());

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(body, Task.class);
    }

    public List<Task> getTasks() throws IOException {
        HttpGet getAll = new HttpGet(properties.getProperty("db.host")); // [ {}, {}, {} ]
        HttpResponse response = client.execute(getAll);
        String body = EntityUtils.toString(response.getEntity());

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(body, new TypeReference<>() {
        });
    }

    public void deleteTask(Task t) throws IOException {
        HttpDelete delete = new HttpDelete(properties.getProperty("db.host") + "/" + t.id());
        client.execute(delete);
    }

    @Override
    public void setCompleted(Task task) throws IOException {
        HttpPatch update = new HttpPatch(properties.getProperty("db.host") + "/" + task.id());
        StringEntity entity = new StringEntity("{\"completed\":true}",
            ContentType.APPLICATION_JSON);
        update.setEntity(entity);

        client.execute(update);
    }
}

