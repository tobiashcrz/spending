package br.com.tobiashcrz.spending.spendingmanager.builder;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.tobiashcrz.spending.spendingmanager.dto.SpentDTO;
import br.com.tobiashcrz.spending.spendingmanager.entity.SpentsEntity;
import br.com.tobiashcrz.spending.spendingmanager.entity.TagsEntity;

@Component
public class BuilderSpentsEntity {

    public SpentsEntity build(SpentDTO request) {
       return SpentsEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .dateSpent(request.getDate())
                .amount(request.getAmount())
                .tags(buildTags(request))
                .build();
    }

    public SpentsEntity merge(SpentsEntity spentsEntity, SpentDTO request) {
        spentsEntity.setName(request.getName());
        spentsEntity.setDescription(request.getDescription());
        spentsEntity.setDateSpent(request.getDate());
        spentsEntity.setAmount(request.getAmount());
        spentsEntity.setTags(buildTags(request));
        return spentsEntity;
    }
    
    private Set<TagsEntity> buildTags(SpentDTO request) {
        return request.getTags().stream().map(tag -> {
            return TagsEntity.builder()
            .name(tag)
            .build();
        }).collect(Collectors.toSet());
    }
}
