package ru_inno.x_clients;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.blankString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru_inno.x_clients.helper.CompanyApiHelper;
import ru_inno.x_clients.model.AuthResponse;
import ru_inno.x_clients.model.CreateCompanyRequest;
import ru_inno.x_clients.model.CreateCompanyResponse;

public class CompanyContractTest {

    CompanyApiHelper helper;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://x-clients-be.onrender.com";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    public void setUpL() {
        helper = new CompanyApiHelper();
    }

    @Test
    public void status200OnGetCompanies() {
        given()
                .header("ABC", "123")
                .basePath("company")
                .when()
                .get()
                .then()
                .statusCode(200)
                .header("Content-Type", "application/json; charset=utf-8");
    }

    @Test
    public void iCanAuth() {
        String body = """
                {
                      "username": "leonardo",
                      "password": "leads"
                }
                """;

        given()
                .basePath("/auth/login")
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("userToken", is(not((blankString()))));
    }

    @Test
    public void iCanCreateNewCompany() {
        AuthResponse info = helper.auth("leonardo", "leads");

        CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest("Innopolis", "Онлайн-курсы");

        given()
                .basePath("company")
                .body(createCompanyRequest)
                .header("x-client-token", info.userToken())
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .and()
                .body("id", is(greaterThan(0)));
    }

    @Test
    public void getCompany() {
        helper.printCompanyInfo(3458);
    }

    @Test
    public void iCanDeleteCompany() {

        CreateCompanyResponse response = (CreateCompanyResponse) helper.createCompany("Innopolis", "Онлайн-курсы");
        Response r = helper.deleteCompany(response.id());

        r.then().statusCode(200);

    }
}
