package ru.inno.todo.tests;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.inno.todo.servis.ConfProperties;
import ru.inno.todo.servis.ToDoHelper;
import ru.inno.todo.servis.ToDoHelperApache;

/*  - получение списка - done
    - создание новой задачи - done
    - переименование задачи
    - отметка задачи выполненной
    - удаление задачи  - - done  и посмотреть с пустым и невалидным телом
    */
@SuppressWarnings("ALL")
public class MyTestsContract {

    private ConfProperties properties;
    private HttpClient client;
    ToDoHelper service;

    @BeforeEach
    public void setUp() {
        properties = new ConfProperties();
        properties.getProperty("db.host");
        client = HttpClientBuilder.create().build();
    }

    private String getIdTask() throws IOException {
        HttpResponse newTask = createNewTask();
        String body = EntityUtils.toString(newTask.getEntity());
        String id = body.substring(6, 10);
        return id;
    }

    public void setService() {
        service = new ToDoHelperApache();
    }

    @AfterEach
    public void tearDown() {
        return;
    }

    @Test
    @Tag("позитивная проверка")
    @DisplayName("Получение списка задач. Проверяем статус-код и заголовок Content-Type")
    public void shouldReceive200OnGetRequest() throws IOException {
        // Запрос
        HttpGet getListReq = new HttpGet(properties.getProperty("db.host"));
        // Получить ответ
        HttpResponse response = client.execute(getListReq);
        String body = EntityUtils.toString(response.getEntity());

        // Проверить поля ответа
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(1, response.getHeaders("Content-Type").length);
        assertEquals("application/json; charset=utf-8",
            response.getHeaders("Content-Type")[0].getValue());
        assertTrue(body.startsWith("["));
        assertTrue(body.endsWith("]"));
    }

    @Test
    @Tag("позитивная проверка")
    @DisplayName("Создание задачи. Проверяем статус-код, заголовок Content-Type и тело ответа содержит json")
    public void shouldReceive201OnPostRequest() throws IOException {
        HttpResponse response = createNewTask();
        String body = EntityUtils.toString(response.getEntity());

        // Проверить поля ответа
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals(1, response.getHeaders("Content-Type").length);
        assertEquals("application/json; charset=utf-8",
            response.getHeaders("Content-Type")[0].getValue());
        assertTrue(body.startsWith("{"));
        assertTrue(body.endsWith("}"));
    }

    private HttpResponse createNewTask() throws IOException {
        // Запрос
        HttpPost createItemReq = new HttpPost(properties.getProperty("db.host"));

        String myContent = "{\"title\" : \"testGA\"}";
        StringEntity entity = new StringEntity(myContent, ContentType.APPLICATION_JSON);
        createItemReq.setEntity(entity);

        // Получить ответ
        return client.execute(createItemReq);
    }

    @Test
    @Tag("негативная проверка")
    @DisplayName("Создание задачи. Проверяем статус-код, заголовок Content-Type и тело ответа содержит не json")
    public void shouldReceive201OnPostRequestBedMyContent() throws IOException {
        HttpResponse response = createNewTaskBed();
        String body = EntityUtils.toString(response.getEntity());

        // Проверить поля ответа
        assertEquals(400, response.getStatusLine().getStatusCode());
        assertEquals(1, response.getHeaders("Content-Type").length);
        assertEquals("text/html; charset=utf-8", response.getHeaders("Content-Type")[0].getValue());
        assertFalse(body.startsWith("{"));
        assertFalse(body.endsWith("}"));
    }

    private HttpResponse createNewTaskBed() throws IOException {
        // Запрос
        HttpPost createItemReq = new HttpPost(properties.getProperty("db.host"));

        String myContent = "\"test\" : \"title\"";
        StringEntity entity = new StringEntity(myContent, ContentType.APPLICATION_JSON);
        createItemReq.setEntity(entity);

        // Получить ответ
        return client.execute(createItemReq);
    }

    @Test
    @Tag("негативная проверка")
    @DisplayName("Создание задачи с пустым телом запроса. Статус-код = 400, в теле ответа есть сообщение об ошибке")
    public void shouldReceive400OnEmptyPost() throws IOException {
        // Запрос
        HttpPost createItemReq = new HttpPost(properties.getProperty("db.host"));

        // Получить ответ
        HttpResponse response = client.execute(createItemReq);
        String bodyAsIs = EntityUtils.toString(response.getEntity());

        // Проверить поля ответа
        assertEquals(1, response.getHeaders("Content-Type").length);
        assertEquals("application/json; charset=utf-8",
            response.getHeaders("Content-Type")[0].getValue());
        assertTrue(bodyAsIs.endsWith(",\"title\":null,\"completed\":null}"));
    }

