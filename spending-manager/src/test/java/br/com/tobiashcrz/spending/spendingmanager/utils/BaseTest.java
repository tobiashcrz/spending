package br.com.tobiashcrz.spending.spendingmanager.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import br.com.tobiashcrz.spending.spendingmanager.dto.SpentDTO;
import br.com.tobiashcrz.spending.spendingmanager.entity.SpentsEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class BaseTest {
    private static final String JSON_FINDBYID_RESPONSE_SUCCESS = "json/spent_by_id.json";
    private static final String JSON_GETALLSPENTS_RESPONSE_SUCCESS = "json/spent_get_all_dto.json";
    private static final String JSON_FINDBYID_ENTITY_RESPONSE_SUCCESS = "json/spent_by_id_entity.json";
    private static final String JSON_GETALL_ENTITY_RESPONSE_SUCCESS = "json/spent_all_entity.json";

    @Autowired
    private ObjectMapper objectMapper;

    protected Mono<SpentDTO> getSpentByIdMock() throws IOException {
        return Mono.just(readJsonAndParse(JSON_FINDBYID_RESPONSE_SUCCESS, SpentDTO.class));
    }

    protected SpentDTO getSpentDTOMock() throws IOException {
        return readJsonAndParse(JSON_FINDBYID_RESPONSE_SUCCESS, SpentDTO.class);
    }

    protected Optional<SpentsEntity> getSpentsEntityMock() throws IOException {
        return Optional.of(readJsonAndParse(JSON_FINDBYID_ENTITY_RESPONSE_SUCCESS, SpentsEntity.class));
    }

    protected List<SpentsEntity> getAllSpentsEntityMock() throws IOException {
        return jsonArrayToObjectList(JSON_GETALL_ENTITY_RESPONSE_SUCCESS, SpentsEntity.class);
    }

    protected List<SpentDTO> getAllSpentsDTOMock() throws IOException {
        return readJsonAndParse(JSON_GETALLSPENTS_RESPONSE_SUCCESS, List.class);
    }

    protected Flux<SpentDTO> getAllSpentsMock() throws IOException {
        return Flux.just(readJsonAndParse(JSON_GETALLSPENTS_RESPONSE_SUCCESS, SpentDTO[].class));
    }

    protected Flux<SpentDTO> getAllSpentsEmptyMock() throws IOException {
        return Flux.just();
    }

    protected <T> T readJsonAndParse(String filename, Class<T> clazz) throws IOException {
        InputStream inputStream = new ClassPathResource(filename).getInputStream();
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        T readValue = objectMapper.readValue(reader, clazz);
        return readValue;
    }

    protected <T> List<T> readJsonAndParseList(String filename, Class<T> clazz) throws IOException {
        InputStream inputStream = new ClassPathResource(filename).getInputStream();
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
        List<T> ts = objectMapper.readValue(reader, listType);
        return ts;
    }
    
    public <T> List<T> jsonArrayToObjectList(String filename, Class<T> tClass) throws IOException {
        InputStream inputStream = new ClassPathResource(filename).getInputStream();
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
        List<T> ts = objectMapper.readValue(reader, listType);
        return ts;
    }


}
