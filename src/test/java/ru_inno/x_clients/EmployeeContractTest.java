package ru_inno.x_clients;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.is;
import static ru_inno.constClass.Const.HTTP_CODE_CREATE;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import ru_inno.todo.servis.ConfProperties;
import ru_inno.x_clients.helper.EmployeeApiHelper;
import ru_inno.x_clients.model.AuthResponse;
import ru_inno.x_clients.model.CreateEmployeeReqest;
import ru_inno.x_clients.model.Employee;
//import ru_inno.x_clients.testData.AddNewUserCompany;

public class EmployeeContractTest {

    private static ConfProperties properties;
    private static String baseURI;
    EmployeeApiHelper employeeApiHelper;
    private static String username;
    private static String password;


    @BeforeAll
    public static void setUp() {
        properties = new ConfProperties();
        RestAssured.baseURI = properties.getProperty("baseURI");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    public void setUpL() {
        employeeApiHelper = new EmployeeApiHelper();
    }

    @Test
    @DisplayName("Получение списка сотрудников для компании")
    public void getEmployeeCompany() {
        employeeApiHelper.printEmployeeIsCompony(4933);
    }

    @Test
    @DisplayName("Добавить нового сотрудника")
//curl -X 'POST' \
//  'https://x-clients-be.onrender.com/employee' \
//  -H 'accept: application/json' \
//  -H 'x-client-token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYWRtaW4iLCJpYXQiOjE3MjU2NTY1NzAsImV4cCI6MTcyNTY1NzQ3MH0.XnqbCbxmdcj83dfozgYjJ5xrGeGChB7FZOiaMsZ06V0' \
//  -H 'Content-Type: application/json' \
//  -d '{
//  "id": 0,
//  "firstName": "string",
//  "lastName": "string",
//  "middleName": "string",
//  "companyId": 0,
//  "email": "string",
//  "url": "string",
//  "phone": "string",
//  "birthdate": "2024-09-06T21:03:25.986Z",
//  "isActive": true
//}'
    public void iCanAddNewUserCompany() {
        AuthResponse info = employeeApiHelper.auth(username, password);
        CreateEmployeeReqest createEmployeeReqest = new CreateEmployeeReqest("Kolay", "Ivanov",
            "Ivanovich", 4933, "ddbgfb@mail.com", "privet.ru", "+7987884555", "15.06.2000", true
        );
        Response response = RestAssured.given()
            .basePath("employee")
            .body(createEmployeeReqest)
            .header("x-client-token", info.userToken())
            .contentType(ContentType.JSON)
            .when()
            .post();
        int id = response.body().jsonPath().getInt("id");
        response.then()
            .assertThat()
            .statusCode(HTTP_CODE_CREATE)
            .and()
            .body("id", is(greaterThan(0)));

//        RestAssured.given().basePath("employee")
//            .body(createEmployeeReqest).header("x-client-token", info.userToken()).contentType(ContentType.JSON)
//            .when().post().then().assertThat().statusCode(201)
//            .and().body("id", is(greaterThan(0)));
    }

//    @ParameterizedTest
//    @ArgumentsSource(AddNewUserCompany.class)
//    public void iCanAddNewUserCompanyParameter(Employee createEmployeeReqest) {
//        AuthResponse info = employeeApiHelper.auth(username, password);
//        //CreateEmployeeReqest employeeReqest = new CreateEmployeeReqest(createEmployeeReqest);
//        Response response = RestAssured.given()
//            .basePath("employee")
//            .body(createEmployeeReqest)
//            .header("x-client-token", info.userToken())
//            .contentType(ContentType.JSON)
//            .when()
//            .post();
//        int id = response.body().jsonPath().getInt("id");
//        response.then()
//            .assertThat()
//            .statusCode(HTTP_CODE_CREATE)
//            .and()
//            .body("id", is(greaterThan(0)));

//        RestAssured.given().basePath("employee")
//            .body(createEmployeeReqest).header("x-client-token", info.userToken()).contentType(ContentType.JSON)
//            .when().post().then().assertThat().statusCode(201)
//            .and().body("id", is(greaterThan(0)));
    }
}
