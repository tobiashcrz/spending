package br.com.tobiashcrz.spending.spendingbff.router;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.tobiashcrz.spending.spendingbff.config.SecurityConfig;
import br.com.tobiashcrz.spending.spendingbff.dto.SpentDTO;
import br.com.tobiashcrz.spending.spendingbff.handler.SpendingBffHandler;
import br.com.tobiashcrz.spending.spendingbff.proxy.SpendingManagerProxy;
import br.com.tobiashcrz.spending.spendingbff.utils.BaseTest;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ApiRouter.class, SpendingBffHandler.class, SecurityConfig.class})
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@WebFluxTest
public class ApiRouterTest extends BaseTest {
    @Autowired
    private ApplicationContext context;
    
    @MockBean
    private SpendingManagerProxy spendingManagerProxy;

    private WebTestClient webTestClient;
    
    private static final String baseUrl = "http://localhost:8081";
    private static final String pathById = "/spent/{id}";
    private static final String pathAll = "/spendings";
    private static final String pathCreate = "/spent";
    private static final String pathUpdate = "/spent/{id}";
    
    @BeforeEach
    public void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }
    
    @Test
    public void testUnauthorized() throws IOException {
        webTestClient
            .get()
            .uri("/")
            .accept(MediaType.ALL)
            .headers((headers) -> headers.setBasicAuth("hacker", "hacker"))
            .exchange()
            .expectStatus()
            .isUnauthorized()
            .expectBody()
            .isEmpty();
    }
    
    @Test
    public void testGetSpentById() throws IOException {
        when(spendingManagerProxy.get(any())).thenReturn(getSpentByIdMock());
        webTestClient
            .get()
            .uri(UriComponentsBuilder
                    .fromUriString(baseUrl)
                    .path(pathById)
                    .build(1))
            .accept(MediaType.APPLICATION_JSON)
            .headers((headers) -> headers.setBasicAuth("admin", "admin"))
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
    public void testGetAllSpents() throws IOException {
        when(spendingManagerProxy.search()).thenReturn(getAllSpentsMock());
        webTestClient
            .get()
            .uri(UriComponentsBuilder
                    .fromUriString(baseUrl)
                    .path(pathAll)
                    .toUriString())
            .accept(MediaType.APPLICATION_JSON)
            .headers((headers) -> headers.setBasicAuth("admin", "admin"))
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
        when(spendingManagerProxy.search()).thenReturn(getAllSpentsEmptyMock());
        webTestClient
            .get()
            .uri(UriComponentsBuilder
                    .fromUriString(baseUrl)
                    .path(pathAll)
                    .toUriString())
            .accept(MediaType.APPLICATION_JSON)
            .headers((headers) -> headers.setBasicAuth("admin", "admin"))
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
        when(spendingManagerProxy.create(any())).thenReturn(getSpentByIdMock());
        webTestClient
            .post()
            .uri(UriComponentsBuilder
                    .fromUriString(baseUrl)
                    .path(pathCreate)
                    .toUriString())
            .body(BodyInserters.fromValue(getSpentDTOMock()))
            .accept(MediaType.APPLICATION_JSON)
            .headers((headers) -> headers.setBasicAuth("admin", "admin"))
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
    public void testCreateSpentUnauthorized() throws IOException {
        when(spendingManagerProxy.create(any())).thenReturn(getSpentByIdMock());
        webTestClient
            .post()
            .uri(UriComponentsBuilder
                    .fromUriString(baseUrl)
                    .path(pathCreate)
                    .toUriString())
            .body(BodyInserters.fromValue(getSpentDTOMock()))
            .accept(MediaType.APPLICATION_JSON)
            .headers((headers) -> headers.setBasicAuth("test", "test"))
            .exchange()
            .expectStatus()
            .isForbidden();
    }
    
    @Test
    public void testUpdateSpent() throws IOException {
        when(spendingManagerProxy.update(any(), any())).thenReturn(getSpentByIdMock());
        webTestClient
            .put()
            .uri(UriComponentsBuilder
                    .fromUriString(baseUrl)
                    .path(pathUpdate)
                    .build(1))
            .body(BodyInserters.fromValue(getSpentDTOMock()))
            .accept(MediaType.APPLICATION_JSON)
            .headers((headers) -> headers.setBasicAuth("admin", "admin"))
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
    public void testUpdateSpentUnauthorized() throws IOException {
        when(spendingManagerProxy.update(any(), any())).thenReturn(getSpentByIdMock());
        webTestClient
            .put()
            .uri(UriComponentsBuilder
                    .fromUriString(baseUrl)
                    .path(pathUpdate)
                    .build(1))
            .body(BodyInserters.fromValue(getSpentDTOMock()))
            .accept(MediaType.APPLICATION_JSON)
            .headers((headers) -> headers.setBasicAuth("test", "test"))
            .exchange()
            .expectStatus()
            .isForbidden();
    }
}
