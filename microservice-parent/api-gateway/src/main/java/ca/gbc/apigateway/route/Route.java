package ca.gbc.apigateway.route;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Configuration
@Slf4j
public class Route {

    @Value("${product.service.url}")    // Dynamic. Either localhost or container
    private String productServiceUrl;

    @Value("${order.service.url}")
    private String orderServiceUrl;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> productServiceRoute() {

        log.info("Initializing product service route with URL: {}", productServiceUrl);
        return route("product_service")
                .route(RequestPredicates.path("/api/product/**"), request -> {

                    log.info("Request received for product-service: {}", request.uri());
                    return HandlerFunctions.http(productServiceUrl).handle(request);

//                    try{
//                        ServerResponse response = HandlerFunctions.http(productServiceUrl).handle(request);
//                        log.info("Response status: {}", response.statusCode());
//                        return response;
//                    } catch (Exception e) {
//                        log.error("Error occurred while routing to: {}", e.getMessage(), e);
//                        return ServerResponse.status(500).body("Error occurred when routing to product-service");
//                    }

                })
                .filter(CircuitBreakerFilterFunctions
                        .circuitBreaker("productServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {

        log.info("Initializing order service route with URL: {}", orderServiceUrl);
        return route("order_service")
                .route(RequestPredicates.path("/api/order/**"), request -> {

                    log.info("Request received for order-service: {}", request.uri());
                    return HandlerFunctions.http(orderServiceUrl).handle(request);

//                    try{
//                        ServerResponse response = HandlerFunctions.http(orderServiceUrl).handle(request);
//                        log.info("Response status: {}", response.statusCode());
//                        return response;
//                    } catch (Exception e) {
//                        log.error("Error occurred while routing to: {}", e.getMessage(), e);
//                        return ServerResponse.status(500).body("Error occurred when routing to order-service");
//                    }

                })
                .filter(CircuitBreakerFilterFunctions
                        .circuitBreaker("orderServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoute() {

        log.info("Initializing inventory service route with URL: {}", inventoryServiceUrl);
        return route("inventory_service")
                .route(RequestPredicates.path("/api/inventory/**"), request -> {

                    log.info("Request received for inventory-service: {}", request.uri());
                    return HandlerFunctions.http(inventoryServiceUrl).handle(request);

//                    try{
//                        ServerResponse response = HandlerFunctions.http(inventoryServiceUrl).handle(request);
//                        log.info("Response status: {}", response.statusCode());
//                        return response;
//                    } catch (Exception e) {
//                        log.error("Error occurred while routing to: {}", e.getMessage(), e);
//                        return ServerResponse.status(500).body("Error occurred when routing to inventory-service");
//                    }

                })
                .filter(CircuitBreakerFilterFunctions
                        .circuitBreaker("orderServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> productServiceSwaggerRoute() {

        return route("product_service_swagger")
                .route(RequestPredicates.path("/aggregate/product-service/v3/api-docs"),
                        HandlerFunctions.http(productServiceUrl))
                .filter(setPath("/api-docs"))
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceSwaggerRoute() {

        return route("order_service_swagger")
                .route(RequestPredicates.path("/aggregate/order-service/v3/api-docs"),
                        HandlerFunctions.http(orderServiceUrl))
                .filter(setPath("/api-docs"))
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceSwaggerRoute() {

        return route("inventory_service_swagger")
                .route(RequestPredicates.path("/aggregate/inventory-service/v3/api-docs"),
                        HandlerFunctions.http(inventoryServiceUrl))
                .filter(setPath("/api-docs"))
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return route("fallbackRoute")
                .route(RequestPredicates.all(),
                        request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body("Service is temporarily unavailable, please try again later"))
                .build();
    }

}
