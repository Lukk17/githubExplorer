package com.lukk.githubexplorer.ports.api;

import reactor.core.publisher.Mono;

/**
 * Interface for making HTTP requests and receiving responses.
 *
 * @param <T> the type of the object included in the response body.
 */
public interface RestClient<T> {
    /**
     * Makes a GET request to the provided URL and returns a Mono of type T that represents the result of the request.
     *
     * @param url the URL to which the request is made.
     * @return a Mono of type T that represents the response body.
     */
    Mono<T> requestPopularRepos(String url);
}
