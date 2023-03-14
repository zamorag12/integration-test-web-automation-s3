package com.corecodeqa.controller;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class EmployeeControllerTest {

    @BeforeAll
    public static void setup() {
        baseURI = "http://localhost:8080";
    }

    @Test
    public void testEmployeesStatus() {
        given().
                // baseUri("http://localhost:8080").
                        when().
                get("/employees").
                then().
                log().all().
                assertThat().
                statusCode(200);
    }

    @Test
    public void testEmployeesResponseBody() {
        given().
                // baseUri("http://localhost:8080").
                        when().
                get("/employees").
                then().
                log().all().
                assertThat().
                statusCode(200).
                body("id", hasItems(1, 2),
                        "name", hasItems("User-1", "User-2"),
                        "role", hasItems("Admin", "Supervisor"),
                        "id[0]", equalTo(1),
                        "name[0]", is(equalTo("User-1")),
                        "size()", equalTo(2)
                );
    }

    @Test
    public void testGetEmployeeWithParam() {
        Response empResponse = given().
                // baseUri("http://localhost:8080").
                        contentType(ContentType.JSON).
                pathParam("id", "1").
                when().
                get("/employee/{id}").
                then().
                log().all().
                assertThat().
                statusCode(200).
                extract().
                response();

        JsonPath jsonPathObj = empResponse.jsonPath();
        Assertions.assertEquals(jsonPathObj.getLong("id"), 1);
        Assertions.assertEquals(jsonPathObj.getString("name"), "User-1");
        Assertions.assertEquals(jsonPathObj.getString("role"), "Admin");
    }

    @Test
    public void extractGetEmployeesResponse() {
        Response res = given().
                // baseUri("http://localhost:8080").
                        when().
                get("/employees").
                then().
                log().all().
                assertThat().
                statusCode(200).
                extract().
                response();
        System.out.println("response = " + res.asString());
    }

    @Test
    public void testPostEmployee() throws JSONException {
        JSONObject empParams = new JSONObject();
        empParams.put("name", "corecodeqaUser44");
        empParams.put("role", "Supervisor");

        given()
                .contentType(ContentType.JSON)
                .body(empParams.toString())
                .log().all()

                .when()
                .post("http://localhost:8080/employee")

                .then()
                .assertThat().statusCode(200)
                .body("name", equalTo("corecodeqaUser44"))
                .body("role", equalTo("Supervisor"))
                .log().all();

    }
}
