package ca.gbc.productservice;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

    // This annotation is used with TestContainers to configure the connection to container automatically.
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo");


    // To have the actual port used in @SpringBootTest annotation.
    @LocalServerPort
    private Integer port;


    // http://localhost:8085/api/product

    @BeforeEach
    void setUp() {

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

    }

    @AfterEach
    void cleanUp() {
        RestAssured.given()
                .when()
                .get("/api/product")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("id", String.class)
                .forEach(id ->
                        RestAssured.given()
                                .when()
                                .delete("/api/product/{id}", id)
                                .then()
                                .statusCode(204)
                );
    }


    static {
        mongoDBContainer.start();
    }


    static String requestBody = """
                {
                    "name": "LG Flat-screen TV",
                    "description": "2023 Model",
                    "price": 6000.00
                }
                """;


    @Test
    void contextLoads() {

    }

    @Test
    void createProductTest() {

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.equalTo("LG Flat-screen TV"))
                .body("description", Matchers.equalTo("2023 Model"))
                .body("price", Matchers.equalTo(6000.00f));

    }

    @Test
    void getAllProductsTest() {

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.equalTo("LG Flat-screen TV"))
                .body("description", Matchers.equalTo("2023 Model"))
                .body("price", Matchers.equalTo(6000.00f));

        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/product")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0))
                .body("[0].id", Matchers.notNullValue())
                .body("[0].name", Matchers.equalTo("LG Flat-screen TV"))
                .body("[0].description", Matchers.equalTo("2023 Model"))
                .body("[0].price", Matchers.equalTo(6000.00f));

    }


    @Test
    void updateProductTest() {

        String productId = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.equalTo("LG Flat-screen TV"))
                .body("description", Matchers.equalTo("2023 Model"))
                .body("price", Matchers.equalTo(6000.00f))
                .extract()
                .path("id");

        String updatedBody = """
                {
                    "name": "LG Updated TV",
                    "description": "Updated Description",
                    "price": 7000.00
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(updatedBody)
                .when()
                .put("/api/product/{id}", productId)
                .then()
                .log().all()
                .statusCode(204);

    }


    @Test
    void deleteProductTest() {

        String productId = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("name", Matchers.equalTo("LG Flat-screen TV"))
                .body("description", Matchers.equalTo("2023 Model"))
                .body("price", Matchers.equalTo(6000.00f))
                .extract()
                .path("id");

        RestAssured.given()
                .contentType("application/json")
                .when()
                .delete("/api/product/{id}", productId)
                .then()
                .log().all()
                .statusCode(204);

    }

}
