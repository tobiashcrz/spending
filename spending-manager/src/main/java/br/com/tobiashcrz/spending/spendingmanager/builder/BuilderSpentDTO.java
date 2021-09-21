package br.com.tobiashcrz.spending.spendingmanager.builder;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.tobiashcrz.spending.spendingmanager.dto.SpentDTO;
import br.com.tobiashcrz.spending.spendingmanager.entity.SpentsEntity;
import br.com.tobiashcrz.spending.spendingmanager.entity.TagsEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BuilderSpentDTO {

    public SpentDTO build(SpentsEntity spentsEntity) {
        SpentDTO build = SpentDTO.builder()
                .id(spentsEntity.getId())
                .name(spentsEntity.getName())
                .description(spentsEntity.getDescription())
                .date(spentsEntity.getDateSpent())
                .amount(spentsEntity.getAmount())
                .tags(spentsEntity.getTags().stream()
                        .map(BuilderSpentDTO::buildTags)
                        .collect(Collectors.toList()))
                .build();
        log.info("build {}", build);
        return build;
    }
    
    private static String buildTags(TagsEntity tags) {
        return tags.getName();
    }
}
