package ru.inno.builder_demo;

public class UserBuilder {
    private static User futureUser;


    public UserBuilder() {
    }

    public UserBuilder create() {
        futureUser = new User();
        return new UserBuilder();
    }

    public UserBuilder withLastName(String lastName){
        futureUser.lastName = lastName;
        return this;
    }

    public UserBuilder withAge(int age){
        futureUser.age = age;
        return this;
    }

    public UserBuilder withEmail(String email){
        futureUser.email = email;
        return this;
    }

    public static User build() {

        return futureUser;
    }
}
