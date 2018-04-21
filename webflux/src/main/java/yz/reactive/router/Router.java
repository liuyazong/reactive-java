package yz.reactive.router;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import yz.reactive.entity.User;
import yz.reactive.mongo.handler.UserHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * author: liuyazong
 * datetime: 2018/4/19 上午11:58
 * <p></p>
 */
@Configuration
public class Router {
    @Autowired
    private UserHandler userHandler;

    @Autowired
    private yz.reactive.redis.handler.UserHandler redisUserHandler;

    @Bean
    public RouterFunction mongoRouter() {
        return nest(
                path("/mongo/fp"),
                nest(
                        path("/user"),
                        nest(
                                accept(MediaType.APPLICATION_JSON_UTF8),
                                route(GET("/{id}"), request -> userHandler.findById(request.pathVariable("id")))
                                        .andRoute(method(HttpMethod.POST), request -> userHandler.insert(request.bodyToFlux(User.class)))
                        )
                )
        ).andNest(
                path("/redis/fp"),
                nest(
                        path("/user"),
                        nest(
                                accept(MediaType.APPLICATION_JSON_UTF8),
                                route(GET("/{id}"), request -> redisUserHandler.findById(request.pathVariable("id")))
                                        .andRoute(method(HttpMethod.POST), request -> redisUserHandler.insert(request.bodyToFlux(User.class)))
                        )
                )
        );
    }
}
