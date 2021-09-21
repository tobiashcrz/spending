package br.com.tobiashcrz.spending.spendingbff.proxy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.tobiashcrz.spending.spendingbff.dto.SpentDTO;
import br.com.tobiashcrz.spending.spendingbff.utils.BaseTest;
import reactor.core.publisher.Flux;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpendingManagerProxyTest extends BaseTest {
    
    @Value("${clients.spending-manager.base-url}")
    private String baseUrl;

    @Autowired
    @InjectMocks
    private SpendingManagerProxy proxy;

    @MockBean
    WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    WebClient.RequestBodyUriSpec requestBodyUriSpec;
    
    @Mock
    WebClient.RequestHeadersSpec requestHeadersSpec;
    
    @Mock
    WebClient.ResponseSpec responseSpec;


    @BeforeEach
    public void setUp() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(Mockito.mock(WebClient.ResponseSpec.class));

        MockitoAnnotations.initMocks(true);
    }
    
    @Test
    public void testSearch() throws IOException {
        given(webClient.get()
                .uri(anyString())
                .accept(any())
                .retrieve()
                .bodyToFlux(SpentDTO.class))
        .willReturn(getAllSpentsMock());
        
        Flux<SpentDTO> spents = proxy.search();

        verify(webClient, times(2)).get();

        assertThat(spents).isNotNull();
    }

    @Test
    public void testGet() throws IOException {
        when(webClient.get()
                .uri(anyString())
                .accept(any())
                .retrieve()
                .bodyToMono(SpentDTO.class))
        .thenReturn(getSpentByIdMock());
        
        SpentDTO spent = proxy.get(1).block();
        
        Assertions.assertThat(spent).isEqualTo(getSpentDTOMock());
    }

}
