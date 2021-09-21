package br.com.tobiashcrz.spending.spendingmanager.router;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.tobiashcrz.spending.spendingmanager.dto.SpentDTO;
import br.com.tobiashcrz.spending.spendingmanager.handler.SpendingHandler;
import br.com.tobiashcrz.spending.spendingmanager.repository.SpentsRepository;
import br.com.tobiashcrz.spending.spendingmanager.service.SpentsService;
import br.com.tobiashcrz.spending.spendingmanager.utils.BaseTest;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ApiRouter.class, SpendingHandler.class})
@WebFluxTest
public class ApiRouterTest extends BaseTest {
    @Autowired
    private ApplicationContext context;
    
    @MockBean
    private SpentsService spentsService;

    @MockBean
    private SpentsRepository spentsRepository;
    
    private WebTestClient webTestClient;
    
    private static final String baseUrl = "http://localhost:8080";
    private static final String pathById = "/spent/{id}";
    private static final String pathAll = "/spendings";
    private static final String pathCreate = "/spent";
    private static final String pathUpdate = "/spent/{id}";
    
    @BeforeEach
    public void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }
    
    @Test
    public void testGetSpentById() throws IOException {
        when(spentsService.getById(1)).thenReturn(getSpentByIdMock());
        webTestClient
            .get()
            .uri(UriComponentsBuilder
                    .fromUriString(baseUrl)
                    .path(pathById)
                    .build(1))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(SpentDTO.class)
            .value(response -> {
                Assertions.assertThat(response.getId()).isEqualTo(1);
                Assertions.assertThat(response.getName()).isEqualTo("Água");
                Assertions.assertThat(response.getDescription()).isEqualTo("Conta de Água");
                Assertions.assertThat(response.getDate()).isEqualTo(LocalDate.parse("09/09/2021", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                Assertions.assertThat(response.getAmount()).isEqualTo(100.0);
                Assertions.assertThat(response.getTags()).contains("AGUA");
            });
    }

    @Test
    public void testGetNotFoundSpentById() throws IOException {
        when(spentsService.getById(10)).thenReturn(Mono.empty());
        webTestClient
            .get()
            .uri(UriComponentsBuilder
                    .fromUriString(baseUrl)
                    .path(pathById)
                    .build(10))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.NOT_FOUND);
    }
    
    @Test
    public void testGetAllSpents() throws IOException {
        when(spentsService.getAllSpents()).thenReturn(getAllSpentsMock());
        webTestClient
            .get()
            .uri(UriComponentsBuilder
                    .fromUriString(baseUrl)
                    .path(pathAll)
                    .toUriString())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(SpentDTO.class)
            .value(response -> { 
                Assertions.assertThat(response).hasSize(2);
            });
    }
    
    @Test
    public void testGetEmptyAllSpents() throws IOException {
        when(spentsService.getAllSpents()).thenReturn(getAllSpentsEmptyMock());
        webTestClient
            .get()
            .uri(UriComponentsBuilder
                    .fromUriString(baseUrl)
                    .path(pathAll)
                    .toUriString())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(SpentDTO.class)
            .value(response -> { 
                Assertions.assertThat(response).hasSize(0);
            });
    }
    
    @Test
    public void testCreateSpent() throws IOException {
        when(spentsService.createSpent(any())).thenReturn(getSpentByIdMock());
        webTestClient
            .post()
            .uri(UriComponentsBuilder
                    .fromUriString(baseUrl)
                    .path(pathCreate)
                    .toUriString())
            .body(BodyInserters.fromValue(getSpentDTOMock()))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(SpentDTO.class)
            .value(response -> {
                Assertions.assertThat(response.getId()).isEqualTo(1);
                Assertions.assertThat(response.getName()).isEqualTo("Água");
                Assertions.assertThat(response.getDescription()).isEqualTo("Conta de Água");
                Assertions.assertThat(response.getDate()).isEqualTo(LocalDate.parse("09/09/2021", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                Assertions.assertThat(response.getAmount()).isEqualTo(100.0);
                Assertions.assertThat(response.getTags()).contains("AGUA");
            });
    }
    
    @Test
    public void testUpdateSpent() throws IOException {
        when(spentsRepository.findById(any())).thenReturn(getSpentsEntityMock());
        when(spentsService.updateSpent(any(), any())).thenReturn(getSpentByIdMock());
        webTestClient
            .put()
            .uri(UriComponentsBuilder
                    .fromUriString(baseUrl)
                    .path(pathUpdate)
                    .build(1))
            .body(BodyInserters.fromValue(getSpentDTOMock()))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(SpentDTO.class)
            .value(response -> {
                Assertions.assertThat(response.getId()).isEqualTo(1);
                Assertions.assertThat(response.getName()).isEqualTo("Água");
                Assertions.assertThat(response.getDescription()).isEqualTo("Conta de Água");
                Assertions.assertThat(response.getDate()).isEqualTo(LocalDate.parse("09/09/2021", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                Assertions.assertThat(response.getAmount()).isEqualTo(100.0);
                Assertions.assertThat(response.getTags()).contains("AGUA");
            });
    }
    
    @Test
    public void testUpdateNotFoundSpent() throws IOException {
        when(spentsRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        when(spentsService.updateSpent(any(), any())).thenReturn(Mono.empty());
        webTestClient
            .put()
            .uri(UriComponentsBuilder
                    .fromUriString(baseUrl)
                    .path(pathUpdate)
                    .build(10))
            .body(BodyInserters.fromValue(getSpentDTOMock()))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.NOT_FOUND);
    }
}