    @Test
    @Tag("позитивная проверка")
    @DisplayName("Удаляет существующую задачу. Статус 200, Content-Length=0")
    public void shouldReceive204OnDelete() throws IOException {
        HttpDelete idTask = new HttpDelete(properties.getProperty("db.host") +"/" + getIdTask());
        HttpResponse responseDelete = client.execute(idTask);
        assertEquals(200, responseDelete.getStatusLine().getStatusCode());//503
        assertEquals(1, responseDelete.getHeaders("Content-Length").length);
        assertEquals("\"todo was deleted\"", EntityUtils.toString(responseDelete.getEntity()));
    }

      // - переименование задачи
    @Test
    @Tag("позитивная проверка")
    @DisplayName("Переименование задачи. Добавляем цифры в title")
    public void renamingTaskAddNumbers() throws IOException {
        //Отправляем запрос на корректировку наименования и выделяем из тела наименование новой задачи
        HttpPatch idTask = new HttpPatch(properties.getProperty("db.host") + "/" + getIdTask());
        String myContent = "{\"id\":" + getIdTask() + ", \"title\" : \"test34\"}";
        StringEntity entity = new StringEntity(myContent, ContentType.APPLICATION_JSON);
        idTask.setEntity(entity);
        String bodyNew = EntityUtils.toString(idTask.getEntity());
        HttpResponse response = client.execute(idTask);
        String title = bodyNew.substring(23, 29);

        //Проверка измененой задачи по коду и наименованию задачи
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("test34", title);
    }

    @Test
    @Tag("негативная проверка")
    @DisplayName("Переименование задачи. title = пустая строка")
    public void renamingTaskNullTitle() throws IOException {
    //Отправляем запрос на корректировку наименования и выделяем из тела наименование новой задачи
        HttpPatch idTask = new HttpPatch(properties.getProperty("db.host") + "/" + getIdTask());
        StringEntity entity = new StringEntity("{\"id\":" + getIdTask() + ",\"title\" : }",
            ContentType.APPLICATION_JSON);
        idTask.setEntity(entity);
        String bodyNew = EntityUtils.toString(idTask.getEntity());
        HttpResponse response = client.execute(idTask);
        String title = bodyNew.substring(12, 12); // {"id":1236,"title":"","completed":null}

        //Проверка измененой задачи по коду, заголовкам и наименованию задачи
        assertEquals(400, response.getStatusLine().getStatusCode());
        assertEquals("", title);
    }

    @Test
    @Tag("негативная проверка")
    @DisplayName("Добавляем спец символов в title.")
    public void renamingTaskSymbolsTitle() throws IOException {
    //Отправляем запрос на корректировку наименования и выделяем из тела наименование новой задачи
        HttpPatch idTask = new HttpPatch(properties.getProperty("db.host") + "/" + getIdTask());
        StringEntity entity = new StringEntity("{\"id\":" + getIdTask() + ",\"title\" : !@#$%^&*(_)/~}",
            ContentType.APPLICATION_JSON);
        idTask.setEntity(entity);
        String bodyNew = EntityUtils.toString(idTask.getEntity());
        HttpResponse response = client.execute(idTask);
        String title = bodyNew.substring(21, 34);

        //Проверка измененой задачи по коду, заголовкам и наименованию задачи
        assertEquals(400, response.getStatusLine().getStatusCode());
        assertEquals("!@#$%^&*(_)/~", title);
    }

    @Test
    @Tag("негативная проверка")
    @Tag("Ошибка. Будет правиться в Jirp-123")
    @DisplayName("Переименование задачи. title = NULL.")
    public void renamingTaskNulllsTitle() throws IOException {
     //Отправляем запрос на корректировку наименования и выделяем из тела наименование новой задачи
        HttpPatch idTask = new HttpPatch(properties.getProperty("db.host") + "/" + getIdTask());

        StringEntity entity = new StringEntity("{\"id\":" + getIdTask() + ",\"title\" : null}",
            ContentType.APPLICATION_JSON);
        idTask.setEntity(entity);
        String bodyNew = EntityUtils.toString(idTask.getEntity());
        HttpResponse response = client.execute(idTask);
        String title = bodyNew.substring(21, 21); // {"id":1236,"title":null,"completed":null}

        //Проверка измененой задачи по коду, заголовкам и наименованию задачи
        assertEquals(400, response.getStatusLine().getStatusCode());
        assertNull(title);
    }

