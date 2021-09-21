package br.com.tobiashcrz.spending.spendingmanager.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import br.com.tobiashcrz.spending.spendingmanager.handler.SpendingHandler;

@Configuration
@EnableWebFlux
public class ApiRouter {
     
    @Bean
    public RouterFunction<ServerResponse> apiRoute(SpendingHandler spendingHandler){
        return RouterFunctions
            .route(GET("/spendings").and(accept(MediaType.APPLICATION_JSON))
                    ,spendingHandler::getAllSpendings)
            .andRoute(POST("/spent").and(accept(MediaType.APPLICATION_JSON))
                    ,spendingHandler::createSpent)
            .andRoute(GET("/spent/{id}").and(accept(MediaType.APPLICATION_JSON))
                    ,spendingHandler::getById)
            .andRoute(PUT("/spent/{id}").and(accept(MediaType.APPLICATION_JSON))
                    ,spendingHandler::updateById);
    }
    
}
