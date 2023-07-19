package com.lukk.githubexplorer.ports.service;

import com.lukk.githubexplorer.adapters.dto.GithubResponse;
import com.lukk.githubexplorer.adapters.dto.RepoDTO;
import com.lukk.githubexplorer.ports.api.RestClient;
import com.lukk.githubexplorer.ports.model.SearchData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * Service that communicates with GitHub to get data about popular repositories.
 * This is the primary implementation of {@link RepositoryService}.
 */
@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class GithubRepositoryService implements RepositoryService {

    private final RestClient<GithubResponse> restClient;
    private final UriBuilder uriBuilder;

    /**
     * {@inheritDoc}
     * If the count parameter in searchData is less than 100, a single request is made.
     * Otherwise, multiple requests are made and the results are merged.
     */
    @Override
    public Mono<RepoDTO> getPopularRepos(SearchData searchData) {

        if (searchData.count() < 100) {
            return makeSingleRequest(searchData);

        } else {
            return makeMultipleRequests(searchData);
        }
    }

    private Mono<RepoDTO> makeSingleRequest(SearchData searchData) {
        String url = uriBuilder.buildUri(searchData);
        log.info("Request url: {}", url);

        return restClient.requestPopularRepos(url)
                .flatMap(githubResponse -> Mono.just(RepoDTO.of(githubResponse)));
    }

    private Mono<RepoDTO> makeMultipleRequests(SearchData searchData) {
        int pages = calculatePagesNumber(searchData);
        SearchData updatedSearchData = createSearchDataWithCorrectCount(searchData);
        log.info("Making multiple request with updated search data: {}", updatedSearchData);

        return Flux.range(0, pages)
                .flatMap(i -> restClient.requestPopularRepos(uriBuilder.buildUri(updatedSearchData, String.valueOf(i)))
                )
                .collectList()
                .flatMap(githubResponses -> {
                    List<String> mergedRepositories = getMergedRepositories(searchData, githubResponses);

                    return Mono.just(new RepoDTO(mergedRepositories));
                });
    }

    private static int calculatePagesNumber(SearchData searchData) {
        return searchData.count() / 99 + 1;
    }

    private static SearchData createSearchDataWithCorrectCount(SearchData searchData) {
        return new SearchData(searchData.language(), searchData.since(), 99);
    }

    private static ArrayList<String> getMergedRepositories(SearchData searchData, List<GithubResponse> githubResponses) {
        return new ArrayList<>(
                githubResponses.stream()
                        .map(RepoDTO::of)
                        .flatMap(repoDTO -> repoDTO.repositories().stream())
                        .limit(searchData.count())
                        .toList());
    }
}
