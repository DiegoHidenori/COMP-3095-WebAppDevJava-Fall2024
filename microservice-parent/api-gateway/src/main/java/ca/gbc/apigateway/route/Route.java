package ca.gbc.apigateway.route;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
@Slf4j
public class Route {

    @Value("${product.service.url}")    // Dynamic. Either localhost or container
    private String productServiceUrl;

    @Value("${order.service.url}")
    private String orderServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> productServiceRoute() {

        log.info("Initializing product service route with URL: {}", productServiceUrl);
        return GatewayRouterFunctions.route("product_service")
                .route(RequestPredicates.path("/api/product"), request -> {

                    log.info("Request received for product-service: {}", request.uri());

                    try{
                        ServerResponse response = HandlerFunctions.http(productServiceUrl).handle(request);
                        log.info("Response status: {}", response.statusCode());
                        return response;
                    } catch (Exception e) {
                        log.error("Error occurred while routing to: {}", e.getMessage(), e);
                        return ServerResponse.status(500).body("Error occurred when routing to product-service");
                    }

                })
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {

        log.info("Initializing order service route with URL: {}", orderServiceUrl);
        return GatewayRouterFunctions.route("order_service")
                .route(RequestPredicates.path("/api/order"), request -> {

                    log.info("Request received for order-service: {}", request.uri());

                    try{
                        ServerResponse response = HandlerFunctions.http(orderServiceUrl).handle(request);
                        log.info("Response status: {}", response.statusCode());
                        return response;
                    } catch (Exception e) {
                        log.error("Error occurred while routing to: {}", e.getMessage(), e);
                        return ServerResponse.status(500).body("Error occurred when routing to order-service");
                    }

                })
                .build();

    }

}
