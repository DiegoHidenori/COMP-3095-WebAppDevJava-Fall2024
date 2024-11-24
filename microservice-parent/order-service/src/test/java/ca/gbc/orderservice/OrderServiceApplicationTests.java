package ca.gbc.orderservice;

import ca.gbc.orderservice.stub.InventoryClientStub;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.org.hamcrest.Matchers;

import static org.hamcrest.Matchers.equalTo;
import static org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceApplicationTests {

	@ServiceConnection
	static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine");


	@LocalServerPort
	private Integer port;


	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		postgreSQLContainer.start();
	}

	@Test
	void shouldSubmitOrder() {

		String submitOrderJson = """
				{
					"skuCode": "testing lala phone",
					"price": 10000,
					"quantity": 15
				}
				""";

		// Week 10
		// Mock a call to inventory-service
		InventoryClientStub.stubInventoryCall("testing lala phone", 15);

//		var responseBodyString = RestAssured.given()
//				.contentType("application/json")
//				.body(submitOrderJson)
//				.when()
//				.post("/api/order")
//				.then()
//				.log().all()
//				.statusCode(201)
//				.extract()
//				.body().asString();
//
//		assertThat(responseBodyString, Matchers.is("Order placed successfully."));
		RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson)
				.when()
				.post("/api/order")
				.then()
				.log().all()
				.statusCode(201)
				.body(equalTo("Order placed successfully"));

	}


	@Test
	void contextLoads() {
	}

}
