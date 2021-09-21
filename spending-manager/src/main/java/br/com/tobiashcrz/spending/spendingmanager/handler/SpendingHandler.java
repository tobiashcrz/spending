package br.com.tobiashcrz.spending.spendingmanager.handler;

import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import br.com.tobiashcrz.spending.spendingmanager.dto.SpentDTO;
import br.com.tobiashcrz.spending.spendingmanager.service.SpentsService;
import reactor.core.publisher.Mono;

@Component
public class SpendingHandler {

    @Autowired
    private SpentsService spentsService;
    
    public Mono<ServerResponse> getAllSpendings(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(spentsService.getAllSpents(), SpentDTO.class);
    }
    
    public Mono<ServerResponse> createSpent(ServerRequest serverRequest) {
        Mono<SpentDTO> spentToSave = serverRequest.bodyToMono(SpentDTO.class);
        return spentToSave.flatMap(request -> 
            ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(spentsService.createSpent(request), SpentDTO.class)
       );
    }    

    public Mono<ServerResponse> getById(ServerRequest serverRequest) {
        return spentsService.getById(Integer.valueOf(serverRequest.pathVariable("id")))
                .flatMap(spent -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(spent))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> updateById(ServerRequest serverRequest) {
        Mono<SpentDTO> spentToSave = serverRequest.bodyToMono(SpentDTO.class);

        return spentToSave.flatMap(request -> { 
            return spentsService.updateSpent(Integer.valueOf(serverRequest.pathVariable("id")), request)
            .flatMap(spent -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(spent))
            .switchIfEmpty(notFound().build());
        });
    }
}
