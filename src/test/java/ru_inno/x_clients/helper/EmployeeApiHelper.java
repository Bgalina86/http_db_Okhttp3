package ru_inno.x_clients.helper;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import ru_inno.x_clients.model.AuthRequest;
import ru_inno.x_clients.model.AuthResponse;
//login tecna password tecna-fairy
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
public void  printEmployeeIsCompony (int id){
        //    https://x-clients-be.onrender.com/employee?company=4930
//    curl -X 'GET' \
//  'https://x-clients-be.onrender.com/employee?company=4930' \
//  -H 'accept: application/json'

//    Подключение к БД
//    postgresql://x_clients_user:95PM5lQE0NfzJWDQmLjbZ45ewrz1fLYa@dpg-cqsr9ulumphs73c2q40g-a.frankfurt-postgres.render.com/x_clients_db_fxd0
//
//DRIVER://LOGIN:PASS@HOST/DBNAME
//DRIVER://LOGIN:PASS@HOST:PORT/DBNAME
//
//HOSTNAME dpg-cqsr9ulumphs73c2q40g-a.frankfurt-postgres.render.com
//PORT
//DB NAME x_clients_db_fxd0
//LOGIN x_clients_user
//PASS 95PM5lQE0NfzJWDQmLjbZ45ewrz1fLYa
//DRIVER MySQL | oracle | PostgreSQL | MSSQL
        given()
            .basePath("employee")
            .pathParams("company","id",id)
            .when().get("{}")
            .
}

}
