package ru.inno.todo.servis;

public record Task(int id, String title, boolean completed) {

    public int getId() {
        return id;
    }
}