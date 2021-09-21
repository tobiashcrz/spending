package br.com.tobiashcrz.spending.spendingbff.handler;

import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import br.com.tobiashcrz.spending.spendingbff.dto.SpentDTO;
import br.com.tobiashcrz.spending.spendingbff.proxy.SpendingManagerProxy;
import reactor.core.publisher.Mono;

@Component
public class SpendingBffHandler {
    @Autowired
    private SpendingManagerProxy proxy;
    
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(proxy.search(), SpentDTO.class);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        Mono<SpentDTO> spentToSave = serverRequest.bodyToMono(SpentDTO.class);
        return spentToSave.flatMap(request -> 
            ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(proxy.create(request), SpentDTO.class)
       );
    }
    
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ServerResponse> getById(ServerRequest serverRequest) {
        return proxy.get(Integer.valueOf(serverRequest.pathVariable("id")))
                .flatMap(spent -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(spent))
                .onErrorResume(e -> notFound().build());
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        Mono<SpentDTO> spentToSave = serverRequest.bodyToMono(SpentDTO.class);

        return spentToSave.flatMap(request -> { 
            return proxy.update(Integer.valueOf(serverRequest.pathVariable("id")), request)
            .flatMap(spent -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(spent))
            .onErrorResume(e -> notFound().build());
        });
    }

}
