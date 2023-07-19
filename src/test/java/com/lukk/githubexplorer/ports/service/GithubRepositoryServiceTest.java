package com.lukk.githubexplorer.ports.service;

import com.lukk.githubexplorer.adapters.dto.GithubResponse;
import com.lukk.githubexplorer.adapters.dto.RepoDTO;
import com.lukk.githubexplorer.ports.api.RestClient;
import com.lukk.githubexplorer.ports.model.SearchData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class GithubRepositoryServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private UriBuilder uriBuilder;

    @InjectMocks
    GithubRepositoryService githubRepositoryService;

    @ParameterizedTest
    @CsvSource(value = {"1:1", "99:1", "150:2", "199:3"}, delimiter = ':')
    void whenGetPopularRepos_thenReturnCorrectNumberOfRequests(int input, int expected) {
        // Given
        SearchData searchData = new SearchData("java", LocalDate.of(2020, 6, 20), input);

        GithubResponse githubResponse = new GithubResponse(List.of(new GithubResponse.GithubItem("testName")));
        when(restClient.requestPopularRepos(any())).thenReturn(Mono.just(githubResponse));

        // When
        Mono<RepoDTO> actual = githubRepositoryService.getPopularRepos(searchData);

        // Then
        assertEquals(expected, Objects.requireNonNull(actual.block()).repositories().size());
    }

    @Test
    void whenGetPopularRepos_thenReturnCorrectRepositories() {
        // Given
        SearchData searchData = new SearchData("java", LocalDate.of(2020, 6, 20), 2);

        RepoDTO repoDTO = new RepoDTO(List.of("testName1", "testName2"));

        GithubResponse githubResponse = new GithubResponse(List.of(
                new GithubResponse.GithubItem("testName1"),
                new GithubResponse.GithubItem("testName2"))
        );
        when(uriBuilder.buildUri(eq(searchData))).thenReturn("http://test.test");
        when(restClient.requestPopularRepos(eq("http://test.test"))).thenReturn(Mono.just(githubResponse));

        // When
        Mono<RepoDTO> actual = githubRepositoryService.getPopularRepos(searchData);

        // Then
        assertEquals(repoDTO.repositories(), Objects.requireNonNull(actual.block()).repositories());
    }

    @Test
    void whenGetPopularRepos_whenOther99Repos_thenReturnCorrectRepositories() {
        // Given
        SearchData searchData = new SearchData("java", LocalDate.of(2020, 6, 20), 102);
        SearchData updatedSearchData = new SearchData("java", LocalDate.of(2020, 6, 20), 99);

        RepoDTO repoDTO = new RepoDTO(List.of("testName1", "testName2", "testName3", "testName4"));

        GithubResponse githubResponseFirst = new GithubResponse(List.of(
                new GithubResponse.GithubItem("testName1"),
                new GithubResponse.GithubItem("testName2"))
        );
        GithubResponse githubResponseSecond = new GithubResponse(List.of(
                new GithubResponse.GithubItem("testName3"),
                new GithubResponse.GithubItem("testName4"))
        );
        when(uriBuilder.buildUri(eq(updatedSearchData), eq("0"))).thenReturn("http://test1.test");
        when(uriBuilder.buildUri(eq(updatedSearchData), eq("1"))).thenReturn("http://test2.test");
        when(restClient.requestPopularRepos(eq("http://test1.test"))).thenReturn(Mono.just(githubResponseFirst));
        when(restClient.requestPopularRepos(eq("http://test2.test"))).thenReturn(Mono.just(githubResponseSecond));

        // When
        Mono<RepoDTO> actual = githubRepositoryService.getPopularRepos(searchData);

        // Then
        assertEquals(repoDTO.repositories(), Objects.requireNonNull(actual.block()).repositories());
    }

}