    @Test
    @Tag("негативная проверка")
    @DisplayName("Переименование задачи. title = Script")
    public void renamingTaskScriptTitle() throws IOException {
       //Отправляем запрос на корректировку наименования и выделяем из тела наименование новой задачи
        HttpPatch idTask = new HttpPatch(properties.getProperty("db.host") + "/" + getIdTask());

        StringEntity entity = new StringEntity(
            "{\"id\":" + getIdTask() + ",\"title\" : <script>alert(‘XSS’)</script>}",
            ContentType.APPLICATION_JSON);
        idTask.setEntity(entity);
        String bodyNew = EntityUtils.toString(idTask.getEntity());
        HttpResponse response = client.execute(idTask);
        String title = bodyNew.substring(21, 50); // {"id":1236,"title":null,"completed":null}

        //Проверка измененой задачи по коду, заголовкам и наименованию задачи
        assertEquals(400, response.getStatusLine().getStatusCode());
        assertEquals("<script>alert(‘XSS’)</script>", title);
    }

    @Test
    @Tag("негативная проверка")
    @DisplayName("Переименование задачи. title = Script JS")
    public void renamingTaskScriptJSTitle() throws IOException {
         //Отправляем запрос на корректировку наименования и выделяем из тела наименование новой задачи
        HttpPatch idTask = new HttpPatch(properties.getProperty("db.host") + "/" + getIdTask());

        StringEntity entity = new StringEntity(
            "{\"id\":" + getIdTask() + ",\"title\" : javascript:alert('alert');}",
            ContentType.APPLICATION_JSON);
        idTask.setEntity(entity);
        String bodyNew = EntityUtils.toString(idTask.getEntity());
        HttpResponse response = client.execute(idTask);
        String title = bodyNew.substring(21, 47); // {"id":1236,"title":null,"completed":null}

        //Проверка измененой задачи по коду, заголовкам и наименованию задачи
        assertEquals("javascript:alert('alert');", title);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    @Tag("негативная проверка")
    @DisplayName("Переименование задачи. title = Иероглифы")
    public void renamingTaskHieroglyphsTitle() throws IOException {
        //Отправляем запрос на корректировку наименования и выделяем из тела наименование новой задачи
        HttpPatch idTask = new HttpPatch(properties.getProperty("db.host") + "/" + getIdTask());

        StringEntity entity = new StringEntity(
            "{\"id\":" + getIdTask() + ",\"title\" : 中国的 한국의 éàòù ÀàÁáÈèÉéÌìÍíÒòÓóÙùÚú NÑO äöüÄÖÜß}",
            ContentType.APPLICATION_JSON);
        idTask.setEntity(entity);
        String bodyNew = EntityUtils.toString(idTask.getEntity());
        HttpResponse response = client.execute(idTask);
        String title = bodyNew.substring(21, 66); // {"id":1236,"title":null,"completed":null}

        //Проверка измененой задачи по коду, заголовкам и наименованию задачи
        assertEquals("中国的 한국의 éàòù ÀàÁáÈèÉéÌìÍíÒòÓóÙùÚú NÑO äöüÄÖÜß", title);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    @Tag("негативная проверка")
    @DisplayName("Переименование задачи. titlr содержит 294 знака")
    public void renamingTask294SymbolsTitle() throws IOException {
        //Отправляем запрос на корректировку наименования и выделяем из тела наименование новой задачи
        HttpPatch idTask = new HttpPatch(properties.getProperty("db.host") + "/" + getIdTask());

        StringEntity entity = new StringEntity("{\"id\":" + getIdTask()
            + ",\"title\" : Задача организации, в особенности же дальнейшее развитие различных форм деятельности представляет собой интересный эксперимент проверки модели развития. Однозначно, действия представителей оппозиции формируют глобальную экономическую сеть и при этом — в равной степени предоставлены сами себе.}",
            ContentType.APPLICATION_JSON);
        idTask.setEntity(entity);
        String bodyNew = EntityUtils.toString(idTask.getEntity());
        HttpResponse response = client.execute(idTask);
        String title = bodyNew.substring(21, 314);

        //Проверка измененой задачи по коду, заголовкам и наименованию задачи
        assertEquals(
            "Задача организации, в особенности же дальнейшее развитие различных форм деятельности представляет собой интересный эксперимент проверки модели развития. Однозначно, действия представителей оппозиции формируют глобальную экономическую сеть и при этом — в равной степени предоставлены сами себе.",
            title);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    //- отметка задачи выполненной
    @Test
    @Tag("позитивная проверка")
    @DisplayName("Отметка о выполнении задачи.")
    public void doneTask() throws IOException {

        //Отправляем запрос на корректировку наименования и выделяем из тела наименование новой задачи
        HttpPatch idTask = new HttpPatch(properties.getProperty("db.host") + "/" + getIdTask());
        StringEntity entity = new StringEntity("{\"completed\" : true}",
            ContentType.APPLICATION_JSON);
        idTask.setEntity(entity);
        String bodyNew = EntityUtils.toString(idTask.getEntity());
        HttpResponse response = client.execute(idTask);
        String completed = bodyNew.substring(15, 19); // {"id":1236,"title":"","completed":null}

        //Проверка измененой задачи по коду, заголовкам и наименованию задачи
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("true", completed);
    }
}
