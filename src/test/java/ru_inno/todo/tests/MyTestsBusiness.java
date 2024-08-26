package ru_inno.todo.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru_inno.todo.servis.ToDoHelperOkHttp;
import ru_inno.todo.servis.Task;
import ru_inno.todo.servis.ToDoHelper;

public class MyTestsBusiness {

    ToDoHelper service;

    @BeforeEach
    //Работа с библиотекой Apache
    //public void setService() {service = new ToDoHelperApache();}

    //Работа с библиотекой OkHttp3 версия 4.11
    public void setService() {
        service = new ToDoHelperOkHttp();
    }

    @Test
    @DisplayName("Я могу создать задачу")
    public void iCanAddMyTask() throws IOException {
        Task task = service.createNewTask();
        List<Task> tasks = service.getTasks();

        assertTrue(tasks.contains(task));
    }

    @Test
    @DisplayName("Я могу переименовать задачу")
    public void iCanAddMyCreateTask() throws IOException {
        Task createTask = service.createAddTask("{\"title\" : \"test34\"}");
        List<Task> createTasks = service.getTasks();

        assertTrue(createTasks.contains(createTask));
    }

    @Test
    @DisplayName("Я могу удалить задачу")
    public void iCanDeleteMyTask() throws IOException {
        Task task = service.createNewTask();
        service.deleteTask(task);
        List<Task> tasks = service.getTasks();

        assertFalse(tasks.contains(task));
    }

    @Test
    @DisplayName("Я могу отметить задачу выполненной")
    public void iCanSetMyTaskCompleted() throws IOException {
        Task myTask = service.createNewTask();
        service.setCompleted(myTask);

        List<Task> tasks = service.getTasks();

        Task taskToAssert = null;
        for (Task task : tasks) {
            if (task.id() == myTask.id()) {
                taskToAssert = task;
            }
        }

        assertNotNull(taskToAssert);
        assertTrue(taskToAssert.completed());
    }
}
