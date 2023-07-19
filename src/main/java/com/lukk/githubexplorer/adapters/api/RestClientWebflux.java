package com.lukk.githubexplorer.adapters.api;

import com.lukk.githubexplorer.adapters.dto.GithubResponse;
import com.lukk.githubexplorer.ports.api.RestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Implementation of the RestClient interface using Spring's WebClient.
 * It's designed to fetch data from GitHub's API.
 */
@Service
@RequiredArgsConstructor
@Primary
public class RestClientWebflux implements RestClient<GithubResponse> {

    private final WebClient webClient;

    @Value("${github.token}")
    private String token;

    /**
     * {@inheritDoc}
     * <p>
     * In this implementation, a GET request is made to the GitHub API.
     * The response is converted to a GithubResponse object.
     * If a GitHub token is provided, it is included in the request header.
     *
     * @param url The URL to make the request to.
     * @return A Mono of GithubResponse representing the response body.
     */
    @Override
    public Mono<GithubResponse> requestPopularRepos(String url) {

        if (!token.isBlank()) {
            return webClient.get()
                    .uri(url)
                    .header("Authorization", token)
                    .accept(MediaType.valueOf("application/vnd.github+json"))
                    .retrieve()
                    .bodyToMono(GithubResponse.class);
        }

        return webClient.get()
                .uri(url)
                .accept(MediaType.valueOf("application/vnd.github+json"))
                .retrieve()
                .bodyToMono(GithubResponse.class);
    }
}
