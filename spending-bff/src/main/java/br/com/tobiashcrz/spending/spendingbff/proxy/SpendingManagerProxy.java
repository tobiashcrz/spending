package br.com.tobiashcrz.spending.spendingbff.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.tobiashcrz.spending.spendingbff.dto.SpentDTO;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class SpendingManagerProxy {
    @Autowired
    private WebClient webClient;

    @Value("${clients.spending-manager.base-url}")
    private String baseUrl;

    @Value("${clients.spending-manager.path-get-all}")
    private String pathGetAll;

    @Value("${clients.spending-manager.path-create}")
    private String pathCreate;

    @Value("${clients.spending-manager.path-get-id}")
    private String pathGetById;

    @Value("${clients.spending-manager.path-update}")
    private String pathUpdate;
    
    public Flux<SpentDTO> search() {
        log.info("SpendingManagerProxy.search - start.");
        
        Flux<SpentDTO> response = this.webClient.get()
                .uri(getUriWithParams(pathGetAll))
                .retrieve()
                .bodyToFlux(SpentDTO.class);
        
        log.info("SpendingManagerProxy.search - end. {}", response);
        
        return response;
    }

    public Mono<SpentDTO> create(SpentDTO request) {
        log.info("SpendingManagerProxy.create - start. {}", request);
        
        Mono<SpentDTO> response = this.webClient.post()
                .uri(getUriWithParams(pathCreate))
                .body(Mono.just(request), SpentDTO.class)
                .retrieve()
                .bodyToMono(SpentDTO.class);
        
        log.info("SpendingManagerProxy.create - end. {}", response);
        
        return response;
    }
    
    public Mono<SpentDTO> get(Integer id) {
        log.info("SpendingManagerProxy.get - start. {}", id);
        
        Mono<SpentDTO> response = this.webClient.get()
                .uri(getUriWithParams(pathGetById, id))
                .retrieve()
                .bodyToMono(SpentDTO.class);
        
        log.info("SpendingManagerProxy.search - end. {}", response);
        
        return response;
    }
    
    public Mono<SpentDTO> update(Integer id, SpentDTO request) {
        log.info("SpendingManagerProxy.update - start. {}, {}", id, request);
        
        Mono<SpentDTO> response = this.webClient.put()
                .uri(getUriWithParams(pathUpdate, id))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(SpentDTO.class);
        
        log.info("SpendingManagerProxy.update - end. {}, {}", id, response);
        
        return response;
    }

    private String getUriWithParams(String url) {
        return UriComponentsBuilder
                .fromUriString(baseUrl)
                .path(url)
                .build()
                .toUriString();
    }

    private String getUriWithParams(String url, Integer id) {
        return UriComponentsBuilder
                .fromUriString(baseUrl)
                .path(url)
                .build(id)
                .toString();
    }
}
