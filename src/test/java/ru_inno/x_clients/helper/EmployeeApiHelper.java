package ru_inno.x_clients.helper;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import ru_inno.x_clients.model.AuthRequest;
import ru_inno.x_clients.model.AuthResponse;


public class EmployeeApiHelper {

    public AuthResponse auth(String username, String password) {
        AuthRequest authRequest = new AuthRequest(username, password);

        return given()
            .basePath("/auth/login")
            .body(authRequest)
            .contentType(ContentType.JSON)
            .when()
            .post()
            .as(AuthResponse.class);
    }

    public void printEmployeeIsCompony(int id) {
        //    https://x-clients-be.onrender.com/employee?company=4933
        //    curl -X 'GET' \
        //  'https://x-clients-be.onrender.com/employee?company=4933' \
        //  -H 'accept: application/json'
        given()
            .basePath("employee")
            .queryParam("company", id)
            .when()
            .get()
            .body().prettyPrint();
    }

}
