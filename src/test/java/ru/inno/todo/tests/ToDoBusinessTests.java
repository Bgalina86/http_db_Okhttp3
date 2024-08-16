package ru.inno.todo.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.inno.todo.servis.Task;
import ru.inno.todo.servis.ToDoHelper;
import ru.inno.todo.servis.ToDoHelperApache;

public class ToDoBusinessTests {
    ToDoHelper service;

    @BeforeEach
    public void setService() {
        service = new ToDoHelperApache();
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
