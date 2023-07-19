package com.lukk.githubexplorer.adapters.api;

import com.lukk.githubexplorer.adapters.dto.RepoDTO;
import com.lukk.githubexplorer.ports.model.SearchData;
import com.lukk.githubexplorer.ports.service.RepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;

@WebFluxTest(DefaultController.class)
class DefaultControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private RepositoryService repositoryService;

    @Test
    void whenGetPopularRepos_thenReturnCorrectNumberOfRepos() {
        // Given
        SearchData searchData = new SearchData("Java", LocalDate.now(), 10);
        RepoDTO expectedRepos = new RepoDTO(List.of("testName"));

        when(repositoryService.getPopularRepos(searchData)).thenReturn(Mono.just(expectedRepos));

        // When
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/repos/popular")
                        .queryParam("language", "Java")
                        .queryParam("since", LocalDate.now().toString())
                        .queryParam("count", "10")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                // Then
                .expectStatus().isOk()
                .expectBody(RepoDTO.class)
                .isEqualTo(expectedRepos);
    }

    @Test
    void whenGetPopularRepos_andErrorHappened_thenReturnBadRequest() {
        // Given
        SearchData searchData = new SearchData("Java", LocalDate.now(), 10);

        when(repositoryService.getPopularRepos(searchData)).thenThrow(new RuntimeException("testException"));

        // When
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/repos/popular")
                        .queryParam("language", "Java")
                        .queryParam("since", LocalDate.now().toString())
                        .queryParam("count", "10")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                // Then
                .expectStatus().isBadRequest();
    }

    @Test
    void whenGetPopularRepos_andWrongDate_thenReturnBadRequest() {
        // Given
        LocalDate wrongDate = LocalDate.of(1000, 1, 1);

        // When
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/repos/popular")
                        .queryParam("language", "Java")
                        .queryParam("since", wrongDate)
                        .queryParam("count", "10")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                // Then
                .expectStatus().isBadRequest();
    }

    @Test
    void whenGetPopularRepos_andWrongFutureDate_thenReturnBadRequest() {
        // Given
        LocalDate wrongDate = LocalDate.of(3000, 1, 1);

        // When
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/repos/popular")
                        .queryParam("language", "Java")
                        .queryParam("since", wrongDate)
                        .queryParam("count", "10")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                // Then
                .expectStatus().isBadRequest();
    }
}
