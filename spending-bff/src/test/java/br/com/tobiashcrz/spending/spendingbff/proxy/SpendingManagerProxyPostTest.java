package br.com.tobiashcrz.spending.spendingbff.proxy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.tobiashcrz.spending.spendingbff.dto.SpentDTO;
import br.com.tobiashcrz.spending.spendingbff.utils.BaseTest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpendingManagerProxyPostTest  extends BaseTest {
    @Value("${clients.spending-manager.base-url}")
    private String baseUrl;

    @Autowired
    @InjectMocks
    private SpendingManagerProxy proxy;

    @Mock
    WebClient webClient;

    @Mock
    WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    WebClient.RequestBodySpec requestBodySpec;

    @Mock
    WebClient.ResponseSpec responseSpec;

    @Test
    public void createSpentSuccess() throws IOException {
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(any(),any())).thenReturn(requestBodySpec);
        when(requestHeadersSpec.header(any(),any())).thenReturn(requestHeadersSpec);
        when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ArgumentMatchers.<Class<SpentDTO>>notNull())).thenReturn(getSpentByIdMock());

        Assert.assertNotNull(proxy.create(getSpentDTOMock()));
    }

    @Test
    public void updateSpentSuccess() throws IOException {
        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(any(),any())).thenReturn(requestBodySpec);
        when(requestHeadersSpec.header(any(),any())).thenReturn(requestHeadersSpec);
        when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ArgumentMatchers.<Class<SpentDTO>>notNull())).thenReturn(getSpentByIdMock());
        
        Assert.assertNotNull(proxy.update(1, getSpentDTOMock()));
    }
}
