package br.com.tobiashcrz.spending.spendingmanager.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.tobiashcrz.spending.spendingmanager.builder.BuilderSpentDTO;
import br.com.tobiashcrz.spending.spendingmanager.builder.BuilderSpentsEntity;
import br.com.tobiashcrz.spending.spendingmanager.dto.SpentDTO;
import br.com.tobiashcrz.spending.spendingmanager.entity.SpentsEntity;
import br.com.tobiashcrz.spending.spendingmanager.repository.SpentsRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class SpentsService {

    @Autowired
    private SpentsRepository spentsRepository;
    
    @Autowired
    private BuilderSpentsEntity builderSpentsEntity;

    @Autowired
    private BuilderSpentDTO builderSpentDTO;
    
    public Flux<SpentDTO> getAllSpents() {
        log.info("SpentsService - getAllSpents - START {}", System.currentTimeMillis());
        
        Flux<SpentDTO> response = Flux.fromStream(spentsRepository.findAll()
                .stream()
                .map(spentsEntity -> builderSpentDTO.build(spentsEntity)));
        
        log.info("SpentsService - getAllSpents - END {}", response);
        
        return response;
    }
    
    public Mono<SpentDTO> createSpent(SpentDTO request) {
        log.info("SpentsService - createSpent - START {}", request);
       
        SpentsEntity spentsEntity = builderSpentsEntity.build(request);
        
        Mono<SpentDTO> response = Mono.just(builderSpentDTO.build(spentsRepository.save(spentsEntity)));
        
        log.info("SpentsService - createSpent - END {}", response);
        
        return response;
    }

    public Mono<SpentDTO> getById(Integer id) {
        log.info("SpentsService - getById - START {}", id);

        SpentDTO spentDTO = spentsRepository.findById(id)
                .map(builderSpentDTO::build)
                .orElse(null);

        Mono<SpentDTO> response;
        if(Objects.nonNull(spentDTO)) {
            response = Mono.just(spentDTO);
        } else {
            response = Mono.empty();
        }
        
        log.info("SpentsService - getById - END {}", response);

        return response;
    }
    
    public Mono<SpentDTO> updateSpent(Integer id, SpentDTO request) {
        log.info("SpentsService - updateSpent - START {} {}", id, request);

        Optional<SpentsEntity> findSpentsEntity = spentsRepository.findById(id);

        if(!findSpentsEntity.isPresent()) {
            return Mono.empty();
        }
        
        SpentsEntity spentsEntity = builderSpentsEntity.merge(findSpentsEntity.get(), request);
        
        Mono<SpentDTO> response = Mono.just(builderSpentDTO.build(spentsRepository.save(spentsEntity)));
        
        log.info("SpentsService - updateSpent - END {}", response);
        
        return response;
    }
    
}

