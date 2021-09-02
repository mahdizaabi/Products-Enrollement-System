package com.mahdi.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator getwayRouter(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p.path("/get")
                        .filters(f -> f
                        .addRequestHeader("dd","dd")
                        .addRequestParameter("param", "value"))
                        .uri("http://httpbin.org:80"))

                .route(p -> p.path("/course-service/**").uri("lb://course-service"))
                .route(p -> p.path("/user-service/**").uri("lb://user-service"))
                .route(p -> p.path("/user-service-new/**").filters(f -> f.rewritePath("/user-service-new/(?<segment>.*)"
                        , "/currency-conversion-feign/${segment}"))
                        .uri("lb://currency-conversion"))
                .build();
    }
}




























































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































