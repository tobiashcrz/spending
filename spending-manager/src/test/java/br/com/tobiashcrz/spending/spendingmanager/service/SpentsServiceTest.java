package br.com.tobiashcrz.spending.spendingmanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.tobiashcrz.spending.spendingmanager.dto.SpentDTO;
import br.com.tobiashcrz.spending.spendingmanager.repository.SpentsRepository;
import br.com.tobiashcrz.spending.spendingmanager.utils.BaseTest;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpentsServiceTest extends BaseTest {
    @Autowired
    private SpentsService spentsService;

    @MockBean
    private SpentsRepository spentsRepository;

    @Test
    public void testByIdSuccess() throws IOException {
        when(spentsRepository.findById(any())).thenReturn(getSpentsEntityMock());
        StepVerifier
            .create(spentsService.getById(1))
            .expectNext(getSpentDTOMock())
            .verifyComplete();
    }
    
    @Test
    public void testByIdEmpty() throws IOException {
        when(spentsRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        StepVerifier
            .create(spentsService.getById(10))
            .expectNextCount(0)
            .verifyComplete();
    }

    @Test
    public void testFindAllSuccess() throws IOException {
        when(spentsRepository.findAll()).thenReturn(getAllSpentsEntityMock());
        List<SpentDTO> block = spentsService.getAllSpents()
                .collectList()
                .block();
        assertThat(block).hasSize(2);
    }

    @Test
    public void testFindAllEmpty() throws IOException {
        when(spentsRepository.findAll()).thenReturn(new ArrayList());
        List<SpentDTO> block = spentsService.getAllSpents()
                .collectList()
                .block();
        assertThat(block).hasSize(0);
    }
    
    @Test
    public void testCreateSuccess() throws IOException {
        when(spentsRepository.save(any())).thenReturn(getSpentsEntityMock().get());
        StepVerifier
            .create(spentsService.createSpent(getSpentDTOMock()))
            .expectNext(getSpentDTOMock())
            .verifyComplete();
    }

    @Test
    public void testUpdateSuccess() throws IOException {
        when(spentsRepository.findById(any())).thenReturn(getSpentsEntityMock());
        when(spentsRepository.save(any())).thenReturn(getSpentsEntityMock().get());
        StepVerifier
            .create(spentsService.updateSpent(1, getSpentDTOMock()))
            .expectNext(getSpentDTOMock())
            .verifyComplete();
    }

    @Test
    public void testUpdateNotFound() throws IOException {
        when(spentsRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        StepVerifier
            .create(spentsService.updateSpent(1, getSpentDTOMock()))
            .expectNextCount(0)
            .verifyComplete();
    }
}
