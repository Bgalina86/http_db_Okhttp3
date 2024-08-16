package ru.inno.todo.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ToDoContractTest {
    // TODO: ���������� �� ����� | Owner
    private static final String URL = "https://todo-app-sky.herokuapp.com";

    private HttpClient client;

    @BeforeEach
    public void setUp() {
        client = HttpClientBuilder.create().build();
    }

    @Test
    @DisplayName("��������� ������ �����. ��������� ������-��� � ��������� Content-Type")
    public void shouldReceive200OnGetRequest() throws IOException {
        // ������
        HttpGet getListReq = new HttpGet(URL);
        // �������� �����
        HttpResponse response = client.execute(getListReq);
        String body = EntityUtils.toString(response.getEntity());

        // ��������� ���� ������
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(1, response.getHeaders("Content-Type").length);
        assertEquals("application/json; charset=utf-8", response.getHeaders("Content-Type")[0].getValue());
        assertTrue(body.startsWith("["));
        assertTrue(body.endsWith("]"));
    }

    @Test
    @DisplayName("�������� ������. ��������� ������-���, ��������� Content-Type � ���� ������ �������� json")
    public void shouldReceive201OnPostRequest() throws IOException {
        HttpResponse response = createNewTask();
        String body = EntityUtils.toString(response.getEntity());

        // ��������� ���� ������
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(1, response.getHeaders("Content-Type").length);
        assertEquals("application/json; charset=utf-8", response.getHeaders("Content-Type")[0].getValue());
        assertTrue(body.startsWith("{"));
        assertTrue(body.endsWith("}"));
    }

    @Test
    @DisplayName("�������� ������ � ������ ����� �������. ������-��� = 400, � ���� ������ ���� ��������� �� ������")
    public void shouldReceive400OnEmptyPost() throws IOException {
        // ������
        HttpPost createItemReq = new HttpPost(URL);

        // �������� �����
        HttpResponse response = client.execute(createItemReq);
        String bodyAsIs = EntityUtils.toString(response.getEntity());

        // ��������� ���� ������
        assertEquals(1, response.getHeaders("Content-Type").length);
        assertEquals("application/json; charset=utf-8", response.getHeaders("Content-Type")[0].getValue());
        assertTrue(bodyAsIs.endsWith(",\"title\":null,\"completed\":null}"));
    }

    @Test
    @DisplayName("������� ������������ ������. ������ 204, Content-Length=0")
    public void shouldReceive204OnDelete() throws IOException {
        // ������� ������, ������� ����� �������
        HttpResponse newTask = createNewTask();
        String body = EntityUtils.toString(newTask.getEntity());
        String id = "/" + body.substring(6, 10); // {"id":12364,"title":"test","completed":null}

        String myUrl = URL + id;
        HttpDelete deleteTaskReq = new HttpDelete(myUrl);
        HttpResponse response = client.execute(deleteTaskReq);
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(1, response.getHeaders("Content-Length").length);
        assertEquals("\"todo was deleted\"", EntityUtils.toString(response.getEntity()));
    }

    //TODO: ����� �����! �.�������
    private HttpResponse createNewTask() throws IOException {
        // ������
        HttpPost createItemReq = new HttpPost(URL);

        String myContent = "{\"title\" : \"test\"}";
        StringEntity entity = new StringEntity(myContent, ContentType.APPLICATION_JSON);
        createItemReq.setEntity(entity);

        // �������� �����
        return client.execute(createItemReq);
    }
}
