package ru.inno.todo.servis;

import java.io.IOException;
import java.util.List;

public interface ToDoHelper {

    Task createNewTask() throws IOException;
    Task createAddTask(String myContent) throws IOException;

    List<Task> getTasks() throws IOException;

    public void deleteTask(Task t) throws IOException ;

    void setCompleted(Task task) throws IOException;

}
